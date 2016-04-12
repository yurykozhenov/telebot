package ru.finnetrolle.telebot.model

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 12.04.16.
 */

interface MailRepository: CrudRepository<Mail, Long> {

    @Query(value = "select max(id) from mail", nativeQuery = true)
    fun getMaxId(): Long?

    fun findFirst3ByOrderByIdDesc(): List<Mail>

}

@Entity(name = "mail")
data class Mail (
        @Id var id: Long = 0,
        var sent: Date = Date(),
        var sender: String = "",
        var title: String = "",
        var body: String = ""
)
