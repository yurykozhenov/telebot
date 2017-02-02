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


interface CorporationRepository: Repository<Corporation, Long> {
    fun findByTicker(ticker: String): Optional<Corporation>
    fun save(corporation: Corporation): Corporation
    fun findOne(id: Long): Optional<Corporation>
    fun delete(corporation: Corporation)
    fun findAll(): List<Corporation>
}

@Entity(name = "corporations")
data class Corporation (
        @Id var id: Long = 0,
        var ticker: String = "",
        var title: String = ""
)