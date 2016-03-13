package ru.finnetrolle.telebot.telegramapi

/**
 * Created by maxsyachin on 13.03.16.
 */
object Messages {

    val VERSION = "xXDEATHXx Bot v 0.0.2"

    val REGISTER_MESSAGE = "Вы еще не зарегистрированы. Для регистрации необходимо использовать" +
            "ваш apikey и vCode. Отправьте мне сообщение /register apikey vcode."
    val regex = Regex("[\\W]")

    object Registration {
        val SELECT_CHAR: String = "Я нашел на указанном аккаунте следующих пилотов: выберите того, кого хотите привязать"
        val BAD_AUTH: String = "Введены некорректные apiKey + vCode или у сервера какие-то проблемы с авторизацией"
        val SUCCESS: String = "Успешно зарегистрирован под именем "
    }

}