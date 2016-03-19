package ru.finnetrolle.telebot.service.telegram

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.api.objects.User
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import ru.finnetrolle.telebot.service.eveapi.EveApiConnector
import ru.finnetrolle.telebot.telegramapi.AllyService.AddResponse.AllianceAdded
import ru.finnetrolle.telebot.telegramapi.AllyService.AddResponse.AllianceIsAlreadyInList
import ru.finnetrolle.telebot.telegramapi.AllyService.AddResponse.AllianceIsNotExist
import ru.finnetrolle.telebot.telegramapi.AllyService.RemoveResponse.AllianceNotFound
import ru.finnetrolle.telebot.telegramapi.AllyService.RemoveResponse.AllianceRemoved
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
        val eve: EveApiConnector,
        val userService: UserService,
        val allyService: AllyService,
        val corpService: CorpService
): TelegramLongPollingBot() {

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

        if (userService.isModerator(user.id) && processAuth(parsed, chatId)) {
            return
        }
        processUnauth(parsed, chatId)
    }

    fun processAuth(parsed: Command, chatId: String): Boolean {
        val text = when(parsed.command.toUpperCase()) {
            "/USERS" -> userService.getCharacters()
                    .joinToString("\n")
            "/ADDALLY" -> when (allyService.addAlly(parsed.data)) {
                is AllianceAdded -> Messages.Ally.ADDED
                is AllianceIsAlreadyInList -> Messages.Ally.IN_LIST
                is AllianceIsNotExist -> Messages.Ally.NOT_EXIST
                else -> Messages.IMPOSSIBLE
            }
            "/RMALLY" -> when (allyService.removeAlly(parsed.data)) {
                is AllianceRemoved -> Messages.Ally.REMOVED
                is AllianceNotFound -> Messages.Ally.NOT_FOUND
                else -> Messages.IMPOSSIBLE
            }
            "/ADDCORP" -> when (corpService.addCorporation(parsed.data.toLong())) {
                is CorpService.Add.Success -> Messages.Corp.ADDED
                is CorpService.Add.AlreadyInList -> Messages.Corp.IN_LIST
                is CorpService.Add.NotExist -> Messages.Corp.NOT_EXIST
                else -> Messages.IMPOSSIBLE
            }
            "/RMCORP" -> when (corpService.removeCorporation(parsed.data)) {
                is CorpService.Remove.Success -> Messages.Corp.REMOVED
                is CorpService.Remove.NotFound -> Messages.Corp.NOT_FOUND
                else -> Messages.IMPOSSIBLE
            }
            "/CHECK" -> userService.check().joinToString("\n")
            "/PRO" -> if (userService.setModerator(parsed.data, true) == null)
                Messages.User.NOT_FOUND else Messages.User.PROMOTED
            "/DEM" -> if (userService.setModerator(parsed.data, false) == null)
                Messages.User.NOT_FOUND else Messages.User.DEMOTED
            "/RENEGADE" -> if (userService.setRenegade(parsed.data, true) == null)
                Messages.User.NOT_FOUND else Messages.User.RENEGADED
            "/LEGALIZE" -> if (userService.setRenegade(parsed.data, false) == null)
                Messages.User.NOT_FOUND else Messages.User.LEGALIZED
            "/CAST" -> broadcast(parsed.data)
            else -> return false
        }
        sendMessage(MessageBuilder.build(chatId, text))
        return true
    }

    fun processUnauth(parsed: Command, chatId: String): Boolean {
        val text = when(parsed.command.toUpperCase()) {
            "/JOKE" -> "oh fuck you, bro!"
            "/LA" -> allyService.getAll()
                    .map { a -> "[${a.ticker}] - ${a.title}" }
                    .sorted()
                    .joinToString("\n")
            "/LC" -> corpService.getAll()
                    .map { c -> "[${c.ticker}] - ${c.title}" }
                    .sorted()
                    .joinToString("\n")
            "/LM" -> userService.getModerators().joinToString("\n")
            else -> return false
        }
        sendMessage(MessageBuilder.build(chatId, text))
        return true
    }

    fun broadcast(text: String): String {
        if (checkBeforeSend) {
            userService.check()
        }
        val messages = broadcastComposer.compose(text)
        messages.forEach { m -> sendMessage(m) }
        val s = "broadcast sent to ${messages.size} users"
        log.info(s)
        return s
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