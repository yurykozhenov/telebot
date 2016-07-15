package ru.finnetrolle.telebot.model

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.*

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 15.07.16.
 */

interface JokeRepository: CrudRepository<Joke, Long> {
}

@Entity(name = "jokes")
data class Joke (
        @Id
        @GeneratedValue(strategy = javax.persistence.GenerationType.SEQUENCE, generator = "jokes_sequence")
        @SequenceGenerator(name = "jokes_sequence", sequenceName = "jokes_seq")
        @Column(name = "joke_id") var id: Long = 0,
        @Column(name = "from_name") var fromName: String = "",
        @Column(name = "timestamp") var sent: Date = Date(),
        @Column(name = "from_id") var fromId: Long = 0,
        @Column(name = "joke_text") var text: String = ""
)