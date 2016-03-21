package ru.finnetrolle.telebot.telegramapi

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Alliance
import ru.finnetrolle.telebot.model.AllianceRepository
import ru.finnetrolle.telebot.service.eveapi.EveApiConnector

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 19.03.16.
 */

@Component open class AllyService
@Autowired constructor(
    private val repo: AllianceRepository,
    private val eve: EveApiConnector
){

    interface AddResponse {
        data class AllianceAdded(val alliance: Alliance) : AddResponse
        data class AllianceIsAlreadyInList(val alliance: Alliance) : AddResponse
        data class AllianceIsNotExist(val ticker: String) : AddResponse
    }

    fun addAlly(ticker: String): AddResponse {
        val existing = repo.findByTicker(ticker)
        if (existing == null) {
            val ally = eve.getAlliance(ticker)
            return if (ally == null) {
                AddResponse.AllianceIsNotExist(ticker)
            } else {
                AddResponse.AllianceAdded(repo.save(Alliance(ally.allianceID, ally.shortName, ally.name)))
            }
        } else {
            return AddResponse.AllianceIsAlreadyInList(existing)
        }
    }

    interface RemoveResponse {
        data class AllianceRemoved(val alliance: Alliance): RemoveResponse
        data class AllianceNotFound(val ticker: String): RemoveResponse
    }

    fun removeAlly(ticker: String): RemoveResponse {
        val ally = repo.findByTicker(ticker);
        if (ally != null) {
            repo.delete(ally)
            return RemoveResponse.AllianceRemoved(ally)
        } else {
            return RemoveResponse.AllianceNotFound(ticker)
        }
    }

    fun contains(allyId: Long) = repo.exists(allyId)

    fun get(allyId: Long) = repo.findOne(allyId)

    open fun getAll() = repo.findAll()

    fun isEmpty() = repo.count() == 0L

}