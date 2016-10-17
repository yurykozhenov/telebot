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
        var sb = StringBuilder()
        val msgs = mutableListOf<SendMessage>()
        message.text.forEach { c ->
            sb.append(c)
            if (sb.length >= 4000) {
                msgs.add(MessageBuilder.build(message.chatId, sb.toString()))
                sb = StringBuilder()
            }
        }
        if (sb.length != 0) {
            msgs.add(MessageBuilder.build(message.chatId, sb.toString()))
        }
        return msgs
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