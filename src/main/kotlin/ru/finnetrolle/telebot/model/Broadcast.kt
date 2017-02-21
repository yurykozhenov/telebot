package ru.finnetrolle.telebot.model

import org.springframework.data.repository.Repository
import java.util.*
import javax.persistence.*

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

interface BroadcastRepository : Repository<Broadcast, Long> {
        fun findAll(): List<Broadcast>
        fun save(broadcast: Broadcast): Broadcast
}

@Entity(name="broadcasts")
data class Broadcast (
        @Id
        @GeneratedValue(strategy = javax.persistence.GenerationType.SEQUENCE, generator = "broadcasts_sequence")
        @SequenceGenerator(name = "broadcasts_sequence", sequenceName = "broadcasts_seq")
        @Column(name = "broadcast_id") var id: Long = 0,
        @Column(name = "from_name") var fromName: String = "",
        @Column(name = "timestamp") var sent: Date = Date(),
        @Column(name = "to_group") var toGroupName: String = "all",
        @Column(name = "message") var message: String = "",
        @Column(name = "receivers_count") var receiversCount: Long = 0
)