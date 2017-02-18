package ru.finnetrolle.telebot.service.telegram.processors

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.User
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.external.ExternalRegistrationService
import ru.finnetrolle.telebot.service.internal.PilotService
import ru.finnetrolle.telebot.util.MessageBuilder
import ru.finnetrolle.telebot.util.MessageLocalization
import ru.finnetrolle.telebot.util.decide

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
open class AuthPreprocessor {

    @Autowired private lateinit var externalRegistrationService: ExternalRegistrationService
    @Autowired private lateinit var pilotService: PilotService
    @Autowired private lateinit var loc: MessageLocalization



    private fun tryRegister(tryApp: ExternalRegistrationService.ApproveResult) = when (tryApp) {
        is ExternalRegistrationService.ApproveResult.Success ->
            loc.getMessage("telebot.fastreg.welcome", tryApp.name)
        is ExternalRegistrationService.ApproveResult.Forbidden ->
            loc.getMessage("telebot.fastreg.forbidden", tryApp.name)
        is ExternalRegistrationService.ApproveResult.TimedOut ->
            loc.getMessage("telebot.fastreg.expired")
        is ExternalRegistrationService.ApproveResult.NotAKey ->
            loc.getMessage("messages.please.register")
    }

    sealed class Auth {
        class Intercepted(val response: SendMessage) : Auth()
        class Authorized(val pilot: Pilot, val command: String, val data: String) : Auth()
    }

    private val log = LoggerFactory.getLogger(AuthPreprocessor::class.java)

    open fun selectResponse(text: String, user: User, chatId: String): Auth {
        log.debug("selecting response for pilot ${user} with ${user.firstName}, ${user.lastName}, ${user.userName}")
        return pilotService.getPilot(user.id).decide({
            if (it.renegade) {
                Auth.Intercepted(MessageBuilder.build(chatId, loc.getMessage("messages.renegade")))
            } else {
                if (isTelegramDataEquals(user, it)) {
                    Auth.Authorized(it, text.substringBefore(" "), text.substringAfter(" "))
                } else {
                    it.firstName = user.firstName
                    it.lastName = user.lastName
                    it.username = user.userName
                    Auth.Authorized(pilotService.update(it), text.substringBefore(" "), text.substringAfter(" "))
                }
            }
        }, {
            if (text.length == externalRegistrationService.getKeyLength()) {
                val regResult = tryRegister(externalRegistrationService.tryToApproveContender(text.toUpperCase(), user))
                Auth.Intercepted(MessageBuilder.build(chatId, regResult))
            } else {
                Auth.Intercepted(MessageBuilder.build(chatId, loc.getMessage("messages.please.register")))
            }
        })
    }

    private fun isTelegramDataEquals(user: User, pilot: Pilot): Boolean =
            user.firstName == pilot.firstName && user.lastName == pilot.lastName && user.userName == pilot.username
}