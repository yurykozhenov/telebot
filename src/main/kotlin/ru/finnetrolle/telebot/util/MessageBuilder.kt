package ru.finnetrolle.telebot.util

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.api.methods.send.SendMessage


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
//        msg.enableMarkdown(true)
//        val rkh = ReplyKeyboardHide()
//        rkh.hideKeyboard = true
//        msg.replayMarkup = rkh
        return msg
    }

    private val log = LoggerFactory.getLogger(MessageBuilder::class.java)

    fun split(message: SendMessage): Collection<SendMessage> {
        if (message.text.length < 4000) {
            return listOf(message)
        }
        var sb = StringBuilder()
        val msgs = mutableListOf<SendMessage>()
        message.text.split("\n").forEach { line ->
            line.forEach { c ->
                sb.append(c)
                if (sb.length >= 4100) {
                    msgs.add(MessageBuilder.build(message.chatId, sb.toString()))
                    log.debug("Split by char")
                    sb = StringBuilder()
                }
            }
            sb.append("\n")
            if (sb.length >= 4000) {
                msgs.add(MessageBuilder.build(message.chatId, sb.toString()))
                sb = StringBuilder()
                log.debug("Split by newline")
            }
        }
//
//
//        message.text.forEach { c ->
//            sb.append(c)
//            if (sb.length >= 4000) {
//                msgs.add(MessageBuilder.build(message.chatId, sb.toString()))
//                sb = StringBuilder()
//            }
//        }
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