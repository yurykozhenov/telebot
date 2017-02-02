package ru.finnetrolle.telebot.service.processing.commands.secured

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.internal.PilotService
import ru.finnetrolle.telebot.service.telegram.TelegramBotService
import ru.finnetrolle.telebot.util.MessageBuilder
import ru.finnetrolle.telebot.util.MessageLocalization
import java.util.*

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
@Component
open class GlobalBroadcasterCommand : AbstractSecuredCommand() {

    @Autowired
    private lateinit var pilotService: PilotService

    @Autowired
    private lateinit var telegram: TelegramBotService

    @Autowired
    private lateinit var loc: MessageLocalization

    private val log = LoggerFactory.getLogger(GlobalBroadcasterCommand::class.java)

    override fun name() = "/CAST"

    override fun description() = loc.getMessage("telebot.command.description.cast")

    override fun execute(pilot: Pilot, data: String): String {
        try {
            val users = pilotService.getLegalUsers()
            val message = "Broadcast from ${pilot.characterName} at ${Date()} \n$data"
            telegram.broadcast(users.map { u -> MessageBuilder.build(u.id.toString(), message) })
            return loc.getMessage("messages.broadcast.result", users.size)
        } catch (e: Exception) {
            log.error("Can't execute command global broadcast because of", e)
        }
        return "Some very bad happened"
    }
}