package ru.finnetrolle.telebot.restful

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
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

@Component
@Path("cast")
class BroadcastResource @Autowired constructor (
        val bot: SimpleTelegramBot
) {

    data class Message(var text: String = "")

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun cast(message: Message): Response {
        bot.broadcast(message.text)
        return Response.ok().build()
    }

}