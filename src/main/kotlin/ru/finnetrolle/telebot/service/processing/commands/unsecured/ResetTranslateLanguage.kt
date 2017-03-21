package ru.finnetrolle.telebot.service.processing.commands.unsecured

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.internal.PilotService
import ru.finnetrolle.telebot.service.processing.commands.AbstractUnsecuredCommand
import ru.finnetrolle.telebot.util.MessageLocalization
import ru.finnetrolle.telebot.util.decide

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
class ResetTranslateLanguage : AbstractUnsecuredCommand() {

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var pilotService: PilotService

    override fun name() = "/STOPTRANSLATE"

    override fun description() = loc.getMessage("telebot.command.description.resetlang")

    override fun execute(pilot: Pilot, data: String): String {
        return pilotService.setTranslator(pilot.id, "").decide({
            loc.getMessage("messages.lang.changed", pilot.characterName, "русский")
        },{
            loc.getMessage("messages.user.not.found")
        })
    }

}