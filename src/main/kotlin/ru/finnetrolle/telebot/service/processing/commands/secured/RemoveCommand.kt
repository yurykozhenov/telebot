package ru.finnetrolle.telebot.service.processing.commands.secured

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
class RemoveCommand: AbstractSecuredCommand() {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var loc: MessageLocalization

    override fun name() = "/REMOVE"

    override fun description() = loc.getMessage("telebot.command.description.remove")

    override fun execute(pilot: Pilot, data: String): String {
        return "removed ${userService.remove(data)}"
    }
}