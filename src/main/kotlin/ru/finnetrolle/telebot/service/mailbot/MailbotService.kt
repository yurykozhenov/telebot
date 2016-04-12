package ru.finnetrolle.telebot.service.mailbot

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.finnetrolle.telebot.model.Mail
import ru.finnetrolle.telebot.model.MailRepository
import ru.finnetrolle.telebot.service.eveapi.EveApiConnector
import java.util.concurrent.atomic.AtomicLong
import javax.annotation.PostConstruct

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 12.04.16.
 */

@Component
open class MailbotService {

    @Autowired lateinit private var eve: EveApiConnector

    @Autowired lateinit private var mailRepo: MailRepository

    @Value("\${mailbot.keyid}")
    private lateinit var keyId: Integer

    @Value("\${mailbot.vcode}")
    private lateinit var vCode: String

    @Value("\${mailbot.list.id}")
    private lateinit var listId: String

    private var lastId: AtomicLong = AtomicLong()

    @PostConstruct
    open fun init() {
        lastId.set(mailRepo.getMaxId() ?: 0)
        log.info("Last mail id is ${lastId.get()}")
    }

    open fun getLast(): List<Mail> {
        return mailRepo.findFirst3ByOrderByIdDesc()
    }

    @Transactional
    open fun receiveMail() {
        log.info("Receiving mail procedure")
        val mails = eve.getMailList(keyId.toInt(), vCode, listId)
                .filter { x -> x.messageID > lastId.get() }
                .filter { x -> x.title.contains("CTA") || x.title.contains("СТА") }
                .map { x -> Mail(x.messageID,
                        x.sentDate,
                        eve.getCharacter(x.senderID).characterName,
                        x.title,
                        purgeHtml(eve.getMailBody(keyId.toInt(), vCode, x.messageID))) }
        if (mails.isNotEmpty()) {
            lastId.set(mails.maxBy { x -> x.id }!!.id)
            mailRepo.save(mails)
        }
    }

    private fun purgeHtml(html: String): String {
        var text = html
        var pos = text.indexOf("<")
        while (pos != -1) {
            val end = text.indexOf(">")
            if (end == -1) {
                return text
            }
            if (text.substring(pos, end + 1).equals("<br>")) {
                text = text.substring(0, pos) + "\n" + text.substring(end + 1, text.length)
            } else {
                text = text.substring(0, pos) + text.substring(end + 1, text.length)
            }
            pos = text.indexOf("<")
        }
        return text
    }

    companion object {
        private val log = LoggerFactory.getLogger(MailbotService::class.java)
    }



}