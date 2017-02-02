package ru.finnetrolle.telebot.service.processing.commands.secured

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.internal.PilotService
import ru.finnetrolle.telebot.util.MessageLocalization
import ru.finnetrolle.telebot.util.decide

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
@Component
class UserStatusCommand : AbstractSecuredCommand() {

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var pilotService: PilotService

    override fun name() = "/US"

    override fun description() = loc.getMessage("telebot.command.description.us")

    override fun execute(pilot: Pilot, data: String): String {
        return pilotService.getPilot(data).decide({
            "${it.characterName} [${it.characterId}] \n moderator: ${it.moderator}\n renegade: ${it.renegade}"
        }, {
            loc.getMessage("messages.user.not.found")
        })
    }
}