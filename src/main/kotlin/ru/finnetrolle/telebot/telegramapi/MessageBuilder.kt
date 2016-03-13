package ru.finnetrolle.telebot.telegramapi

import org.telegram.telegrambots.api.methods.SendMessage
import org.telegram.telegrambots.api.objects.ReplyKeyboardMarkup
import java.util.*

/**
 * Created by maxsyachin on 13.03.16.
 */
object MessageBuilder {

    fun build(chatId: String, text: String, kb: ReplyKeyboardMarkup): SendMessage {
        val msg = build(chatId, text)
        msg.replayMarkup = kb
        return msg
    }

    fun build(chatId: String, text: String): SendMessage {
        val msg = SendMessage()
        msg.enableMarkdown(true)
        msg.chatId = chatId
        msg.text = text
        return msg
    }

    fun createKeyboard(keys: List<String>): ReplyKeyboardMarkup {
        val kb = ReplyKeyboardMarkup()
        val list = ArrayList<String>()
        for(i in keys.indices) {
            list.add("/${i} ${keys.get(i)}")
        }
        kb.keyboard = listOf(list)
        return kb
    }

}