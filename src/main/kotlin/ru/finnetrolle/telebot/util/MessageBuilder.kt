package ru.finnetrolle.telebot.util

import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardHide


/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

object MessageBuilder {

    //    todo: commented for better times
    //    fun build(chatId: String, text: String, kb: ReplyKeyboardMarkup): SendMessage {
    //        val msg = build(chatId, text)
    //        msg.replayMarkup = kb
    //        return msg
    //    }

    /**
     * Build SendMessage to send to chatId (chatid can be equal to user telegram id)
     */
    fun build(chatId: String, text: String): SendMessage {
        val msg = SendMessage()
        msg.chatId = chatId
        msg.text = text
        msg.enableMarkdown(true)
//        val rkh = ReplyKeyboardHide()
//        rkh.hideKeyboard = true
//        msg.replayMarkup = rkh
        return msg
    }

    fun split(message: SendMessage): Collection<SendMessage> {
        if (message.text.length > 4000) {
            val msgs = mutableListOf<SendMessage>()
            val count = message.text.length / 4000
            (0..count-2).forEach { i ->
                msgs.add(MessageBuilder.build(message.chatId, message.text.substring(i * 4000, (i + 1) * 4000)))
            }
            return msgs
        } else {
            return listOf(message)
        }
    }

    //    todo: commented for better times
    //    fun createKeyboard(keys: List<String>): ReplyKeyboardMarkup {
    //        val kb = ReplyKeyboardMarkup()
    //        val list = ArrayList<String>()
    //        for(i in keys.indices) {
    //            list.add("/$i ${keys[i]}")
    //        }
    //        kb.keyboard = listOf(list)
    //        return kb
    //    }

}