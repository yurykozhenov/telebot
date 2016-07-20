package ru.finnetrolle.telebot.service.processing.commands

import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.processing.engine.CommandExecutor

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
class DummyExecutor: CommandExecutor {

    override fun name() = "/DUMMY"

    override fun secured() = false

    override fun description() = "telebot.command.description.dummy"

    override fun execute(pilot: Pilot, data: String): String {
        return "${pilot.id} of $data"
    }

}