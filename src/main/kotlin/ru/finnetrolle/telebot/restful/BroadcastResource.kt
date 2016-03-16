package ru.finnetrolle.telebot.restful

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import ru.finnetrolle.telebot.telegramapi.SimpleTelegramBot
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
* Licence: MIT
* Legion of xXDEATHXx notification bot for telegram
* Created by finnetrolle on 13.03.16.
*/

//@Component
//@Path("cast")
@Controller
@RequestMapping("/cast")
class BroadcastResource @Autowired constructor (
        val bot: SimpleTelegramBot
) {

    data class Message(var text: String = "")

//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun cast(@RequestBody message: Message): ResponseEntity<String> {
        if (message.text.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        bot.broadcast(message.text)
//        return Response.ok().build()
        return ResponseEntity.ok("Message sent")
    }

}