package ru.finnetrolle.telebot.service.internal

import com.beimin.eveapi.response.eve.CharacterInfoResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.telegram.telegrambots.api.objects.User
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.model.PilotRepository
import ru.finnetrolle.telebot.service.external.EveApiConnector
import ru.finnetrolle.telebot.service.external.ExternalGroupProvider
import javax.annotation.PostConstruct

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by finnetrolle on 13.03.16.
 */

@Component

open class UserService {

    @Autowired lateinit private var pilotRepo: PilotRepository
    @Autowired lateinit private var allyService: AllyService
    @Autowired lateinit private var corpService: CorpService
    @Autowired lateinit private var eve: EveApiConnector
    @Autowired lateinit private var groups: ExternalGroupProvider

    @Value("\${telebot.superuser}")
    private lateinit var superUser: String

    private val DUMMY_PILOT = Pilot()

    @PostConstruct
    @Transactional
    open fun init() {
        log.info("System bot super user is $superUser")
        val user = setModerator(superUser, true)
        if (user == null) {
            log.info("Super User is not registered in system")
        } else {
            log.info("Succesfully promoted super user is $user")
        }
    }

    open fun register(user: User, character: String, characterId: Long) {
        log.info("registering new pilot: " + character)
        val pilot = Pilot(user.id, user.firstName, user.lastName, user.userName, character, characterId)
        val saved = pilotRepo.save(pilot);
        log.info("saved pilot is $saved");
    }

    open fun getLegalUsers(): List<Pilot> = pilotRepo.findByRenegadeFalse()

    open fun getLegalUsers(groupName: String): List<Pilot> {
        val names = groups.getMembers(groupName)
        return pilotRepo.findByRenegadeFalse()
                .filter { n -> names.contains(n.characterName) }
                .toList()
    }

    open fun getPilot(telegramId: Int): Pilot? {
        val pilot = pilotRepo.findOne(telegramId)
        return if (pilot != null) pilot else null
    }

    data class CheckPair(val pilot: Pilot, val character: CharacterInfoResponse)
    data class CheckResult(val renegaded: List<String>, val checked: Int)

    @Transactional
    open fun check(): CheckResult {
        val allowedAllies = allyService.getAll().map { a -> a.id }.toSet()
        val allowedCorps = corpService.getAll().map { c -> c.id }.toSet()
        val toCheck = pilotRepo.findByRenegadeFalse()
        val renes = toCheck
                .filter { p -> !isSuperUser(p) }
                .map { p -> CheckPair(p, eve.getCharacter(p.characterId)) }
                .filter { s -> !allowedAllies.contains(s.character.allianceID) }
                .filter { s -> !allowedCorps.contains(s.character.corporationID) }
        if (renes.isNotEmpty()) {
            pilotRepo.makeRenegades(renes.map { r -> r.pilot.id })
        }
        return CheckResult(renes.map { r -> r.pilot.characterName }, toCheck.size)
    }

    interface SingleCheckResult {
        data class OK(val name: String, val corp: String, val ally: String) : SingleCheckResult
        data class Renegade(val name: String, val corp: String, val ally: String) : SingleCheckResult
    }

    open fun singleCheck(characterId: Long): SingleCheckResult {
        val evechar = eve.getCharacter(characterId)
        if (evechar.characterName == null) {
            log.error("character name is null for $evechar with ${evechar.characterName} and ${evechar.characterID}")
            throw EveApiUnknownException();
        }
        if (allyService.getAll().filter { a -> a.id == evechar.allianceID }.isNotEmpty() ||
                corpService.getAll().filter { a -> a.id == evechar.corporationID }.isNotEmpty()) {
            return SingleCheckResult.OK (evechar.characterName, evechar.corporation, evechar.alliance)
        } else {
            return SingleCheckResult.Renegade(evechar.characterName, evechar.corporation, evechar.alliance)
        }
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


    private fun isSuperUser(pilot: Pilot) = pilot.characterName.equals(superUser)

    companion object {
        val log = LoggerFactory.getLogger(UserService::class.java)
    }

    open fun getAllUsers() = pilotRepo.findAll()

    open fun removeByTelegramId(chatId: String) {
        val id = chatId.toInt()
        val pilot = pilotRepo.findOne(id)
        pilotRepo.delete(chatId.toInt())
        log.warn("User ${pilot.characterName} is removed")
    }

}

class EveApiUnknownException : RuntimeException() {

}
