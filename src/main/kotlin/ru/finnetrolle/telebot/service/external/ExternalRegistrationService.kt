package ru.finnetrolle.telebot.service.external

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.telegram.telegrambots.api.objects.User
import ru.finnetrolle.telebot.service.internal.PilotService
import java.util.*

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 05.07.16.
 */
@Controller
open class ExternalRegistrationService {

    @Autowired
    lateinit private var pilotService: PilotService

    data class PreData(val charName: String, val charId: Long, val dueTo: Long)

    public val KEY_LENGTH: Int = 6

    private val contenders: MutableMap<String, PreData> = mutableMapOf()

    fun registerContender(charName: String, charId: Long): String {
        log.info("Add new contender $charName with id=$charId")
        val key = UUID.randomUUID().toString().substring(0, KEY_LENGTH)
        val dueTo = System.currentTimeMillis() + TIMEOUT
        contenders.put(key.toUpperCase(), PreData(charName, charId, dueTo))
        return key
    }

    sealed class ApproveResult {
        class Success(val name: String, val corp: String, val ally: String) : ApproveResult()
        class Forbidden(val name: String) : ApproveResult()
        class TimedOut(val late: Long) : ApproveResult()
        class NotAKey(val text: String) : ApproveResult()
    }

    open fun tryToApproveContender(key: String, user: User): ApproveResult {
        val cont = contenders[key.toUpperCase()]
        if (cont != null) {
            contenders.remove(key.toUpperCase())
            if (cont.dueTo >= System.currentTimeMillis()) {
                val checkResult = pilotService.singleCheck(cont.charId);
                when (checkResult) {
                    is PilotService.SingleCheckResult.OK -> {
                        log.info("Registered ${user.id} as ${cont.charName}")
                        pilotService.add(user, cont.charName, cont.charId)
                        return ApproveResult.Success(checkResult.name, checkResult.corp, checkResult.ally)
                    }
                    is PilotService.SingleCheckResult.Renegade -> {
                        log.info("Renegade ${cont.charName} from ${checkResult.corp} of ${checkResult.ally} trying to register")
                        return ApproveResult.Forbidden(checkResult.name)
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
        private var TIMEOUT = 1000 * 60 * 20
    }

}