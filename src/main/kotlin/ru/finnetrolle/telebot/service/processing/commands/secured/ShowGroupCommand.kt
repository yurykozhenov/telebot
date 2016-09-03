package ru.finnetrolle.telebot.service.processing.commands.secured

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.internal.UserService
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
class ShowGroupCommand : AbstractSecuredCommand() {

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var userService: UserService

    override fun name() = "/SHOWGROUP"

    override fun description() = loc.getMessage("telebot.command.description.showgroup")

    override fun execute(pilot: Pilot, data: String): String {
        log.debug("Trying to find some users for group $data")
        try {
            val prefix = loc.getMessage("messages.group.header", data)
            return userService.getLegalUsers(data)
                    .map { u -> u.characterName }
                    .joinToString(separator = "\n", prefix = prefix)
        } catch (e: Exception) {
            log.error(e.message, e)
            return loc.getMessage("messages.impossible")
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(ShowGroupCommand::class.java)
    }
}