package ru.finnetrolle.telebot.service.telegram.processors

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.SendMessage
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.processing.engine.CommandExecutorService

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
open class CommandProcessor {

    @Autowired
    private lateinit var ces: CommandExecutorService

    open fun process(command: String, data: String, pilot: Pilot): SendMessage {
        log.debug("Processing command $command from $pilot with data = $data")
        return ces.execute(command, data, pilot, pilot.id.toString())
    }

    companion object {
        val log = LoggerFactory.getLogger(CommandProcessor::class.java)
    }
}