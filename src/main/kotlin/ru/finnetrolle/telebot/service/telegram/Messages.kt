package ru.finnetrolle.telebot.service.telegram

/**
* Licence: MIT
* Legion of xXDEATHXx notification bot for telegram
* Created by finnetrolle on 13.03.16.
*/
object Messages {

    val REGISTER_MESSAGE = "Вы еще не зарегистрированы. Для регистрации необходимо использовать" +
            "ваш keyID и vCode. Отправьте мне сообщение /register [keyID] [verification code]"
    val regex = Regex("[\\W]")

    val UNKNOWN = "Команда не распознана"

    val IMPOSSIBLE = "Произошла невозможная ошибка в логике"

    object Ally {
        val ADDED = "Альянс добавлен"
        val IN_LIST = "Альянс был добавлен ранее"
        val NOT_EXIST = "Альянса с таким тикером не существует"
        val REMOVED = "Альянс удален"
        val NOT_FOUND = "Альянс с таким тикером не найден в базе"
    }

    object Corp {
        val ADDED = "Корпорация добавлена"
        val IN_LIST = "Корпорация уже есть в списке"
        val NOT_EXIST = "Корпорации с таким id не существует. Уточните id с помощью http://evemaps.dotlan.net"
        val REMOVED = "Корпорация удалена"
        val NOT_FOUND = "Корпорация с таким тикером не найдена в базе"
    }

    object Registration {
        val SELECT_CHAR: String = "Я нашел на указанном аккаунте следующих пилотов: выберите того, кого хотите привязать"
        val BAD_AUTH: String = "Введены некорректные keyID + vCode или у сервера какие-то проблемы с авторизацией"
        val SUCCESS: String = "Успешно зарегистрирован под именем "
    }

}