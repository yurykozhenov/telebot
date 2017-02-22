package ru.finnetrolle.telebot.model

import org.springframework.data.repository.Repository
import java.util.*
import javax.persistence.*

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

interface QuestOptionRepository : Repository<QuestOption, String> {
        fun save(data: QuestOption): QuestOption
        fun findOne(id: String): Optional<QuestOption>
}

@Entity
@Table(name = "quest_options")
data class QuestOption(
        @Id
        @Column(name = "quest_option_id")
        var id: String = UUID.randomUUID().toString(),

        @Column(name = "text")
        var text: String = "",

        @ManyToOne(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
        @JoinColumn(name = "quest_id")
        var quest: Quest = Quest(),

        @ManyToMany(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
        @JoinTable(name = "pilot_options",
                joinColumns = arrayOf(JoinColumn(name = "quest_option_id", nullable = false, updatable = false)),
                inverseJoinColumns = arrayOf(JoinColumn(name = "id", nullable = false, updatable = false)))
        var voters: MutableSet<Pilot> = mutableSetOf()
)