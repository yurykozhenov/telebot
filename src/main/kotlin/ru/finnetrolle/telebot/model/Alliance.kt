package ru.finnetrolle.telebot.model

import org.springframework.data.repository.CrudRepository
import javax.persistence.Entity
import javax.persistence.Id

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 19.03.16.
 */


interface AllianceRepository: CrudRepository<Alliance, Long> {
    fun findByTicker(ticker: String): Alliance?
}

@Entity(name = "alliances")
data class Alliance (
        @Id var id: Long = 0,
        var ticker: String = "",
        var title: String = ""
)
