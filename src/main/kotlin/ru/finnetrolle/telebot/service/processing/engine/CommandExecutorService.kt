package ru.finnetrolle.telebot.service.processing.engine

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.send.SendMessage
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.internal.MeetingService
import ru.finnetrolle.telebot.util.MessageBuilder
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
open class CommandExecutorService {

    private val executors = mutableMapOf<String, CommandExecutor>()

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var meet: MeetingService

    open fun addExecutor(executor: CommandExecutor) {
        executors.put(executor.name().toUpperCase(), executor)
    }

    open fun execute(command: String, data: String, pilot: Pilot, chatId: String): SendMessage {
        log.debug("COMMAND DEBUG. COMMAND = [$command]")
        if (command.toUpperCase() == "/HELP")
            return MessageBuilder.build(chatId, generateHelp(pilot))
        if (command.length > 8 && command.toUpperCase().substring(0, 9) == "/MEET-YES-") {
            val id = command.substring(10, command.length - 1)
            log.debug("Want accept meeting with id = $id")
            return MessageBuilder.build(chatId, meet.acceptMeeting(id))
        }
        if (command.length > 7 && command.toUpperCase().substring(0, 8) == "/MEET-NO-") {
            val id = command.substring(9, command.length - 1)
            log.debug("Want decline meeting with id = $id")
            return MessageBuilder.build(chatId, meet.declineMeeting(id))
        }

        val executor = executors[command.toUpperCase()]
        return if (executor != null) {
            if (executor.secured()) {
                if (pilot.moderator) {
                    MessageBuilder.build(chatId, executor.execute(pilot, data))
                } else {
                    MessageBuilder.build(chatId, loc.getMessage("messages.access.denied"))
                }
            } else {
                MessageBuilder.build(chatId, executor.execute(pilot, data))
            }
        } else {
            MessageBuilder.build(chatId, loc.getMessage("messages.unknown"))
        }
    }

    open fun generateHelp(pilot: Pilot): String {
        val msg = loc.getMessage("messages.help.message")
        return if (pilot.moderator)
            executors
                    .map { e -> "${e.value.name()} - ${e.value.description()}" }
                    .joinToString(separator = "\n", prefix = msg)
        else
            executors
                    .filter { e -> !e.value.secured() }
                    .map { e -> "${e.value.name()} - ${e.value.description()}" }
                    .joinToString(separator = "\n", prefix = msg)
    }

    companion object {
        val log = LoggerFactory.getLogger(CommandExecutorService::class.java)
    }
}