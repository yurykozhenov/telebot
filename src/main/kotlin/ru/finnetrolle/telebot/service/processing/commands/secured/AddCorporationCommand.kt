package ru.finnetrolle.telebot.service.processing.commands.secured

import org.slf4j.LoggerFactory
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
class AddCorporationCommand : AbstractSecuredCommand() {

    @Autowired
    private lateinit var corpService: CorpService

    @Autowired
    private lateinit var loc: MessageLocalization

    override fun name() = "/ADDCORP"

    override fun description() = loc.getMessage("telebot.command.description.addcorp")

    override fun execute(pilot: Pilot, data: String): String {
        try {
            val result = corpService.add(data.toLong())
            return when (result) {
                is CorpService.Add.AlreadyInList -> loc.getMessage("messages.corp.in.list")
                is CorpService.Add.NotExist -> loc.getMessage("messages.corp.not.exist")
                is CorpService.Add.Success -> loc.getMessage("messages.corp.added")
            }
        } catch (e: NumberFormatException) {
            log.error("Can't convert '$data' to corporation id", e)
            return loc.getMessage("messages.errors.bad.data")
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(AddCorporationCommand::class.java)
    }
}