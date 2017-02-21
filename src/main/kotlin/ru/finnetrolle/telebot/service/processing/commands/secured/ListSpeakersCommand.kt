package ru.finnetrolle.telebot.service.processing.commands.secured

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.internal.PilotService
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
//@Component
class ListSpeakersCommand : AbstractSecuredCommand() {

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var pilotService: PilotService

    override fun name() = "/LS"

    override fun description() = loc.getMessage("telebot.command.description.ls")

    override fun execute(pilot: Pilot, data: String): String {
        val speakers = pilotService.getSpeakers().sortedBy { it.characterName.toUpperCase() }.map { it.characterName }
        return loc.getMessage("messages.response.ls", speakers.size, speakers.joinToString(separator = "\n"))
    }

}