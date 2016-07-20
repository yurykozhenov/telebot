package ru.finnetrolle.telebot.service.processing

import org.junit.Assert.*
import org.junit.Test

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
class MessageBuilderTest {

    private val CHAT_ID = "chatid"
    private val TEXT = "some text"

    @Test
    fun checkMessageBuilding() {
        val msg = MessageBuilder.build(CHAT_ID, TEXT)
        assertEquals(CHAT_ID, msg.chatId)
        assertEquals(TEXT, msg.text)
    }

}