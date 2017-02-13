package ru.finnetrolle.telebot.service.internal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.finnetrolle.telebot.model.Alliance
import ru.finnetrolle.telebot.model.AllianceRepository
import ru.finnetrolle.telebot.service.external.EveApiConnector
import ru.finnetrolle.telebot.util.decide

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 19.03.16.
 */

@Component
open class AllyService {

    @Autowired
    private lateinit var repo: AllianceRepository

    @Autowired
    private lateinit var eve: EveApiConnector

    sealed class Add {
        class Success(val alliance: Alliance) : Add()
        class AlreadyExists(val alliance: Alliance) : Add()
        class NotFound(val ticker: String) : Add()
    }

    @Transactional
    open fun add(ticker: String): Add {
        val exists = repo.findByTicker(ticker)
        if (exists.isPresent) {
            return Add.AlreadyExists(exists.get())
        }

        val ally = eve.getAlliance(ticker)
        return if (ally == null) {
            Add.NotFound(ticker)
        } else {
            Add.Success(repo.save(Alliance(ally.allianceID, ally.shortName, ally.name)))
        }
    }

    sealed class Remove {
        class Success(val alliance: Alliance) : Remove()
        class NotFound(val ticker: String) : Remove()
    }

    @Transactional
    open fun remove(ticker: String): Remove {
        return repo.findByTicker(ticker).decide({
            repo.delete(it)
            Remove.Success(it)
        },{
            Remove.NotFound(ticker)
        })
    }

    open fun get(allyId: Long) = repo.findOne(allyId)

    open fun getAll() = repo.findAll()

}
