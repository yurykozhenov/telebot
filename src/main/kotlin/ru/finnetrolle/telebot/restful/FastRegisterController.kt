package ru.finnetrolle.telebot.restful

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import ru.finnetrolle.telebot.telegramapi.ExternalRegistrationService

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 05.07.16.
 */

@Component
@RequestMapping("/register")
class FastRegisterController {

    @Value("\${telebot.secret.external.register}")
    lateinit private var secret: String

    @Autowired
    lateinit private var service: ExternalRegistrationService

    data class RegisterRequest(var secret: String = "", var name: String = "", var id: Long = 0)
    data class RegisterResponse(var key: String = "")

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<RegisterResponse> {
        if (secret.equals(request.secret)) {
            try {
                val key = service.registerContender(request.name, request.id)
                return ResponseEntity.ok(RegisterResponse(key))
            } catch (e: Exception) {
                return ResponseEntity.status(500).body(null)
            }
        } else {
            log.warn("Bad secret received! Danger! Trying to register ${request.name} of ${request.id} with secret = ${request.secret}")
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(FastRegisterController::class.java)
    }

}