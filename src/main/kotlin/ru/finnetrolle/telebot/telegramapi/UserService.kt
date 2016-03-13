package ru.finnetrolle.telebot.telegramapi

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.telegram.telegrambots.api.objects.User
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.model.PilotRepository

/**
 * Created by maxsyachin on 13.03.16.
 */

@Component
open class UserService @Autowired constructor (val pilotRepo: PilotRepository) {

    fun register(user: User, key: Int, code: String, character: String) {
        log.info("registering new pilot: " + character)
        val pilot = Pilot(user.id, user.firstName, user.lastName, user.userName, key, code, character)
        val saved = pilotRepo.save(pilot)
    }

    fun getCharacterName(id: Int): String? {
        val pilot = pilotRepo.findOne(id)
        if (pilot != null)
            return pilot.character
        else
            return null
    }

    fun getCharacters(): List<String> = pilotRepo.findAll().map { p -> p.character }.toList()

    companion object {
        val log = LoggerFactory.getLogger(UserService::class.java)
    }

}