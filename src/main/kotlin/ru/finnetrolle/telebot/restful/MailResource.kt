package ru.finnetrolle.telebot.restful

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
        return CheckWrapper(System.currentTimeMillis() - start, result)
    }

    data class CheckWrapper( val time: Long, val result: PilotService.CheckResult)

}