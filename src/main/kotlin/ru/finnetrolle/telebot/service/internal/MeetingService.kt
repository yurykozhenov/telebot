package ru.finnetrolle.telebot.service.internal

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.finnetrolle.telebot.model.Meeting
import ru.finnetrolle.telebot.model.MeetingRepository
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.telegram.TelegramBotService
import ru.finnetrolle.telebot.util.MessageBuilder
import ru.finnetrolle.telebot.util.MessageLocalization
import ru.finnetrolle.telebot.util.decide

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
open class MeetingService {

    @Autowired
    private lateinit var pilotService : PilotService

    @Autowired
    private lateinit var meetingRepo: MeetingRepository

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var telegram: TelegramBotService

    private val log = LoggerFactory.getLogger(MeetingService::class.java)

    @Transactional
    open fun createMeeting(meeter: Pilot, wants: String): String {
        log.debug("Trying to meet from $meeter to $wants")
        if (meeter.username == null) {
            log.debug("Meeting failed because meeter username not exists")
            return loc.getMessage("messages.meeting.must.have.username")
        }
        return pilotService.getPilot(wants).decide({
            // todo: check if meeting is already exists
            if (it.username?.isNotEmpty() ?: false) {
                val meeting = meetingRepo.save(Meeting(from = meeter.id, to = it.id))
                telegram.broadcast(listOf(MessageBuilder.build(
                                it.id.toString(),
                                loc.getMessage("messages.meet", meeter.characterName, meeting.id))))
                log.debug("Meeting created $meeting")
                loc.getMessage("messages.meeting.wait", it.characterName)
            } else {
                log.debug("Meeting failed because meeter username not exists")
                loc.getMessage("messages.meeting.impossible", wants)
            }
        },{
            log.debug("Meeting failed because user is not found")
            loc.getMessage("message.user.not.found")
        })
    }

    @Transactional
    open fun acceptMeeting(meetingId: String): String {
        log.debug("Accepting $meetingId")
        return meetingRepo.findOne(meetingId).decide({
            val meeter = pilotService.getPilot(it.from)
            val target = pilotService.getPilot(it.to)
            if (meeter.isPresent && target.isPresent) {
                it.result = "YES"
                meetingRepo.save(it)
                telegram.broadcast(listOf(MessageBuilder.build(
                        meeter.get().id.toString(),
                        loc.getMessage("messages.meet.yes", target.get().characterName, target.get().username!!))))
                loc.getMessage("messages.meet.accepted", meeter.get().characterName, meeter.get().username!!)
            } else {
                loc.getMessage("messages.user.not.found")
            }
        },{
            loc.getMessage("messages.meeting.not.exists")
        })
    }

    @Transactional
    open fun declineMeeting(meetingId: String): String {
        log.debug("Declining $meetingId")
        return meetingRepo.findOne(meetingId).decide({
            val meeter = pilotService.getPilot(it.from)
            val target = pilotService.getPilot(it.to)
            if (meeter.isPresent && target.isPresent) {
                it.result = "NO"
                meetingRepo.save(it)
                telegram.broadcast(listOf(MessageBuilder.build(
                        meeter.get().id.toString(),
                        loc.getMessage("messages.meet.no", target.get().characterName))))
                loc.getMessage("messages.meet.declined", meeter.get().characterName)
            } else {
                loc.getMessage("messages.user.not.found")
            }
        },{
            loc.getMessage("messages.meeting.not.exists")
        })
    }

}