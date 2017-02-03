package ru.finnetrolle.telebot.service.processing.commands.secured

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.external.TranslateService
import ru.finnetrolle.telebot.service.external.YandexTranslate
import ru.finnetrolle.telebot.service.internal.PilotService
import ru.finnetrolle.telebot.service.telegram.TelegramBotService
import ru.finnetrolle.telebot.util.MessageBuilder
import ru.finnetrolle.telebot.util.MessageLocalization
import java.util.*

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
@Component
open class GlobalBroadcasterCommand : AbstractSecuredCommand() {

    @Autowired
    private lateinit var pilotService: PilotService

    @Autowired
    private lateinit var telegram: TelegramBotService

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var translator: TranslateService

    private val log = LoggerFactory.getLogger(GlobalBroadcasterCommand::class.java)

    override fun name() = "/CAST"

    override fun description() = loc.getMessage("telebot.command.description.cast")

    override fun execute(pilot: Pilot, data: String): String {
        try {
            val message = "Broadcast from ${pilot.characterName} at ${Date()} \n$data"
            val users = pilotService.getLegalUsers()
            val languages = users
                    .filter { it.translateTo.isNotEmpty() }
                    .map { it.translateTo }
                    .distinct()
                    .map { Pair(it, translator.translate(message, it)) }
                    .toMap()
            telegram.broadcast(users
                    .map { u -> MessageBuilder.build(u.id.toString(), prepareMessage(u, languages, message)) }
            )
            return loc.getMessage("messages.broadcast.result", users.size)
        } catch (e: Exception) {
            log.error("Can't execute command global broadcast because of", e)
        }
        return "Some very bad happened"
    }

    fun prepareMessage(to: Pilot, languages: Map<String, String>, text: String) : String {
        if (to.translateTo.isNotEmpty()) {
            return "$text\n\n<=>\n\n${languages[to.translateTo!!]}"
        }
        return text
    }
}