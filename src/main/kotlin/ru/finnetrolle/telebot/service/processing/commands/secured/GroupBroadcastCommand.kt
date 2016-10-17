package ru.finnetrolle.telebot.service.processing.commands.secured

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.internal.UserService
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
open class GroupBroadcastCommand : AbstractSecuredCommand() {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var telegram: TelegramBotService

    @Autowired
    private lateinit var loc: MessageLocalization

    override fun name() = "/GC"

    override fun description() = loc.getMessage("telebot.command.description.gc")

    override fun execute(pilot: Pilot, data: String): String {
        val text = data.substringAfter(" ")
        val users = userService.getLegalUsers(data.substringBefore(" "))
        val message = "Broadcast from ${pilot.characterName} at ${Date()} \n$text"
        telegram.broadcast(users.map { u -> MessageBuilder.build(u.id.toString(), message) }.toList())
        return loc.getMessage("messages.broadcast.result", users.size)
    }
}