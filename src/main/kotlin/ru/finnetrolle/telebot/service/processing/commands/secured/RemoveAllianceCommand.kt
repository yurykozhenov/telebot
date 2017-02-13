package ru.finnetrolle.telebot.service.processing.commands.secured

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.internal.AllyService
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
class RemoveAllianceCommand : AbstractSecuredCommand() {

    @Autowired
    private lateinit var allyService: AllyService

    @Autowired
    private lateinit var loc: MessageLocalization

    override fun name() = "/RMALLY"

    override fun description() = loc.getMessage("telebot.command.description.rmally")

    override fun execute(pilot: Pilot, data: String): String {
        val result = allyService.remove(data)
        return when (result) {
            is AllyService.Remove.Success -> loc.getMessage("messages.ally.removed")
            is AllyService.Remove.NotFound -> loc.getMessage("messages.ally.not.found")
        }
    }
}