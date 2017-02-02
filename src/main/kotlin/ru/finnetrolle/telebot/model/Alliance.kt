package ru.finnetrolle.telebot.model

import org.springframework.data.repository.Repository
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 19.03.16.
 */


interface AllianceRepository: Repository<Alliance, Long> {
    fun findByTicker(ticker: String): Optional<Alliance>
    fun save(alliance: Alliance): Alliance
    fun delete(alliance: Alliance)
    fun findOne(id: Long): Optional<Alliance>
    fun findAll(): List<Alliance>
}

@Entity(name = "alliances")
data class Alliance (
        @Id var id: Long = 0,
        var ticker: String = "",
        var title: String = ""
)
