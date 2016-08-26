package ru.finnetrolle.telebot.service.processing.commands.unsecured

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
class ListAlliancesCommand: AbstractUnsecuredCommand() {

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var allyService: AllyService

    override fun name() = "/LA"

    override fun description() = loc.getMessage("telebot.command.description.la")

    override fun execute(pilot: Pilot, data: String): String {
        val allys = allyService.getAll()
                .map { a -> "[${a.ticker}] - ${a.title}" }
                .sorted()
        return loc.getMessage("messages.response.la", allys.size, allys.joinToString(separator = "\n"))
    }
}