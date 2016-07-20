package ru.finnetrolle.telebot.service.telegram.processors

import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.SendMessage
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.processing.MessageBuilder

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
open class CommandProcessor {
    open fun process(command: String, data: String, pilot: Pilot): SendMessage {
        return MessageBuilder.build(pilot.id.toString(), data)
    }
}