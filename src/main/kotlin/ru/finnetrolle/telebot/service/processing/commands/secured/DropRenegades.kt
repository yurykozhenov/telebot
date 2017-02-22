package ru.finnetrolle.telebot.service.processing.commands.secured

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.internal.PilotService
import ru.finnetrolle.telebot.service.processing.commands.AbstractSecuredCommand
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
@Component
class DropRenegades : AbstractSecuredCommand() {

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var pilotService: PilotService

    override fun name() = "/DROPRENE"

    override fun description() = loc.getMessage("telebot.command.description.drop.renegades")

    override fun execute(pilot: Pilot, data: String): String {
        val dropped = pilotService.dropRenegades()
        return dropped.joinToString("\n", loc.getMessage("messages.drop.renegades", dropped.size))
    }

}