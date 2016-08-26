package ru.finnetrolle.telebot.service.processing.commands.unsecured

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.internal.UserService
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
class ListModeratorsCommand: AbstractUnsecuredCommand() {

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var userService: UserService

    override fun name() = "/LM"

    override fun description() = loc.getMessage("telebot.command.description.lm")

    override fun execute(pilot: Pilot, data: String): String {
        val moders = userService.getModerators().sorted()
        return loc.getMessage("messages.response.lm", moders.size, moders.joinToString(separator = "\n"))
    }
}