package ru.finnetrolle.telebot.service.eveapi

import com.beimin.eveapi.model.eve.Alliance
import com.beimin.eveapi.model.eve.CorporationStat
import com.beimin.eveapi.parser.ApiAuthorization
import com.beimin.eveapi.parser.account.CharactersParser
import com.beimin.eveapi.parser.corporation.CorpSheetParser
import com.beimin.eveapi.parser.eve.AllianceListParser
import com.beimin.eveapi.parser.eve.CharacterInfoParser
import com.beimin.eveapi.parser.eve.CharacterLookupParser
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.finnetrolle.cachingcontainer.CachingContainer
import ru.finnetrolle.cachingcontainer.CachingContainer.HOURS

/**
* Licence: MIT
* Legion of xXDEATHXx notification bot for telegram
* Created by finnetrolle on 12.03.16.
*/

@Component
open class EveApiConnector {

    data class Character(val name: String, val id: Long, val allyId: Long)

    val allyList = CachingContainer.build<Set<Alliance>>(1 * HOURS)

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

    fun getAlliances() = allyList.get { -> AllianceListParser().response.all }//AllianceListParser().response.all

    fun isAllianceExist(ticker: String) = getAlliance(ticker) != null

    fun getAlliance(ticker: String): Alliance? = getAlliances().find { x -> x.shortName.equals(ticker) }

    fun getCorporation(id: Long) = CorpSheetParser().getResponse(id)

    companion object {
        private val log = LoggerFactory.getLogger(EveApiConnector::class.java)
    }

}