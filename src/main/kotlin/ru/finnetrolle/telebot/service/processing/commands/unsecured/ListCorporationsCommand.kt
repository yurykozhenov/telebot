package ru.finnetrolle.telebot.service.processing.commands.unsecured

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

//@Component
class ListCorporationsCommand : AbstractUnsecuredCommand() {

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var corpService: CorpService

    override fun name() = "/LC"

    override fun description() = loc.getMessage("telebot.command.description.lc")

    override fun execute(pilot: Pilot, data: String): String {
        val corps = corpService.getAll()
                .map { a -> "[${a.ticker}] - ${a.title}" }
                .sorted()
        return loc.getMessage("messages.response.lc", corps.size, corps.joinToString(separator = "\n"))
    }
}