package ru.finnetrolle.telebot.service.processing.commands

import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.service.processing.engine.CommandExecutor

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
class DummyExecutor: CommandExecutor {

    override fun execute() {
        throw UnsupportedOperationException()
    }
}