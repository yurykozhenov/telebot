package ru.finnetrolle.telebot.service.message

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.service.mailbot.MailbotService
import ru.finnetrolle.telebot.telegramapi.AllyService
import ru.finnetrolle.telebot.telegramapi.CorpService
import ru.finnetrolle.telebot.telegramapi.UserService
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 22.03.16.
 */

@Component open class MessageProcessor
@Autowired constructor(
        val userService: UserService,
        val allyService: AllyService,
        val corpService: CorpService,
        val mailbotService: MailbotService
) {

    @Autowired private lateinit var loc: MessageLocalization

    open fun joke() = "Rent macht frei"

    private fun <T> listOrEmpty(list: List<T>, divider: String, emptyMsg: String): String {
        return if (list.isEmpty()) emptyMsg else list.joinToString(divider)
    }

    open fun listOfModerators() = listOrEmpty(userService.getModerators(), "\n", loc.getMessage("messages.empty.list"))

    open fun listOfAlliances() = listOrEmpty(allyService.getAll()
            .map { a -> "[${a.ticker}] - ${a.title}" }
            .sorted(), "\n", loc.getMessage("messages.empty.list"))

    open fun listOfCorporations() = listOrEmpty(corpService.getAll()
            .map { c -> "[${c.ticker}] - ${c.title}" }
            .sorted(), "\n", loc.getMessage("messages.empty.list"))

    open fun checkAll() = listOrEmpty(userService.check(), "\n", loc.getMessage("messages.empty.list"))

    open fun promote(data: String) = if (userService.setModerator(data, true) == null)
        loc.getMessage("messages.user.not.found") else loc.getMessage("messages.user.promoted")

    open fun demote(data: String) = if (userService.setModerator(data, false) == null)
        loc.getMessage("messages.user.not.found") else loc.getMessage("messages.user.demoted")

    open fun renegade(name: String) = if (userService.setRenegade(name, true) == null)
        loc.getMessage("messages.user.not.found") else loc.getMessage("messages.user.renegaded")

    open fun legalize(name: String) = if (userService.setRenegade(name, false) == null)
        loc.getMessage("messages.user.not.found") else loc.getMessage("messages.user.legalized")

    open fun listOfUsers() = listOrEmpty(userService.getCharacters(), "\n", loc.getMessage("messages.empty.list"))

    open fun addAlly(ticker: String) = when (allyService.addAlly(ticker)) {
        is AllyService.AddResponse.AllianceAdded -> loc.getMessage("messages.ally.added")
        is AllyService.AddResponse.AllianceIsAlreadyInList -> loc.getMessage("messages.ally.in.list")
        is AllyService.AddResponse.AllianceIsNotExist -> loc.getMessage("messages.ally.not.exist")
        else -> loc.getMessage("messages.impossible")
    }

    open fun rmAlly(ticker: String) = when (allyService.removeAlly(ticker)) {
        is AllyService.RemoveResponse.AllianceRemoved -> loc.getMessage("messages.ally.removed")
        is AllyService.RemoveResponse.AllianceNotFound -> loc.getMessage("messages.ally.not.found")
        else -> loc.getMessage("messages.impossible")
    }

    open fun addCorp(corpId: String) = when (corpService.addCorporation(corpId.toLong())) {
        is CorpService.Add.Success -> loc.getMessage("messages.corp.added")
        is CorpService.Add.AlreadyInList -> loc.getMessage("messages.corp.in.list")
        is CorpService.Add.NotExist -> loc.getMessage("messages.corp.not.exist")
        else -> loc.getMessage("messages.impossible")
    }

    open fun rmCorp(ticker: String) = when (corpService.removeCorporation(ticker)) {
        is CorpService.Remove.Success -> loc.getMessage("messages.corp.removed")
        is CorpService.Remove.NotFound -> loc.getMessage("messages.corp.not.found")
        else -> loc.getMessage("messages.impossible")
    }

    open fun lastMail() = mailbotService.getLast()
            .map { m -> "*${m.title}*\n*from: ${m.sender}*\n${m.body}\n----------" }
            .joinToString("\n\n")

    fun showGroup(groupName: String): String {
        log.debug("Trying to find some users for group $groupName")
        val prefix = loc.getMessage("messages.group.header", groupName)
        log.debug("header will be $prefix")
        return userService.showGroup(groupName)
                .joinToString(separator = "\n", prefix = prefix)
    }

    companion object {
        val log = LoggerFactory.getLogger(MessageProcessor::class.java)
    }

}