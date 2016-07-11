package ru.finnetrolle.telebot.service.telegram

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.api.objects.User
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import ru.finnetrolle.telebot.service.external.ExternalGroupProvider
import ru.finnetrolle.telebot.service.message.*
import ru.finnetrolle.telebot.telegramapi.*
import kotlin.collections.List
import ru.finnetrolle.telebot.telegramapi.ExternalRegistrationService.*
import ru.finnetrolle.telebot.util.MessageLocalization

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
        val manager: TelebotServantManager,
        val externalRegistrationService: ExternalRegistrationService,
        val kupuyc: ExternalGroupProvider
): TelegramLongPollingBot() {

    @Autowired private lateinit var loc: MessageLocalization

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

    fun tryAcceptExternalRegistration(text: String, user: User, chatId: String): Boolean {
        if (text.length == 6) {
            val tryApp = externalRegistrationService.tryToApproveContender(text.toUpperCase(), user)
            when (tryApp) {
                is ApproveResult.Success -> {
                    send(chatId, "Welcome, ${tryApp.name}!")
                    return true
                }
                is ApproveResult.Forbidden -> {
                    send(chatId, "Sir ${tryApp.name}, you can't join us because of security settings.")
                    return true
                }
                is ApproveResult.TimedOut -> {
                    send(chatId, "Dear user, you must enter code into telegram within 20 mins after registration. Try again.")
                    return true
                }
            }
        }
        return false
    }

    fun start(text: String, user: User, chatId: String) {
        if (tryAcceptExternalRegistration(text, user, chatId)) {
            return
        }

        val parsed = parse(text)

        if (parsed.command.toUpperCase() == "/REGISTER") {
            val dataElements = parsed.data.split(Regex("[\\W]"))
            val charList = registerer.startRegistration(user, dataElements[0].toInt(), dataElements[1])
            if (charList == null) {
                send(chatId, loc.getMessage("messages.reg.bad.auth"))
            } else {
                send(chatId, loc.getMessage("messages.reg.select.char"), charList)
            }
            return
        }

        if (registerer.isInProcess(user.id)) {
            if (parsed.command.length == 2 && parsed.command[0].equals('/')) {
                val finish = registerer.finishRegistration(user.id, parsed.command.substring(1, 2).toInt())
                when (finish) {
                    is RegistererService.Finish.FailByNotAllowed ->
                            send(chatId, loc.getMessage("messages.reg.fail.denied", finish.name))
                    is RegistererService.Finish.FailByRegistrationExpired ->
                            send(chatId, loc.getMessage("messages.reg.fail.expired"))
                    is RegistererService.Finish.FailByWrongSelect ->
                            send(chatId, loc.getMessage("messages.reg.fail.id"))
                    is RegistererService.Finish.SuccessByAlliance ->
                            send(chatId, loc.getMessage("messages.reg.success.ally", finish.name, finish.alliance))
                    is RegistererService.Finish.SuccessByCorporation ->
                            send(chatId, loc.getMessage("messages.reg.success.corp", finish.name, finish.corp))
                    is RegistererService.Finish.SuccessByNoLists ->
                            send(chatId, loc.getMessage("messages.reg.success.first", finish.name))
                    else -> send(chatId, loc.getMessage("messages.impossible"))
                }
            } else {
                send(chatId, loc.getMessage("messages.reg.select.char"), registerer.getListOfCharacterCandidates(user.id))
            }
            return
        } else {
            val pilot = userService.getCharacterName(user.id)
            if (pilot == null) {
                sendMessage(MessageBuilder.build(user.id.toString(), loc.getMessage("messages.register")))
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

    fun groupBroadcast(group: String, text: String): Int {
        if (checkBeforeSend) {
            userService.check()
        }
        val members: Set<String> = kupuyc.getMembers(group)
        val messages = broadcastComposer.compose(text, members)
        messages.forEach { m -> broadcaster.enqueue(m) }
        val s = "broadcast sent to ${messages.size} users"
        log.info(s)
        return messages.size
    }
}

