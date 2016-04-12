package ru.finnetrolle.telebot.service.message

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.SendMessage
import ru.finnetrolle.telebot.service.telegram.SimpleTelegramBot
import java.util.concurrent.ConcurrentLinkedQueue
import javax.annotation.PostConstruct

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 12.04.16.
 */
@Component
class BroadcastService: Runnable {

    override fun run() {
        while(true) {
            try {
                val start = System.currentTimeMillis()
                if (q.isNotEmpty()) {
                    var messages = mutableListOf<SendMessage>()
                    while (messages.size < 30 && q.isNotEmpty()) {
                        messages.add(q.poll())
                    }
                    val result = messages
                            .map { x -> telegram.sendMessage(x) }
                            .map { x -> x.chatId }
                            .joinToString(", ")
                    log.info("Messages sent to: \n$result in ${1001L - (System.currentTimeMillis() - start)} msec")
                }
                val timeToSleep = 1001L - (System.currentTimeMillis() - start)
                if (timeToSleep > 0) {
                    Thread.sleep(timeToSleep)
                }
            } catch (e: Exception) {

            }
        }
    }

    @Autowired private lateinit var telegram: SimpleTelegramBot

    private val q = ConcurrentLinkedQueue<SendMessage>()

    fun enqueue(msg: SendMessage) {
        q.add(msg)
    }

    @PostConstruct
    fun init() {
        val thread = Thread(this)
        thread.start()
    }

    companion object {
        private val log = LoggerFactory.getLogger(BroadcastService::class.java)
    }



}