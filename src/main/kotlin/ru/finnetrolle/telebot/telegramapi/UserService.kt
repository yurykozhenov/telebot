package ru.finnetrolle.telebot.telegramapi

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.objects.User
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.model.PilotRepository
import javax.annotation.PostConstruct

/**
* Licence: MIT
* Legion of xXDEATHXx notification bot for telegram
* Created by finnetrolle on 13.03.16.
*/

@Component
open class UserService @Autowired constructor (
        val pilotRepo: PilotRepository
) {

    @Value("\${superuser}")
    private lateinit var superUser: String

    private val DUMMY_PILOT = Pilot()

    @PostConstruct
    fun init() {
        log.info("System bot super user is $superUser")
    }

    fun isModerator(teleId: Int):Boolean {
        val pilot = pilotRepo.findOne(teleId)?:DUMMY_PILOT
        if (pilot.characterName.equals(superUser))
            return true
        else
            return pilot.moderator && !pilot.renegade
    }

    fun register(user: User, key: Int, code: String, character: String, characterId: Long) {
        log.info("registering new pilot: " + character)
        val pilot = Pilot(user.id, user.firstName, user.lastName, user.userName, key, code, character, characterId)
        val saved = pilotRepo.save(pilot);
        log.info("saved pilot is $saved");
    }

    fun getCharacterName(id: Int): String? {
        val pilot = pilotRepo.findOne(id)
        if (pilot != null)
            return pilot.characterName
        else
            return null
    }

    fun getCharacters(): List<String> = pilotRepo.findAll().map { p -> p.characterName }.toList()

    companion object {
        val log = LoggerFactory.getLogger(UserService::class.java)
    }

}