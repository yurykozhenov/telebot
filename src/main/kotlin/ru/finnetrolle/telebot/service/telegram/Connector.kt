package ru.finnetrolle.telebot.service.telegram

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.TelegramBotsApi
import javax.annotation.PostConstruct

/**
* Licence: MIT
* Legion of xXDEATHXx notification bot for telegram
* Created by finnetrolle on 12.03.16.
*/

@Component
class Connector @Autowired constructor(
        val simple: SimpleTelegramBot
) {

    @Value("\${telegram.bot.alive}")
    private var alive: Boolean = false

    val bot = TelegramBotsApi()

    @PostConstruct
    fun postConstruct() {
        if (alive) {
            bot.registerBot(simple)
        }
    }

}