package ru.finnetrolle.telebot.service.processing.commands.secured

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.internal.PilotService
import ru.finnetrolle.telebot.service.processing.commands.unsecured.AbstractUnsecuredCommand
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
class ListModeratorsCommand : AbstractSecuredCommand() {

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var pilotService: PilotService

    override fun name() = "/LM"

    override fun description() = loc.getMessage("telebot.command.description.lm")

    override fun execute(pilot: Pilot, data: String): String {
        val moders = pilotService.getModerators().sortedBy { it.characterName }.map { it.characterName }
        return loc.getMessage("messages.response.lm", moders.size, moders.joinToString(separator = "\n"))
    }
}