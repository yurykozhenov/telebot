package ru.finnetrolle.telebot.service.processing.engine

import org.telegram.telegrambots.api.methods.SendMessage
import ru.finnetrolle.telebot.model.Pilot

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

interface CommandExecutor {

    /**
     * name of command - case will be same in description
     */
    fun name(): String

    /**
     * is command only for moderator?
     */
    fun secured(): Boolean

    /**
     * command description for
     */
    fun description(): String

    /**
     * Main method of command - execution
     */
    fun execute(pilot: Pilot, data: String): String

}