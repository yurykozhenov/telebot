package ru.finnetrolle.telebot

import org.junit.Assert
import org.telegram.telegrambots.api.methods.send.SendMessage

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

fun assertMessage(result: SendMessage, expect: SendMessage) {
    Assert.assertEquals(expect.chatId, result.chatId)
    Assert.assertEquals(expect.text, result.text)
}