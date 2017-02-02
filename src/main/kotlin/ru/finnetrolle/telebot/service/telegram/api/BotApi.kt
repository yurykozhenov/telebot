package ru.finnetrolle.telebot.service.telegram.api

import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.exceptions.TelegramApiException

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
interface BotApi {

    interface Send {
        data class Success(val chatId: Long, val spend: Long) : Send
        data class Failed(val chatId: Long, val e: TelegramApiException) : Send
    }

    open fun send(message: SendMessage): Send

}