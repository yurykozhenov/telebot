package ru.finnetrolle.telebot.telegramapi

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.objects.User
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by maxsyachin on 13.03.16.
 */

@Component
class RegistererService @Autowired constructor (
        val userService: UserService,
        val eve: EveApiConnector
) {

    private data class Contender(
            val user: User,
            val key: Int,
            val code: String,
            val characters: List<EveApiConnector.Character>)

    private val registerCandidates = ConcurrentHashMap<Int, Contender>()

    fun startRegistration(user: User, key: Int, code: String): List<String>? {
        if (registerCandidates.containsKey(user.id)) {
            log.info("Reregistering telegram person ${user.userName} id=${user.id}")
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
        return registerCandidates.get(userId)!!.characters.map{ c -> c.name}
    }

    fun finishRegistration(userId: Int, characterNo: Int): String? {
        log.info("Trying to finish registration")
        val contender = registerCandidates.get(userId)
        if (contender == null) {
            log.warn("Can't find contender in candidates for userid=${userId}")
            return null
        }
        val char = contender.characters.getOrNull(characterNo)
        if (char != null) {
            log.info("Registration successfull for ${contender}")
            userService.register(contender.user, contender.key, contender.code, char.name, char.id)
            registerCandidates.remove(userId)
            return char.name
        } else {
            return null
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(RegistererService::class.java)
    }

}