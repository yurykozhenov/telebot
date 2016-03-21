package ru.finnetrolle.telebot.service.telegram

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.telegramapi.AllyService
import ru.finnetrolle.telebot.telegramapi.CorpService
import ru.finnetrolle.telebot.telegramapi.UserService
import javax.annotation.PostConstruct

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 21.03.16.
 */

@Component
class TelebotServantManager @Autowired constructor(
        val userService: UserService,
        val allyService: AllyService,
        val corpService: CorpService
) : ServantManager() {

    @PostConstruct
    override fun configure() {
        registerServant(
                Servantee("/ADDALLY", true, true, { c -> addAlly(c.data!!) }),
                Servantee("/RMALLY", true, true, { c -> rmAlly(c.data!!) }),
                Servantee("/ADDCORP", true, true, { c -> addCorp(c.data!!) }),
                Servantee("/RMCORP", true, true, { c -> rmCorp(c.data!!) }),
                Servantee("/CHECK", false, true, checkAll),
                Servantee("/DEM", true, true, demote),
                Servantee("/PRO", true, true, promote),
                Servantee("/RENEGADE", true, true, { c -> renegade(c.data!!)}),
                Servantee("/LEGALIZE", true, true, { c -> legalize(c.data!!)}),
                Servantee("/LU", false, true, { c -> listOfUsers() }),

                Servantee("/JOKE", false, false, { c -> joke()}),
                Servantee("/LM", false, false, { c -> listOfModerators()}),
                Servantee("/LA", false, false, { c -> listOfAlliances()}),
                Servantee("/LC", false, false, { c -> listOfCorporations()})
                )
    }

    private fun joke() = "oh fuck you, bro!"

    private fun listOfModerators() = userService.getModerators().joinToString("\n")

    private fun listOfAlliances() = allyService.getAll()
            .map { a -> "[${a.ticker}] - ${a.title}" }
            .sorted()
            .joinToString("\n")

    private fun listOfCorporations() = corpService.getAll()
            .map { c -> "[${c.ticker}] - ${c.title}" }
            .sorted()
            .joinToString("\n")

    private val checkAll: (Command) -> String = { c -> userService.check().joinToString("\n") }

    private val promote: (Command) -> String = { c -> if (userService.setModerator(c.data!!, true) == null)
        Messages.User.NOT_FOUND else Messages.User.PROMOTED }

    private val demote: (Command) -> String = {c -> if (userService.setModerator(c.data!!, false) == null)
        Messages.User.NOT_FOUND else Messages.User.DEMOTED }

    private fun renegade(name: String) = if (userService.setRenegade(name, true) == null)
        Messages.User.NOT_FOUND else Messages.User.RENEGADED

    private fun legalize(name: String) = if (userService.setRenegade(name, false) == null)
        Messages.User.NOT_FOUND else Messages.User.LEGALIZED

    private fun listOfUsers(): String {
        return userService.getCharacters()
                .joinToString("\n")
    }

    private fun addAlly(ticker: String) = when (allyService.addAlly(ticker)) {
        is AllyService.AddResponse.AllianceAdded -> Messages.Ally.ADDED
        is AllyService.AddResponse.AllianceIsAlreadyInList -> Messages.Ally.IN_LIST
        is AllyService.AddResponse.AllianceIsNotExist -> Messages.Ally.NOT_EXIST
        else -> Messages.IMPOSSIBLE
    }

    private fun rmAlly(ticker: String) = when (allyService.removeAlly(ticker)) {
        is AllyService.RemoveResponse.AllianceRemoved -> Messages.Ally.REMOVED
        is AllyService.RemoveResponse.AllianceNotFound -> Messages.Ally.NOT_FOUND
        else -> Messages.IMPOSSIBLE
    }

    private fun addCorp(corpId: String) = when (corpService.addCorporation(corpId.toLong())) {
        is CorpService.Add.Success -> Messages.Corp.ADDED
        is CorpService.Add.AlreadyInList -> Messages.Corp.IN_LIST
        is CorpService.Add.NotExist -> Messages.Corp.NOT_EXIST
        else -> Messages.IMPOSSIBLE
    }

    private fun rmCorp(ticker: String) = when (corpService.removeCorporation(ticker)) {
        is CorpService.Remove.Success -> Messages.Corp.REMOVED
        is CorpService.Remove.NotFound -> Messages.Corp.NOT_FOUND
        else -> Messages.IMPOSSIBLE
    }

    override fun getDefaultServant(): (Command) -> String = { c -> Messages.UNKNOWN }

    override fun getAccessDeniedServant(): (Command) -> String = { c -> Messages.ACCESS_DENIED}

    override fun getAccessChecker(): (Command) -> Boolean = { c -> userService.isModerator(c.telegramUserId!!)}


}