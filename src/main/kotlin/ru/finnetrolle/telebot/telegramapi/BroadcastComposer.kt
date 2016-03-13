package ru.finnetrolle.telebot.telegramapi

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.SendMessage
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.model.PilotRepository

/**
 * Created by maxsyachin on 13.03.16.
 */

@Component
class BroadcastComposer @Autowired constructor (
        val repo: PilotRepository
) {

    fun compose(text: String): List<SendMessage> {
        return repo.findAll().map { p ->
            val msg = SendMessage()
            msg.chatId = p.id.toString()
            msg.text = text
            msg
        }.toList()
    }

}