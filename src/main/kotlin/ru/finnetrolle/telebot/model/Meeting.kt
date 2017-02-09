package ru.finnetrolle.telebot.model

import org.springframework.data.repository.Repository
import java.util.*
import javax.persistence.*

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

interface MeetingRepository : Repository<Meeting, String> {
    fun findOne(id: String): Optional<Meeting>
    fun save(meeting: Meeting): Meeting
}

@Entity(name = "meetings")
data class Meeting (
        @Id
        @Column(name = "meeting_id") var id: String = UUID.randomUUID().toString().replace("-", ""),
        @Column(name = "from_tele_id") var from: Int = 0,
        @Column(name = "to_tele_id") var to: Int = 0,
        @Column(name = "timestamp") var date: Date = Date(),
        @Column(name = "result") var result: String = "WAIT"
)