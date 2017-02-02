package ru.finnetrolle.telebot.service.internal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.finnetrolle.telebot.model.Corporation
import ru.finnetrolle.telebot.model.CorporationRepository
import ru.finnetrolle.telebot.service.external.EveApiConnector

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 19.03.16.
 */

@Component
open class CorpService {

    @Autowired
    private lateinit var repo: CorporationRepository

    @Autowired
    private lateinit var eve: EveApiConnector

    interface Add {
        data class Success(val corporation: Corporation) : Add
        data class AlreadyInList(val corporation: Corporation) : Add
        data class NotExist(val id: Long) : Add
    }

    @Transactional
    open fun add(id: Long): Add {
        repo.findOne(id).get()?.let { return Add.AlreadyInList(it) }
        eve.getCorporation(id)?.let {
            return Add.Success(repo.save(Corporation(it.corporationID, it.ticker, it.corporationName)))
        }
        return Add.NotExist(id)
    }

    interface Remove {
        data class Success(val corporation: Corporation) : Remove
        data class NotFound(val ticker: String) : Remove
    }

    @Transactional
    open fun remove(ticker: String): Remove {
        repo.findByTicker(ticker).get()?.let {
            repo.delete(it)
            return Remove.Success(it)
        }
        return Remove.NotFound(ticker)
    }

    open fun get(corpId: Long) = repo.findOne(corpId)

    open fun getAll() = repo.findAll()

}