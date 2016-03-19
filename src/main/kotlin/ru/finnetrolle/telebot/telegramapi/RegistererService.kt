package ru.finnetrolle.telebot.telegramapi

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.objects.User
import ru.finnetrolle.telebot.service.eveapi.EveApiConnector
import java.util.concurrent.ConcurrentHashMap

/**
* Licence: MIT
* Legion of xXDEATHXx notification bot for telegram
* Created by finnetrolle on 13.03.16.
*/

@Component
class RegistererService @Autowired constructor (
        val userService: UserService,
        val eve: EveApiConnector,
        val allyService: AllyService,
        val corpService: CorpService
) {

    private data class Contender(
            val user: User,
            val key: Int,
            val code: String,
            val characters: List<EveApiConnector.Character>)

    private val registerCandidates = ConcurrentHashMap<Int, Contender>()

    fun startRegistration(user: User, key: Int, code: String): List<String>? {
        if (registerCandidates.containsKey(user.id)) {
            log.info("Start register process again for telegram person ${user.userName} id=${user.id}")
            registerCandidates.remove(user.id)
        }
        val chars = eve.getCharacters(key, code)
        if (chars != null) {
            log.info("Starting registration for ${user.userName} with id=${user.id}")
            registerCandidates.put(user.id, Contender(user, key, code, chars))
            return chars.map { x -> x.name }
        } else {
            log.warn("Can't start registration via bad auth pair for ${user.userName} id=${user.id}")
            return null
        }
    }

    fun isInProcess(userId: Int): Boolean {
        val result = registerCandidates.containsKey(userId)
        return result
    }

    fun getListOfCharacterCandidates(userId: Int): List<String> {
        return registerCandidates[userId]!!.characters.map{ c -> c.name}
    }

    interface Finish {
        data class SuccessByNoLists(val name: String) : Finish
        data class SuccessByAlliance(val name: String, val alliance: String) : Finish
        data class SuccessByCorporation(val name: String, val corp: String) : Finish
        data class FailByNotAllowed(val name: String) : Finish
        data class FailByWrongSelect(val characterNo: Int) : Finish
        data class FailByRegistrationExpired(val userId: Int) : Finish
    }

    fun finishRegistration(userId: Int, characterNo: Int) : Finish {
        log.info("Trying to finish registration")
        val contender = registerCandidates[userId]
        if (contender == null) {
            log.warn("Can't find contender in candidates for user id=$userId")
            return Finish.FailByRegistrationExpired(userId)
        }
        val char = contender.characters.getOrNull(characterNo)
        if (char != null) {
            val eveChar = eve.getCharacter(char.id)
            if (allyService.contains(eveChar.allianceID)) {
                log.info("Registration successful for $contender by alliance")
                register(contender, char)
                return Finish.SuccessByAlliance(char.name, allyService.get(eveChar.allianceID).ticker)
            } else if (corpService.contains(eveChar.corporationID)) {
                log.info("Registration successful for $contender by corporation")
                register(contender, char)
                return Finish.SuccessByCorporation(char.name, corpService.get(eveChar.corporationID).ticker)
            } else if (allyService.isEmpty() && corpService.isEmpty()){
                log.info("Registration successful for $contender because no allowed lists allowed")
                register(contender, char)
                return Finish.SuccessByNoLists(char.name)
            } else {
                return Finish.FailByNotAllowed(char.name)
            }
        } else {
            return Finish.FailByWrongSelect(characterNo)
        }
    }

    private fun register(contender: Contender, char: EveApiConnector.Character) {
        userService.register(contender.user, contender.key, contender.code, char.name, char.id)
        registerCandidates.remove(contender.user.id)
    }

    companion object {
        private val log = LoggerFactory.getLogger(RegistererService::class.java)
    }

}