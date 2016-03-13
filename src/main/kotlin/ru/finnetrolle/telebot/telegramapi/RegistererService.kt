package ru.finnetrolle.telebot.telegramapi

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
            val characters: List<String>)

    private val registerCandidates = ConcurrentHashMap<Int, Contender>()

    fun startRegistration(user: User, key: Int, code: String): List<String> {
        if (registerCandidates.containsKey(user.id)) {
            return registerCandidates.get(user.id)!!.characters
        } else {
            val chars = eve.getCharacters(key, code)
            registerCandidates.put(user.id, Contender(user, key, code, chars))
            return chars
        }
    }

    fun isInProcess(userId: Int): Boolean {
        val result = registerCandidates.containsKey(userId)
        return result
    }

    fun getListOfCharacterCandidates(userId: Int): List<String> {
        return registerCandidates.get(userId)!!.characters
    }

    fun finishRegistration(userId: Int, characterNo: Int): String? {
        val contender = registerCandidates.get(userId)
        val char = contender!!.characters.getOrNull(characterNo)
        if (char != null) {
            userService.register(contender.user, contender.key, contender.code, char)
            registerCandidates.remove(userId)
            return char
        }
        return null
    }

}