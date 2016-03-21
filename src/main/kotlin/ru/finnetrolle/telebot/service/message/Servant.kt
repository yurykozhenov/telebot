package ru.finnetrolle.telebot.service.message

import org.telegram.telegrambots.api.methods.SendMessage

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 21.03.16.
 */

abstract class Servant() {

    abstract fun getWord(): String

    abstract fun getServant(): (ServantManager.Command) -> List<SendMessage>

    abstract fun getWithData(): Boolean
}
