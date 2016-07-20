package ru.finnetrolle.telebot.restful

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
//import ru.finnetrolle.telebot.service.telegram._old.SimpleTelegramBot

/**
* Licence: MIT
* Legion of xXDEATHXx notification bot for telegram
* Created by finnetrolle on 13.03.16.
*/
//@Controller
//class BroadcastResource @Autowired constructor (
//        val bot: SimpleTelegramBot
//) {
//
//    data class Message(var text: String = "")
//
//    @RequestMapping(value = "/cast", method = arrayOf(RequestMethod.POST))
//    @ResponseBody
//    fun cast(@RequestBody message: Message): ResponseEntity<String> {
//        if (message.text.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//        bot.broadcast(message.text)
//        return ResponseEntity.ok("Message sent")
//    }
//
//    data class GroupMessage(var text: String = "", var group: String = "")
//
//    @RequestMapping(value = "/groupcast", method = arrayOf(RequestMethod.POST))
//    @ResponseBody
//    fun groupcast(@RequestBody message: GroupMessage): ResponseEntity<String> {
//        if (message.text.isEmpty() || message.group.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//        val receiversCount = bot.groupBroadcast(message.group, message.text)
//        return ResponseEntity.ok("Message sent to $receiversCount of ${message.group}")
//    }
//
//
//
//}