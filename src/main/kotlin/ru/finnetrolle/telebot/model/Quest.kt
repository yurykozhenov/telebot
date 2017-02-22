package ru.finnetrolle.telebot.model

import java.util.*
import javax.persistence.*
import org.springframework.data.repository.Repository
import java.time.LocalDateTime

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

interface QuestRepository : Repository<Quest, String> {
        fun save(quest: Quest): Quest
        fun findOne(id: String): Optional<Quest>
}

@Entity
@Table(name = "quests")
data class Quest(
        @Id
        @Column(name = "quest_id")
        var id: String = UUID.randomUUID().toString(),

        @Column(name = "author")
        var author: Int = 0,

        @Column(name = "text")
        var text: String = "",

        @Column(name = "created_at")
        var created: LocalDateTime = LocalDateTime.now(),

        @Column(name = "expires_at")
        var expires: LocalDateTime = LocalDateTime.now(),

        @Column(name = "group_name")
        var groupName: String = "ALL",

        @OneToMany(fetch = FetchType.EAGER, mappedBy = "quest", cascade = arrayOf(CascadeType.ALL))
        var options: MutableList<QuestOption> = mutableListOf()
)