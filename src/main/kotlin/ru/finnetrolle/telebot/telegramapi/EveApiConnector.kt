package ru.finnetrolle.telebot.telegramapi

import com.beimin.eveapi.parser.ApiAuthorization
import com.beimin.eveapi.parser.account.CharactersParser
import com.beimin.eveapi.parser.eve.CharacterInfoParser
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
* Licence: MIT
* Legion of xXDEATHXx notification bot for telegram
* Created by finnetrolle on 12.03.16.
*/

@Component
open class EveApiConnector {

    data class Character(val name: String, val id: Long, val allyId: Long)

    fun getCharacters(key: Int, code: String): List<Character>? {
        try {
            return CharactersParser().getResponse(ApiAuthorization(key, code)).all
                .map { c -> Character(c.name, c.characterID, getCorpId(c.characterID)) }
                .toList()
        } catch (e: Exception) {
            log.warn("Get characters failed for key=$key", e)
        }
        return null
    }

    fun getCorpId(charId: Long): Long {
        try {
            val response = CharacterInfoParser().getResponse(charId)
            return response.allianceID
        } catch (e: Exception) {
            log.warn("Get ally id failed for character id=$charId", e)
        }
        return 0
    }

    companion object {
        private val log = LoggerFactory.getLogger(EveApiConnector::class.java)
    }

}