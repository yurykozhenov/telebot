package ru.finnetrolle.telebot.service.telegram.processors

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.SendMessage
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.processing.engine.CommandExecutorService
import javax.annotation.PostConstruct

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
        return ces.execute(command, data, pilot, pilot.id.toString())
    }

    @PostConstruct
    fun init() {
        log.info("\n\nCES loaded:\n${ces.generateHelp(Pilot(moderator = true))}\n\n")
        println("CES loaded:\n${ces.generateHelp(Pilot(moderator = true))}")
    }

    companion object {
        val log = LoggerFactory.getLogger(CommandProcessor::class.java)
    }
}