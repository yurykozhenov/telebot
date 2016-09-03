package ru.finnetrolle.telebot.service.telegram.broadcasting

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.api.methods.send.SendMessage
import ru.finnetrolle.telebot.service.telegram.api.BotApi
import java.util.*

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
class BroadcastUnit(
        val unitName: String,
        val queue: Queue<Task>,
        val bot: BotApi,
        val batchSize: Int = 30,
        val timeout: Long = 1000L
) : Thread() {

    interface Task {
        data class Send(val message: SendMessage) : Task
        data class Quit(val unitName: String) : Task
    }

    override fun run() {
        var alive = true
        while (alive) {
            val start = System.currentTimeMillis()
            if (queue.isNotEmpty()) {
                for (i in 0..batchSize - 1) {
                    val task = queue.poll()
                    if (task == null) {
                        log.debug("[PART] Sent $i messages in ${System.currentTimeMillis() - start} ms")
                        break
                    } else {
                        when {
                            task is Task.Send -> {
                                trySend(task.message)
                            }
                            task is Task.Quit && task.unitName.equals(unitName) -> {
                                alive = false
                            }
                            else -> {
                                queue.offer(task)
                            }
                        }
                    }
                }
                log.debug("[FULL] Sent $batchSize messages in ${System.currentTimeMillis() - start} ms")
            }
            sleep(start, System.currentTimeMillis())
        }
    }

    private fun trySend(msg: SendMessage) {
        val sendResult = bot.send(msg)
        when (sendResult) {
            is BotApi.Send.Success -> {
                log.trace("Message successfully sent to ${sendResult.chatId} in ${sendResult.spend} ms")
            }
            is BotApi.Send.Failed -> {
                log.error("Message can not be sent to ${sendResult.chatId}", sendResult.e)
            }
        }
    }

    private fun sleep(start: Long, end: Long) {
        if (timeout > (end - start)) {
            try {
                log.trace("Sleep ${timeout - (end - start)} ms")
                sleep(timeout - (end - start))

            } catch (e: InterruptedException) {
                log.error("Interrupted", e)
            }
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(BroadcastUnit::class.java)
    }

}