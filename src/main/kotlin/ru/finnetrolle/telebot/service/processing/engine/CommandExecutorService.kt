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

    private fun preprocess(command: String, pilot: Pilot): SendMessage? {
        if (command.toUpperCase() == "/HELP")
            return MessageBuilder.build(pilot.id.toString(), generateHelp(pilot))
        if (command.length > 8 && command.toUpperCase().substring(0, 10) == "/MEET_YES_") {
            val id = command.substring(10, command.length)
            return MessageBuilder.build(pilot.id.toString(), meet.acceptMeeting(id))
        }
        if (command.length > 7 && command.toUpperCase().substring(0, 9) == "/MEET_NO_") {
            val id = command.substring(9, command.length)
            return MessageBuilder.build(pilot.id.toString(), meet.declineMeeting(id))
        }
        return null
    }

    open fun execute(command: String, data: String, pilot: Pilot, chatId: String): SendMessage {
        preprocess(command, pilot)?.let { return it }

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

    private val log = LoggerFactory.getLogger(CommandExecutorService::class.java)

}