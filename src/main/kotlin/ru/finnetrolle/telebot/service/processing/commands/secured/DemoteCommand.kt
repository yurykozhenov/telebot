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
class DemoteCommand : AbstractSecuredCommand() {

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var pilotService: PilotService

    override fun name() = "/DEM"

    override fun description() = loc.getMessage("telebot.command.description.dem")

    override fun execute(pilot: Pilot, data: String): String {
        return pilotService.setModerator(data, false).decide({
            loc.getMessage("messages.user.demoted", it.characterName)
        }, {
            loc.getMessage("messages.user.not.found")
        })
    }

}