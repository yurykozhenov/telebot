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
class CheckCommand : AbstractSecuredCommand() {

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var userService: UserService

    override fun name() = "/CHECK"

    override fun description() = loc.getMessage("telebot.command.description.check")

    override fun execute(pilot: Pilot, data: String): String {
        val start = System.currentTimeMillis()
        val result = userService.check()
        return loc.getMessage("messages.response.check",
                (System.currentTimeMillis() - start) / 1000,
                result.renegaded.size, result.checked,
                result.renegaded.joinToString("\n"))
    }
}