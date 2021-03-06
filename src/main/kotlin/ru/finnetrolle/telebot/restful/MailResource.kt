package ru.finnetrolle.telebot.restful

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import ru.finnetrolle.telebot.model.Mail
import ru.finnetrolle.telebot.service.external.MailbotService
import ru.finnetrolle.telebot.service.internal.PilotService

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 12.04.16.
 */

@Controller
@RequestMapping("/test")
class MailResource {

    @Autowired lateinit private var mailbot: MailbotService

    @Autowired
    private lateinit var pilotService: PilotService

    private val log = LoggerFactory.getLogger(MailResource::class.java)

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun mail(): ResponseEntity<List<Mail>> {
        mailbot.receiveMail()
        return ResponseEntity.ok(mailbot.getLast())
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), path = arrayOf("/check"))
    @ResponseBody
    fun check(): CheckWrapper {
        val start = System.currentTimeMillis()
        val result = pilotService.check()
        val wrapper = CheckWrapper(System.currentTimeMillis() - start, result)
        log.info("Renegade check result: $wrapper")
        return wrapper
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), path = arrayOf("/amnesty"))
    @ResponseBody
    fun amnesty(): CheckWrapper {
        val start = System.currentTimeMillis()
        val result = pilotService.amnesty()
        val wrapper = CheckWrapper(System.currentTimeMillis() - start, result)
        log.info("Renegade amnesty result: $wrapper")
        return wrapper
    }

    data class AmnestyWrapper( val time: Long, val result: PilotService.CheckResult)
    data class CheckWrapper( val time: Long, val result: PilotService.CheckResult)

}