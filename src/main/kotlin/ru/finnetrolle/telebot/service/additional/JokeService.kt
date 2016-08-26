package ru.finnetrolle.telebot.service.additional

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Joke
import ru.finnetrolle.telebot.model.JokeRepository
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.util.MessageLocalization
import java.util.*
import javax.annotation.PostConstruct

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin.
 */

@Component
class JokeService {

    @Autowired
    lateinit private var loc: MessageLocalization

    @Autowired
    lateinit private var repo: JokeRepository

    private var jokes: List<Joke> = listOf()

    private val rand = Random()

    fun joke(): String {
        val joke = jokes[rand.nextInt(jokes.size)]
        return loc.getMessage("telebot.joke", joke.fromName, joke.text)
    }

    @PostConstruct
    fun preload() {
        jokes = repo.findAll().toList()
    }

    fun addJoke(pilot: Pilot, text: String): Boolean {
        try {
            val joke = Joke(
                    fromName = pilot.characterName,
                    fromId = pilot.characterId,
                    text = text)
            repo.save(joke)
            preload()
        } catch (e: Exception) {
            log.error("Joke service fails cause of $e", e)
            return false
        }
        return true
    }


    companion object {
        val log = LoggerFactory.getLogger(JokeService::class.java)
    }

}