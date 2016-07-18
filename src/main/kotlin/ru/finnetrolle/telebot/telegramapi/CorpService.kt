package ru.finnetrolle.telebot.telegramapi

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Corporation
import ru.finnetrolle.telebot.model.CorporationRepository
import ru.finnetrolle.telebot.service.eveapi.EveApiConnector

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 19.03.16.
 */

@Component open class CorpService
@Autowired constructor(
        private val repo: CorporationRepository,
        private val eve: EveApiConnector
){

    interface Add {
        data class Success(val corporation: Corporation) : Add
        data class AlreadyInList(val corporation: Corporation) : Add
        data class NotExist(val id: Long) : Add
    }

    fun addCorporation(id: Long): Add {
        val existing = repo.findOne(id)
        return if (existing == null) {
            val corp = eve.getCorporation(id)
            if (corp == null) {
                Add.NotExist(id)
            } else {
                Add.Success(repo.save(Corporation(corp.corporationID, corp.ticker, corp.corporationName)))
            }
        } else {
            Add.AlreadyInList(existing)
        }
    }

    interface Remove {
        data class Success(val corporation: Corporation) : Remove
        data class NotFound(val ticker: String?, val id: Long?) : Remove
    }

    fun removeCorporation(ticker: String): Remove {
        return rmCorporation(ticker = ticker)
    }

    private fun rmCorporation(ticker: String? = null, id: Long? = null): Remove {
        val corp = if (ticker != null) repo.findByTicker(ticker) else repo.findOne(id)
        return if (corp != null) {
            repo.delete(corp)
            Remove.Success(corp)
        } else {
            Remove.NotFound(ticker, id)
        }
    }

    fun contains(corpId: Long) = repo.exists(corpId)

    fun get(corpId: Long) = repo.findOne(corpId)

    open fun getAll() = repo.findAll()

    fun isEmpty() = repo.count() == 0L

}