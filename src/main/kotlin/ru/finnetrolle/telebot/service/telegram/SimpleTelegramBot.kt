package ru.finnetrolle.telebot.service.telegram

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.api.objects.User
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import ru.finnetrolle.telebot.service.message.*
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
        val userService: UserService,
        val manager: TelebotServantManager
): TelegramLongPollingBot() {

    @Autowired private lateinit var broadcaster: BroadcastService

    @Value("\${telegram.bot.token}")
    private lateinit var token: String

    @Value("\${telegram.bot.username}")
    private lateinit var username: String

    @Value("\${telebot.broadcast.check-before-send}")
    private var checkBeforeSend: Boolean = false

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
                send(chatId, Messages.Reg.BAD_AUTH)
            } else {
                send(chatId, Messages.Reg.SELECT_CHAR, charList)
            }
            return
        }

        if (registerer.isInProcess(user.id)) {
            if (parsed.command.length == 2 && parsed.command[0].equals('/')) {
                val finish = registerer.finishRegistration(user.id, parsed.command.substring(1, 2).toInt())
                when (finish) {
                    is RegistererService.Finish.FailByNotAllowed ->
                            send(chatId, finish.name + Messages.Reg.FAIL_DENIED)
                    is RegistererService.Finish.FailByRegistrationExpired ->
                            send(chatId, Messages.Reg.FAIL_EXPIRED)
                    is RegistererService.Finish.FailByWrongSelect ->
                            send(chatId, Messages.Reg.FAIL_ID)
                    is RegistererService.Finish.SuccessByAlliance ->
                            send(chatId, finish.name + Messages.Reg.SUCCESS_ALLY + finish.alliance)
                    is RegistererService.Finish.SuccessByCorporation ->
                            send(chatId, finish.name + Messages.Reg.SUCCESS_CORP + finish.corp)
                    is RegistererService.Finish.SuccessByNoLists ->
                            send(chatId, finish.name + Messages.Reg.SUCCESS_FIRST)
                    else -> send(chatId, Messages.IMPOSSIBLE)
                }
            } else {
                send(chatId, Messages.Reg.SELECT_CHAR, registerer.getListOfCharacterCandidates(user.id))
            }
            return
        } else {
            val pilot = userService.getCharacterName(user.id)
            if (pilot == null) {
                sendMessage(MessageBuilder.build(user.id.toString(), Messages.REGISTER_MESSAGE))
                return
            }
        }

        val response = manager.serve(ServantManager.Command(parsed.command, parsed.data, user.id, chatId))

        response.forEach { r -> sendMessage(r) }
        val s = "Broadcast sent to ${response.size} users: ${response.map { r -> r.chatId }.joinToString(", ")}"
        log.info(s)
        println(s)

    }

    fun broadcast(text: String): String {
        if (checkBeforeSend) {
            userService.check()
        }
        val messages = broadcastComposer.compose(text)
        messages.forEach { m -> broadcaster.enqueue(m) }
        val s = "broadcast sent to ${messages.size} users"
        log.info(s)
        return s
    }

    private fun send(chatId: String, text: String) {
        broadcaster.enqueue(MessageBuilder.build(chatId, text))
    }

    private fun send(chatId: String, text: String, options: List<String>) {
        val kb = MessageBuilder.createKeyboard(options)
        broadcaster.enqueue(MessageBuilder.build(chatId, text, kb))
    }

    companion object {
        val log = LoggerFactory.getLogger(SimpleTelegramBot::class.java)
    }
}