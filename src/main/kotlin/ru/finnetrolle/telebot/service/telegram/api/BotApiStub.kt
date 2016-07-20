package ru.finnetrolle.telebot.service.telegram.api

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.api.methods.SendMessage

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
open class BotApiStub(
        val sendDelay: Long = 0,
        val sendCallback: (SendMessage) -> SendMessage = { msg -> msg }
) : BotApi {

    override fun send(message: SendMessage): BotApi.Send {
        if (sendDelay > 0L) {
            Thread.sleep(sendDelay)
        }
        log.info("Stub 'sending' message to ${message.chatId}")
        sendCallback.invoke(message)
        return BotApi.Send.Success(message.chatId.toLong(), 0)
    }

    companion object {
        val log = LoggerFactory.getLogger(BotApiStub::class.java)
    }

}