package ru.finnetrolle.telebot.service.processing

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.api.methods.SendMessage
import java.util.*


/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 21.03.16.
 */

abstract class ServantManager {

    data class Command(val cmd: String, val data: String, val telegramUserId: Int, val fromChatId: String)
    data class Servantee(
            val word: String,
            val servant: (Command) -> List<SendMessage>,
            val secure: Boolean = false,
            val description: String = "")

    private val servants = HashMap<String, Servantee>()

    abstract protected fun getDefaultServant(): (Command) -> List<SendMessage>

    abstract protected fun getAccessDeniedServant(): (Command) -> List<SendMessage>

    abstract protected fun getAccessChecker(): (Command) -> Boolean

    abstract fun configure()

    fun registerServant(vararg servants: Servant) {
        servants.forEach { servant ->
            registerServant(Servantee(servant.getWord(), servant.getServant(), servant.getWithData())) }
    }

    fun registerServant(vararg servants: Servantee) {
        servants.forEach { servant ->
            this.servants.put(servant.word, servant)
        }
    }

    fun serve(command: Command): List<SendMessage> {
        val servant = servants[command.cmd.toUpperCase()]
        return if (servant == null) {
            log.debug("can't find command ${command.cmd.toUpperCase()}")
            getDefaultServant().invoke(command)
        } else {
            log.debug("Found command: ${command.cmd}")
            if (servant.secure) {
                if (getAccessChecker().invoke(command)) {
                    log.debug("Invoking command $command")
                    servant.servant.invoke(command)
                } else {
                    log.debug("Command is forbidden for current user")
                    getAccessDeniedServant().invoke(command)
                }
            } else {
                log.debug("Invoking command $command")
                servant.servant.invoke(command)
            }
        }
    }

    fun help(forModerator: Boolean) = servants
                .filter { v -> forModerator || !v.value.secure }
                .map { v -> "${v.key} ${v.value.description}" }
                .joinToString("\n")

    companion object {
        val log = LoggerFactory.getLogger(ServantManager::class.java)
    }

}