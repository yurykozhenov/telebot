package ru.finnetrolle.telebot.service.telegram

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.SendMessage
import ru.finnetrolle.telebot.model.PilotRepository

/**
* Licence: MIT
* Legion of xXDEATHXx notification bot for telegram
* Created by finnetrolle on 13.03.16.
*/

@Component
class BroadcastComposer @Autowired constructor (
        val repo: PilotRepository
) {

    fun compose(text: String): List<SendMessage> {
        return repo.findByRenegadeFalse().map { p ->
            val msg = SendMessage()
            msg.chatId = p.id.toString()
            msg.text = text
            msg
        }.toList()
    }

}