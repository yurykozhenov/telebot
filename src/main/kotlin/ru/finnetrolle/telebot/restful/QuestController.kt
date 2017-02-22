package ru.finnetrolle.telebot.restful

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.internal.QuestService

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
@RequestMapping("quest")
class QuestController {

    @Autowired
    private lateinit var service: QuestService

    data class AddRequest(val text: String, val options: List<String>)

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun add(@RequestBody request: AddRequest) = service.create(Pilot(10), request.text, request.options)

    @RequestMapping("/{questId}")
    @ResponseBody
    fun get(@PathVariable("questId") id: String) = service.represent(id)

    @RequestMapping(path = arrayOf("/vote/{optionId}/{pilotId}"), method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun put(@PathVariable("optionId") optionId: String, @PathVariable("pilotId") pilotId: Int) =
            Wrapper(service.vote(pilotId, optionId).toString())

    data class Wrapper (val result: String)

}