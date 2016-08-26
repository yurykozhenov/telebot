package ru.finnetrolle.telebot.service.processing.commands.secured

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.internal.CorpService
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
@Component
class RemoveCorporationCommand: AbstractSecuredCommand() {

    @Autowired
    private lateinit var corpService: CorpService

    @Autowired
    private lateinit var loc: MessageLocalization

    override fun name() = "/RMCORP"

    override fun description() = loc.getMessage("telebot.command.description.rmcorp")

    override fun execute(pilot: Pilot, data: String): String {
        val result = corpService.removeCorporation(data)
        return when (result) {
            is CorpService.Remove.NotFound -> loc.getMessage("messages.corp.not.found")
            is CorpService.Remove.Success -> loc.getMessage("messages.corp.removed")
            else -> loc.getMessage("messages.impossible")
        }
    }
}