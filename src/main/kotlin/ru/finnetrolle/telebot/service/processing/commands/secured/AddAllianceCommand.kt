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
class AddAllianceCommand : AbstractSecuredCommand() {

    @Autowired
    private lateinit var allyService: AllyService

    @Autowired
    private lateinit var loc: MessageLocalization

    override fun name() = "/ADDALLY"

    override fun description() = loc.getMessage("telebot.command.description.addally")

    override fun execute(pilot: Pilot, data: String): String {
        val result = allyService.addAlly(data)
        return when (result) {
            is AllyService.AddResponse.AllianceAdded -> loc.getMessage("messages.ally.added", result.alliance.title)
            is AllyService.AddResponse.AllianceIsAlreadyInList -> loc.getMessage("messages.ally.in.list")
            is AllyService.AddResponse.AllianceIsNotExist -> loc.getMessage("messages.ally.not.exist")
            else -> loc.getMessage("messages.impossible")
        }
    }


}