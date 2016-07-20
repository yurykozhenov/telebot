package ru.finnetrolle.telebot.service.processing

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.service.processing.engine.CommandExecutorService
import javax.annotation.PostConstruct

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
open class DummyToDelete {

    @Autowired
    private lateinit var executorService: CommandExecutorService

    @PostConstruct
    fun init() {
        println("DUMMY HERE")
        executorService.commands().forEach { println(it) }
    }
}