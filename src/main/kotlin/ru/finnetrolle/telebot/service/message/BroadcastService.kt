package ru.finnetrolle.telebot.service.message

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.SendMessage
import org.telegram.telegrambots.api.objects.Message
import ru.finnetrolle.telebot.restful.BroadcastResource
import ru.finnetrolle.telebot.service.mailbot.MailbotService
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

    @Autowired
    lateinit private var mailService: MailbotService

    override fun run() {
        var checkMail = false
        while(true) {
            var piped = listOf<SendMessage>()
            var timeSpend = 0L
            try {
                if (q.isNotEmpty()) {
                    timeSpend = castSome()
                    checkMail = true
                } else {
                    if (checkMail) {
                        mailService.receiveMail()
                        checkMail = false
                    }
                }
                sleep(timeSpend)
            } catch (e: Exception) {
                log.error("Some error in broadcast service", e)
            }
        }
    }

    private val ONE_SECOND: Long = 1000L

    private fun sleep(timeSpend: Long) {
        val timeToSleep = ONE_SECOND - timeSpend
        if (timeToSleep > 0) {
            Thread.sleep(timeToSleep)
        }
    }

    private fun castSome(): Long {
        val start = System.currentTimeMillis()
        var messages = mutableListOf<SendMessage>()
        while (messages.size < 30 && q.isNotEmpty()) {
            messages.add(q.poll())
        }
        val result = messages
                .map { x ->
                    try {
                        telegram.sendMessage(x)
                    } catch (e: Exception) {
                        log.error("Can't send message to ${x.chatId} $x", e)
                        null
                    }
                }
                .map { x -> if (x!=null) x.chatId else 0 }
                .joinToString(", ")
        val timeSpend = System.currentTimeMillis() - start
        log.info("Messages sent to: \n$result in $timeSpend msec")
        return timeSpend
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