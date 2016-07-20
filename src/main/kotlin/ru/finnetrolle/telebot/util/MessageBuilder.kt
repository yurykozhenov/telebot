package ru.finnetrolle.telebot.util

import org.telegram.telegrambots.api.methods.SendMessage
import org.telegram.telegrambots.api.objects.ReplyKeyboardHide

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
        val rkh = ReplyKeyboardHide()
        rkh.hideKeyboard = true
        msg.replayMarkup = rkh
        return msg
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