package ru.finnetrolle.telebot.telegramapi

/**
* Licence: MIT
* Legion of xXDEATHXx notification bot for telegram
* Created by finnetrolle on 13.03.16.
*/
object Messages {

    val VERSION = "xXDEATHXx Bot v 0.0.9"

    val REGISTER_MESSAGE = "Вы еще не зарегистрированы. Для регистрации необходимо использовать" +
            "ваш keyID и vCode. Отправьте мне сообщение /register [keyID] [verification code]"
    val regex = Regex("[\\W]")

    object Registration {
        val SELECT_CHAR: String = "Я нашел на указанном аккаунте следующих пилотов: выберите того, кого хотите привязать"
        val BAD_AUTH: String = "Введены некорректные keyID + vCode или у сервера какие-то проблемы с авторизацией"
        val SUCCESS: String = "Успешно зарегистрирован под именем "
    }

}