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

    object Reg {
        val SELECT_CHAR: String = "Я нашел на указанном аккаунте следующих пилотов: выберите того, кого хотите привязать"
        val BAD_AUTH: String = "Введены некорректные keyID + vCode или у сервера какие-то проблемы с авторизацией"
        val SUCCESS_ALLY  = " успешно зарегистрирован как пилот альянса "
        val SUCCESS_CORP  = " успешно зарегистрирован как пилот корпорации "
        val SUCCESS_FIRST = " успешно зарегистрирован"
        val FAIL_EXPIRED = "Время регистрации истекло, попробуйте начать регистрацию заново с помощью команды /register"
        val FAIL_ID = "Ошибка выбора персонажа"
        val FAIL_DENIED = " запрещено регистрироваться в системе"
    }

    object User {
        val NOT_FOUND = "Пилот с таким именем не найден"
        val PROMOTED = "Пилот повышен до модератора"
        val DEMOTED = "Пилот не является модератором"
        val RENEGADED = "Пилот теперь считается отступником"
        val LEGALIZED = "Пилот более не считается отступником"
    }

}