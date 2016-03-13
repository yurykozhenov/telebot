package ru.finnetrolle.telebot.telegramapi

import com.beimin.eveapi.parser.ApiAuthorization
import com.beimin.eveapi.parser.account.CharactersParser
import org.springframework.stereotype.Component

/**
 * Created by maxsyachin on 12.03.16.
 */

@Component
open class EveApiConnector {

    fun getCharacters(key: Int, code: String): List<String> {
        val response = CharactersParser().getResponse(ApiAuthorization(key, code))
        val chars = response.all
        chars.forEach { c -> println(c.name) }
        return chars.map { x -> x.name }
    }

}