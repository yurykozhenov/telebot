package ru.finnetrolle.telebot.service.processing.commands.secured

import ru.finnetrolle.telebot.service.processing.engine.CommandExecutor

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
abstract class AbstractSecuredCommand: CommandExecutor {
    override fun secured() = true
}