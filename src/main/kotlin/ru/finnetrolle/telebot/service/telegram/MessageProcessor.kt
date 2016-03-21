package ru.finnetrolle.telebot.service.telegram

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.telegramapi.AllyService
import ru.finnetrolle.telebot.telegramapi.CorpService
import ru.finnetrolle.telebot.telegramapi.UserService

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 22.03.16.
 */

@Component open class MessageProcessor
@Autowired constructor(
        val userService: UserService,
        val allyService: AllyService,
        val corpService: CorpService
) {

    open fun joke() = "oh fuck you, bro!"

    open fun listOfModerators() = userService.getModerators().joinToString("\n")

    open fun listOfAlliances() = allyService.getAll()
            .map { a -> "[${a.ticker}] - ${a.title}" }
            .sorted()
            .joinToString("\n")

    open fun listOfCorporations() = corpService.getAll()
            .map { c -> "[${c.ticker}] - ${c.title}" }
            .sorted()
            .joinToString("\n")

    open fun checkAll() = userService.check().joinToString("\n")

    open fun promote(data: String) = if (userService.setModerator(data, true) == null)
        Messages.User.NOT_FOUND else Messages.User.PROMOTED

    open fun demote(data: String) = if (userService.setModerator(data, false) == null)
        Messages.User.NOT_FOUND else Messages.User.DEMOTED

    open fun renegade(name: String) = if (userService.setRenegade(name, true) == null)
        Messages.User.NOT_FOUND else Messages.User.RENEGADED

    open fun legalize(name: String) = if (userService.setRenegade(name, false) == null)
        Messages.User.NOT_FOUND else Messages.User.LEGALIZED

    open fun listOfUsers(): String {
        return userService.getCharacters()
                .joinToString("\n")
    }

    open fun addAlly(ticker: String) = when (allyService.addAlly(ticker)) {
        is AllyService.AddResponse.AllianceAdded -> Messages.Ally.ADDED
        is AllyService.AddResponse.AllianceIsAlreadyInList -> Messages.Ally.IN_LIST
        is AllyService.AddResponse.AllianceIsNotExist -> Messages.Ally.NOT_EXIST
        else -> Messages.IMPOSSIBLE
    }

    open fun rmAlly(ticker: String) = when (allyService.removeAlly(ticker)) {
        is AllyService.RemoveResponse.AllianceRemoved -> Messages.Ally.REMOVED
        is AllyService.RemoveResponse.AllianceNotFound -> Messages.Ally.NOT_FOUND
        else -> Messages.IMPOSSIBLE
    }

    open fun addCorp(corpId: String) = when (corpService.addCorporation(corpId.toLong())) {
        is CorpService.Add.Success -> Messages.Corp.ADDED
        is CorpService.Add.AlreadyInList -> Messages.Corp.IN_LIST
        is CorpService.Add.NotExist -> Messages.Corp.NOT_EXIST
        else -> Messages.IMPOSSIBLE
    }

    open fun rmCorp(ticker: String) = when (corpService.removeCorporation(ticker)) {
        is CorpService.Remove.Success -> Messages.Corp.REMOVED
        is CorpService.Remove.NotFound -> Messages.Corp.NOT_FOUND
        else -> Messages.IMPOSSIBLE
    }
    
}