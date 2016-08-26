package ru.finnetrolle.telebot.service.processing.engine

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.SendMessage
import ru.finnetrolle.telebot.model.Pilot
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

    open fun addExecutor(executor: CommandExecutor) {
        executors.put(executor.name().toUpperCase(), executor)
    }

    open fun execute(command: String, data: String, pilot: Pilot, chatId: String): SendMessage {
        if (command.toUpperCase().equals("/HELP")) {
            return MessageBuilder.build(chatId, generateHelp(pilot))
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
                    .map { e -> "${e.value.name()} - ${loc.getMessage(e.value.description())}" }
                    .joinToString(separator = "\n", prefix = msg)
        else
            executors
                    .filter { e -> !e.value.secured() }
                    .map { e -> "${e.value.name()} - ${loc.getMessage(e.value.description())}" }
                    .joinToString(separator = "\n", prefix = msg)
    }

    companion object {
        val log = LoggerFactory.getLogger(CommandExecutorService::class.java)
    }
}