package ru.finnetrolle.telebot.telegramapi

import com.beimin.eveapi.response.eve.CharacterInfoResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.telegram.telegrambots.api.objects.User
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.model.PilotRepository
import ru.finnetrolle.telebot.service.eveapi.EveApiConnector
import javax.annotation.PostConstruct

/**
* Licence: MIT
* Legion of xXDEATHXx notification bot for telegram
* Created by finnetrolle on 13.03.16.
*/

@Component

open class UserService @Autowired constructor (
        val pilotRepo: PilotRepository,
        val allyService: AllyService,
        val corpService: CorpService,
        val eve: EveApiConnector
) {

    @Value("\${telebot.superuser}")
    private lateinit var superUser: String

    private val DUMMY_PILOT = Pilot()

    @PostConstruct
    open fun init() {
        log.info("System bot super user is $superUser")
    }

    open fun isSuperUser(pilot: Pilot) = pilot.characterName.equals(superUser)

    open fun isModerator(teleId: Int):Boolean {
        val pilot = pilotRepo.findOne(teleId)?:DUMMY_PILOT
        return if (isSuperUser(pilot)) true else pilot.moderator && !pilot.renegade
    }

    open fun register(user: User, key: Int, code: String, character: String, characterId: Long) {
        log.info("registering new pilot: " + character)
        val pilot = Pilot(user.id, user.firstName, user.lastName, user.userName, key, code, character, characterId)
        val saved = pilotRepo.save(pilot);
        log.info("saved pilot is $saved");
    }

    open fun getLegalUsers(): List<Pilot> = pilotRepo.findByRenegadeFalse()

    open fun getCharacterName(id: Int): String? {
        val pilot = pilotRepo.findOne(id)
        if (pilot != null)
            return pilot.characterName
        else
            return null
    }

    data class CheckPair(val pilot: Pilot, val character: CharacterInfoResponse)

    @Transactional
    open fun check(): List<String> {
        val allowedAllies = allyService.getAll().map { a -> a.id }.toSet()
        val allowedCorps = corpService.getAll().map { c -> c.id }.toSet()
        val renes = pilotRepo.findByRenegadeFalse()
                .filter { p -> !isSuperUser(p) }
                .map { p -> CheckPair(p, eve.getCharacter(p.characterId)) }
                .filter { s -> !allowedAllies.contains(s.character.allianceID) }
                .filter { s -> !allowedCorps.contains(s.character.corporationID) }
        pilotRepo.makeRenegades(renes.map { r -> r.pilot.id })
        return renes.map { r -> r.pilot.characterName }
    }

    @Transactional
    open fun setModerator(name: String, value: Boolean): Pilot? {
        val user = pilotRepo.findByCharacterName(name)
        if (user != null) {
            user.moderator = value
            pilotRepo.save(user)
            return user
        } else {
            return null
        }
    }

    @Transactional
    open fun setRenegade(name: String, value: Boolean): Pilot? {
        val user = pilotRepo.findByCharacterName(name)
        if (user != null) {
            user.renegade = value
            pilotRepo.save(user)
            return user
        } else {
            return null
        }
    }

    open fun getModerators(): List<String> = pilotRepo.findByModeratorTrue().map { p -> p.characterName }

    open fun getCharacters(): List<String> = pilotRepo.findAll().map { p -> p.characterName }.toList()

    companion object {
        val log = LoggerFactory.getLogger(UserService::class.java)
    }

}