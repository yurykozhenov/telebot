package ru.finnetrolle.telebot.util

import org.junit.Test

import org.junit.Assert.*

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

    @Test
    fun checkSplitting() {
        val s = StringBuilder()
        (1..5000).forEach { i -> s.append("&") }
        assertEquals(5000, s.toString().length)
        val message = MessageBuilder.build("12345", s.toString())
        assertEquals(2, MessageBuilder.split(message).size)
    }

}