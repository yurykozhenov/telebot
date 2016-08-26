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
class ListUsersCommand : AbstractSecuredCommand() {

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var userService: UserService

    override fun name() = "/LU"

    override fun description() = loc.getMessage("telebot.command.description.lu")

    override fun execute(pilot: Pilot, data: String): String {
        val users = userService.getAllUsers()
                .map { u -> "${renemark(u)}${u.characterName}${modermark(u)}" }
        return loc.getMessage("messages.response.lu", users.size, users.joinToString(separator = ",\n"))
    }

    private fun renemark(pilot: Pilot) = if (pilot.renegade) "[R] " else ""
    private fun modermark(pilot: Pilot) = if (pilot.moderator) " [M]" else ""
}