package ru.finnetrolle.telebot.service.processing.commands.unsecured

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.internal.MeetingService
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
open class MeetInitiateCommand : AbstractUnsecuredCommand() {

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var meet: MeetingService

    override fun name() = "/MEET"

    override fun description() = loc.getMessage("telebot.command.description.meet")

    override fun execute(pilot: Pilot, data: String): String {
        return meet.createMeeting(pilot, data)
    }

}