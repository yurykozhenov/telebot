package ru.finnetrolle.telebot.telegramapi

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.TelegramBotsApi
import javax.annotation.PostConstruct

/**
 * Created by maxsyachin on 12.03.16.
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