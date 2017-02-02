package ru.finnetrolle.telebot.service.processing.commands.unsecured

import ru.finnetrolle.telebot.service.processing.engine.CommandExecutor

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
abstract class AbstractUnsecuredCommand : CommandExecutor {
    override fun secured() = false
}