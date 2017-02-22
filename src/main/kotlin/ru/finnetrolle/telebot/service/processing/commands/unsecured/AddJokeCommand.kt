package ru.finnetrolle.telebot.service.processing.commands.unsecured

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.additional.JokeService
import ru.finnetrolle.telebot.service.processing.commands.AbstractUnsecuredCommand
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
class AddJokeCommand : AbstractUnsecuredCommand() {

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var joker: JokeService

    private val JOKE_MAX_LENGTH = 1000

    override fun name() = "/ADDJOKE"

    override fun description() = loc.getMessage("telebot.command.description.addjoke")

    override fun execute(pilot: Pilot, data: String): String {
        return when {
            (data.isEmpty() || data.toUpperCase().equals("/ADDJOKE")) -> loc.getMessage("telegram.joke.add.nothing")
            (data.length > JOKE_MAX_LENGTH) -> loc.getMessage("telegram.joke.add.too.long")
            else -> {
                joker.addJoke(pilot, data)
                return loc.getMessage("telegram.joke.add.success")
            }
        }
    }


}