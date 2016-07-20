package ru.finnetrolle.telebot.service.telegram.broadcasting

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.SendMessage
import ru.finnetrolle.telebot.service.telegram.api.BotApi
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
open class BroadcastService {

    @Value("\${telegram.bot.threads}")
    private lateinit var threads: Integer

    @Value("\${telegram.bot.delay}")
    private lateinit var delay: Integer

    @Value("\${telegram.bot.batch}")
    private lateinit var batch: Integer

    val q: Queue<BroadcastUnit.Task> = ConcurrentLinkedQueue()
    val bots = mutableListOf<String>()

    open fun init(api: BotApi) {
        (1..threads.toInt()).forEach { t ->
            val name = UUID.randomUUID().toString()
            bots.add(name)
            BroadcastUnit(name, q, api, batch.toInt(), delay.toLong()).start()
        }
    }

    open fun send(message: SendMessage) {
        if (bots.isNotEmpty()) {
            q.offer(BroadcastUnit.Task.Send(message))
        } else {
            log.error("Trying to send messages without worker units")
        }
    }

    open fun send(messages: Collection<SendMessage>) {
        if (bots.isNotEmpty()) {
            q.addAll(messages.map { m -> BroadcastUnit.Task.Send(m) }.toList())
        }else {
            log.error("Trying to send messages without worker units")
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(BroadcastService::class.java)
    }

}