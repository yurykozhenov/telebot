package ru.finnetrolle.telebot.restful

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.processing.commands.secured.GlobalBroadcasterCommand
import ru.finnetrolle.telebot.service.processing.commands.secured.GroupBroadcastCommand

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by finnetrolle on 13.03.16.
 */
@Controller
class BroadcastResource {

    @Autowired
    private lateinit var globalCaster: GlobalBroadcasterCommand

    @Autowired
    private lateinit var groupCaster: GroupBroadcastCommand

    @Value("\${api.secret.cast.group}")
    private lateinit var groupCastSecret: String

    @Value("\${api.secret.cast.global}")
    private lateinit var globalCastSecret: String

    data class Message(var text: String = "", var secret: String = "", var from: String = "")

    @RequestMapping(value = "/cast", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun cast(@RequestBody message: Message): ResponseEntity<String> {
        if (message.text.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (!message.secret.equals(globalCastSecret)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(globalCaster.execute(Pilot(characterName = message.from), message.text))
    }

    data class GroupMessage(var text: String = "", var group: String = "", var secret: String = "", var from: String = "")

    @RequestMapping(value = "/gc", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun groupcast(@RequestBody message: GroupMessage): ResponseEntity<String> {
        if (message.text.isEmpty() || message.group.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (!message.secret.equals(groupCastSecret)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(groupCaster.execute(Pilot(characterName = message.from), "${message.group} ${message.text}"))
    }


}