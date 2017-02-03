package ru.finnetrolle.telebot.service.external

import com.beimin.eveapi.model.eve.Alliance
import com.beimin.eveapi.model.eve.CharacterAffiliation
import com.beimin.eveapi.parser.ApiAuthorization
import com.beimin.eveapi.parser.account.CharactersParser
import com.beimin.eveapi.parser.character.MailBodiesParser
import com.beimin.eveapi.parser.character.MailMessagesParser
import com.beimin.eveapi.parser.corporation.CorpSheetParser
import com.beimin.eveapi.parser.eve.AllianceListParser
import com.beimin.eveapi.parser.eve.CharacterAffiliationParser
import com.beimin.eveapi.parser.eve.CharacterInfoParser
import com.beimin.eveapi.response.corporation.CorpSheetResponse
import com.beimin.eveapi.response.eve.CharacterInfoResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.finnetrolle.cachingcontainer.CachingContainer
import ru.finnetrolle.cachingcontainer.CachingContainer.HOURS
import ru.finnetrolle.telebot.util.getPage
import ru.finnetrolle.telebot.util.getPagesCount

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
            val chars = CharactersParser().getResponse(ApiAuthorization(key, code)).all
                    .map { c -> Character(c.name, c.characterID, getCorpId(c.characterID)) }
            return if (chars.isEmpty()) null else chars
        } catch (e: Exception) {
            log.warn("Get characters failed for key=$key", e)
            return null;
        }
    }

    fun getCharacter(id: Long):CharacterInfoResponse {
        try {
            return CharacterInfoParser().getResponse(id)
        } catch (e: Exception) {
            log.error("bad results when asking $id")
            throw e
        }
    }

    fun getAffilations(ids: List<Long>): Map<Long, CharacterAffiliation>  {
        return (0..ids.getPagesCount(100) - 1)
                .map { ids.getPage(it, 100) }
                .flatMap { CharacterAffiliationParser().getResponse(*it.toLongArray()).all }
                .map { Pair(it.characterID, it) }
                .toMap()
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

    fun getAlliances() = allyList[{ -> AllianceListParser().response.all.toSet() }]//AllianceListParser().response.all

    fun isAllianceExist(ticker: String) = getAlliance(ticker) != null

    fun getAlliance(ticker: String): Alliance? = getAlliances().find { x -> x.shortName.equals(ticker) }

    fun getCorporation(id: Long): CorpSheetResponse? {
        val corp = CorpSheetParser().getResponse(id)
        return if (corp.corporationID == 0L) null else corp
    }

    fun getMailList(apiKey: Int, vCode: String, mailingLists: String) =

            MailMessagesParser().getResponse(ApiAuthorization(apiKey, vCode)).all
                    .filter { x -> x.toListIDs.equals(mailingLists) }

    fun getMailBody(apiKey: Int, vCode: String, mailId: Long) = MailBodiesParser()
            .getResponse(ApiAuthorization(apiKey, vCode), mailId).all.find { x -> x != null }!!.body

    companion object {
        private val log = LoggerFactory.getLogger(EveApiConnector::class.java)
    }

}