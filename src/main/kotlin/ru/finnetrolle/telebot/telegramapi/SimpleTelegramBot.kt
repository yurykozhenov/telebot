package ru.finnetrolle.telebot.telegramapi

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.api.objects.User
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import kotlin.collections.List

/**
 * Created by maxsyachin on 12.03.16.
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

    val regex = Regex("[\\W]")

    val REGISTER_MESSAGE = "Вы еще не зарегистрированы. Для регистрации необходимо использовать" +
            "ваш apikey и vCode. Отправьте мне сообщение /register apikey vcode."

    override fun onUpdateReceived(update: Update?) {
        if (update!!.hasMessage()) {
            val inc = update!!.message
            start(inc.text, inc.from, inc.chatId.toString())
        }
    }

    data class Command(val command: String, val data: String)

    fun parse(text: String): Command {
        return Command(text.substringBefore(" "), text.substringAfter(" "))
    }

    fun start(text: String, user: User, chatId: String) {
        val parsed = parse(text)
        println("parsed command is:${parsed.command}")

        if (parsed.command.toUpperCase() == "/REGISTER") {
            println("do register!")
            val charlist = register(user, parsed.data)
            sendMessage(MessageBuilder.build(
                    chatId,
                    "Now select your character",
                    MessageBuilder.createKeyboard(charlist)
            ))
            return
        }

        if (registerer.isInProcess(user.id)) {
            if (parsed.command.length == 2 && parsed.command[0].equals('/')) {
                val char = registerer.finishRegistration(user.id, parsed.command.substring(1, 2).toInt())
                sendMessage(MessageBuilder.build(
                        chatId,
                        "Successfully registered as ${char}"))
            } else {
                sendMessage(MessageBuilder.build(
                        chatId,
                        "You must select character to finish registration",
                        MessageBuilder.createKeyboard(registerer.getListOfCharacterCandidates(user.id))))
            }
            return
        } else {
            val pilot = userService.getCharacterName(user.id)
            if (pilot == null) {
                sendMessage(MessageBuilder.build(user.id.toString(), REGISTER_MESSAGE))
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

    fun register(user: User, data: String): List<String> {
        val datas = data.split(regex)
        return registerer.startRegistration(user, datas[0].toInt(), datas[1])
    }

    fun broadcast(text: String) {
        val messages = broadcastComposer.compose(text)
        messages.forEach { m -> sendMessage(m) }
        log.info("broadcast sent to ${messages.size} users")
    }

    companion object {
        val log = LoggerFactory.getLogger(SimpleTelegramBot::class.java)
    }
}