package ru.finnetrolle.telebot.service.telegram

import java.util.*


/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 21.03.16.
 */

abstract class ServantManager {

    data class Command(val cmd: String, val data: String?, val telegramUserId: Int?)
    data class CommandIdentifier(val word: String, val data: Boolean, val secure: Boolean)
    data class Servantee(val word: String, val data: Boolean, val secure: Boolean, val servant: (Command) -> String)

    private val servants = HashMap<CommandIdentifier, (Command) -> String>()

    abstract fun getDefaultServant(): (Command) -> String

    abstract fun getAccessDeniedServant(): (Command) -> String

    abstract fun getAccessChecker(): (Command) -> Boolean

    abstract fun configure()

    fun registerServant(commandIdentifier: CommandIdentifier, servant: (Command) -> String) {
        servants.put(commandIdentifier, servant)
    }

    fun registerServant(word: String, data: Boolean, secure: Boolean, servant: (Command) -> String) {
        registerServant(CommandIdentifier(word, data, secure), servant)
    }

    fun registerServant(vararg servants: Servant) {
        servants.forEach { servant ->
            registerServant(servant.getWord(), servant.getWithData(), servant.getSecured(), servant.getServant()) }
    }

    fun registerServant(vararg servants: Servantee) {
        servants.forEach { servant ->
            registerServant(servant.word, servant.data, servant.secure, servant.servant)
        }
    }

    fun serve(command: Command): String {
        val ci = CommandIdentifier(
                command.cmd,
                command.data != null,
                command.telegramUserId != null)
        val servant = servants[ci]
        return if (servant == null) {
            getDefaultServant().invoke(command)
        } else {
            if (ci.secure) {
                if (getAccessChecker().invoke(command)) {
                    servant.invoke(command)
                } else {
                    getAccessDeniedServant().invoke(command)
                }
            } else {
                servant.invoke(command)
            }
        }
    }

}