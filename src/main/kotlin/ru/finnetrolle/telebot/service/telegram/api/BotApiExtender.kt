package ru.finnetrolle.telebot.service.telegram.api

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.TelegramApiException
import org.telegram.telegrambots.TelegramBotsApi
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Message
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.bots.TelegramLongPollingBot

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

open class BotApiExtender(
        val name: String,
        val token: String,
        val processIncomingMessage: (Message) -> SendMessage
) : BotApi {

    private var api = object : TelegramLongPollingBot() {
        override fun getBotUsername() = name

        override fun getBotToken() = token

        override fun onUpdateReceived(request: Update) {
            when {
                request.hasMessage() -> {
                    log.trace("Received request from chatId = ${request.message?.chatId} with text = ${request.message?.text}")
                    send(processIncomingMessage.invoke(request.message))
                }
                request.hasChosenInlineQuery() -> {
                    log.warn("Not supported format - ChosenInlineQuery from ${request.chosenInlineQuery.from.id}")
                }
                request.hasInlineQuery() -> {
                    log.warn("Not supported format - InlineQuery from ${request.inlineQuery.from.id}")
                }
                else -> {
                    log.warn("Impossible Exception for unknown request type")
                }
            }
        }
    }

    init {
        try {
            TelegramBotsApi().registerBot(api)
        } catch (e: TelegramApiException) {
            log.error("Can't connect to telegram", e)
            log.error("Exit")
            System.exit(1)
        }
    }

    override fun send(message: SendMessage): BotApi.Send {
        try {
            log.trace("Trying to send response to ${message.chatId}")
            val start = System.currentTimeMillis()
            return BotApi.Send.Success(
                    api.sendMessage(message).chatId,
                    System.currentTimeMillis() - start)
        } catch (e: TelegramApiException) {
            log.error("Message sending was not success because of", e)
            return BotApi.Send.Failed(message.chatId.toLong(), e)
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(BotApiExtender::class.java)
    }

}