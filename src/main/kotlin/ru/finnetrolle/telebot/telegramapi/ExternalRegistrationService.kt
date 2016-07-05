package ru.finnetrolle.telebot.telegramapi

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.telegram.telegrambots.api.objects.User
import java.util.*

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 05.07.16.
 */
@Controller
class ExternalRegistrationService {

    @Autowired
    lateinit private var userService: UserService

    data class PreData(val charName: String, val charId: Long, val dueTo: Long)

    private val contenders: MutableMap<String, PreData> = mutableMapOf()

    fun registerContender(charName: String, charId: Long): String {
        log.info("Add new contender $charName with id=$charId")
        val key = UUID.randomUUID().toString().substring(0, 6)
        val dueTo = System.currentTimeMillis() + 1000 * 60 * 20
        contenders.put(key.toUpperCase(), PreData(charName, charId, dueTo))
        return key
    }

    fun tryToApproveContender(key: String, user: User): Boolean {
        val cont = contenders[key]
        if (cont != null) {
            contenders.remove(key)
            if (cont.dueTo >= System.currentTimeMillis()) {
                log.info("Registered ${user.id} as ${cont.charName}")
                userService.register(user, 0, "", cont.charName, cont.charId)
                return true
            } else {
                return false
            }
        } else {
            return false
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(ExternalRegistrationService::class.java)
    }

}