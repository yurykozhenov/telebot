package ru.finnetrolle.telebot.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
* Licence: MIT
* Legion of xXDEATHXx notification bot for telegram
* Created by finnetrolle on 13.03.16.
*/

interface PilotRepository: JpaRepository<Pilot, Int> {

    @Modifying
    @Query("update Pilot u set u.renegade = true where u.id in ?1")
    fun makeRenegades(@Param("ids") ids: Collection<Int>)

    fun findByCharacterName(name: String): Pilot?

    fun findByRenegadeFalse(): List<Pilot>

    fun findByModeratorTrue(): List<Pilot>
}

@Entity
@Table(name = "pilots")
data class Pilot (
        @Id var id: Int = 0, // telegram id is PK
        var firstName: String? = "",
        var lastName: String? = "",
        var username: String? = "",
        var apiKey: Int = 0,
        var vCode: String = "",
        var characterName: String = "",
        var characterId: Long = 0,
        var moderator: Boolean = false,
        var renegade: Boolean = true
)