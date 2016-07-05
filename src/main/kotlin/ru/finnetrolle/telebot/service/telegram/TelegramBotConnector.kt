package ru.finnetrolle.telebot.service.telegram

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.bots.TelegramLongPollingBot

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 14.04.16.
 */

class TelegramBotConnector(
        private val username: String,
        private val token: String):
        TelegramLongPollingBot() {

    override fun onUpdateReceived(update: Update?) {
        if (update == null) {
            log.warn("Null-value update received by $username bot")
            return
        }
    }

    override fun getBotUsername() = username

    override fun getBotToken() = token

    companion object {
        private val log = LoggerFactory.getLogger(TelegramBotConnector::class.java)
    }
}