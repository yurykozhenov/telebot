package ru.finnetrolle.telebot.telegramapi

import org.springframework.beans.factory.annotation.Autowired
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

    val bot = TelegramBotsApi()

    @PostConstruct
    fun postConstruct() {
        bot.registerBot(simple)
    }

}