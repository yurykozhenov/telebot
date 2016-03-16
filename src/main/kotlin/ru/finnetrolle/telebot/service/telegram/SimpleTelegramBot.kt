package ru.finnetrolle.telebot.service.telegram

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.api.objects.User
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import ru.finnetrolle.telebot.telegramapi.*
import kotlin.collections.List

/**
* Licence: MIT
* Legion of xXDEATHXx notification bot for telegram
* Created by finnetrolle on 12.03.16.
*/

@Component
class SimpleTelegramBot @Autowired constructor(
        val registerer: RegistererService,
        val broadcastComposer: BroadcastComposer,
        val userService: UserService
): TelegramLongPollingBot() {

    @Value("\${telegram.bot.token}")
    private lateinit var token: String

    @Value("\${telegram.bot.username}")
    private lateinit var username: String

    override fun getBotUsername(): String? {
        return username
    }

    override fun getBotToken(): String? {
        return token
    }

    override fun onUpdateReceived(update: Update?) {
        if (update!!.hasMessage()) {
            val inc = update.message
            log.info("Received message from ${inc.from.userName} : ${inc.text}")
            start(inc.text, inc.from, inc.chatId.toString())
        }
    }

    data class Command(val command: String, val data: String)

    fun parse(text: String): Command {
        return Command(text.substringBefore(" "), text.substringAfter(" "))
    }

    fun start(text: String, user: User, chatId: String) {
        val parsed = parse(text)

        if (parsed.command.toUpperCase() == "/REGISTER") {
            val dataElements = parsed.data.split(Messages.regex)
            val charList = registerer.startRegistration(user, dataElements[0].toInt(), dataElements[1])
            if (charList == null) {
                send(chatId, Messages.Registration.BAD_AUTH)
            } else {
                send(chatId, Messages.Registration.SELECT_CHAR, charList)
            }
            return
        }

        if (registerer.isInProcess(user.id)) {
            if (parsed.command.length == 2 && parsed.command[0].equals('/')) {
                val char = registerer.finishRegistration(user.id, parsed.command.substring(1, 2).toInt())
                send(chatId, "${Messages.Registration.SUCCESS} $char")
            } else {
                send(chatId, Messages.Registration.SELECT_CHAR, registerer.getListOfCharacterCandidates(user.id))
            }
            return
        } else {
            val pilot = userService.getCharacterName(user.id)
            if (pilot == null) {
                sendMessage(MessageBuilder.build(user.id.toString(), Messages.REGISTER_MESSAGE))
                return
            }
        }

        chatty(parsed, chatId)
    }

    fun chatty(parsed: Command, chatId: String) {
        val text = when(parsed.command.toUpperCase()) {
            "/JOKE" -> "oh fuck you, bro!"
            "/USERS" -> userService.getCharacters().joinToString("\n")
            else -> "Ok, so?"
        }
        sendMessage(MessageBuilder.build(chatId, text))
    }

    fun broadcast(text: String) {
        val messages = broadcastComposer.compose(text)
        messages.forEach { m -> sendMessage(m) }
        log.info("broadcast sent to ${messages.size} users")
    }

    private fun send(chatId: String, text: String) {
        sendMessage(MessageBuilder.build(chatId, text))
    }

    private fun send(chatId: String, text: String, options: List<String>) {
        val kb = MessageBuilder.createKeyboard(options)
        sendMessage(MessageBuilder.build(chatId, text, kb))
    }

    companion object {
        val log = LoggerFactory.getLogger(SimpleTelegramBot::class.java)
    }
}