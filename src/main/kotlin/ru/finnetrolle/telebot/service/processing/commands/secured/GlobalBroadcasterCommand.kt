package ru.finnetrolle.telebot.service.processing.commands.secured

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.internal.UserService
import ru.finnetrolle.telebot.service.telegram.TelegramBotService
import ru.finnetrolle.telebot.util.MessageBuilder
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
@Component
open class GlobalBroadcasterCommand : AbstractSecuredCommand() {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var telegram: TelegramBotService

    @Autowired
    private lateinit var loc: MessageLocalization

    override fun name() = "/CAST"

    override fun description() = loc.getMessage("telebot.command.description.cast")

    override fun execute(pilot: Pilot, data: String): String {
        val users = userService.getLegalUsers()
        telegram.broadcast(users.map { u -> MessageBuilder.build(u.id.toString(), data) }.toList())
        return loc.getMessage("messages.broadcast.result", users.size)
    }
}