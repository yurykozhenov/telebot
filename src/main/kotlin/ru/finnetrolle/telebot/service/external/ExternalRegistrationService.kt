package ru.finnetrolle.telebot.service.external

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.telegram.telegrambots.api.objects.User
import ru.finnetrolle.telebot.service.internal.UserService
import java.util.*

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 05.07.16.
 */
@Controller
open class ExternalRegistrationService {

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

    interface ApproveResult {
        data class Success(val name: String, val corp: String, val ally: String): ApproveResult
        data class Forbidden(val name: String): ApproveResult
        data class TimedOut(val late: Long): ApproveResult
        data class NotAKey(val text: String): ApproveResult
    }

    open fun tryToApproveContender(key: String, user: User): ApproveResult {
        val cont = contenders[key]
        if (cont != null) {
            contenders.remove(key)
            if (cont.dueTo >= System.currentTimeMillis()) {
                val checkResult = userService.singleCheck(cont.charId);
                when (checkResult) {
                    is UserService.SingleCheckResult.OK -> {
                        log.info("Registered ${user.id} as ${cont.charName}")
                        userService.register(user, cont.charName, cont.charId)
                        return ApproveResult.Success(checkResult.name, checkResult.corp, checkResult.ally)
                    }
                    is UserService.SingleCheckResult.Renegade -> {
                        log.info("Renegade ${cont.charName} from ${checkResult.corp} of ${checkResult.ally} trying to register")
                        return ApproveResult.Forbidden(checkResult.name)
                    }
                    else -> {
                        return ApproveResult.Forbidden(cont.charName)
                    }
                }
            } else {
                return ApproveResult.TimedOut(System.currentTimeMillis() - cont.dueTo)
            }
        } else {
            return ApproveResult.NotAKey(key)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(ExternalRegistrationService::class.java)
    }

}