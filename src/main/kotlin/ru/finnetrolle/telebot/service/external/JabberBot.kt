package ru.finnetrolle.telebot.service.external

import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.chat.Chat
import org.jivesoftware.smack.chat.ChatManager
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.processing.commands.secured.GlobalBroadcasterCommand
import ru.finnetrolle.telebot.service.processing.commands.secured.GroupBroadcastCommand
import javax.annotation.PostConstruct

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 16.03.16.
 */

@Component
open class JabberBot {

    @Autowired
    private lateinit var groupExecutor: GroupBroadcastCommand

    @Autowired
    private lateinit var globalExecutor: GlobalBroadcasterCommand

    @Value("\${jabber.bot.nick}")
    private lateinit var nick: String
    @Value("\${jabber.bot.password}")
    private lateinit var password: String
    @Value("\${jabber.bot.domain}")
    private lateinit var domain: String
    @Value("\${jabber.bot.server}")
    private lateinit var server: String
    @Value("\${jabber.bot.port}")
    private var port: Int = 0
    @Value("\${jabber.bot.broadcaster}")
    private lateinit var broadcaster: String
    @Value("\${jabber.bot.alive}")
    private var alive: Boolean = false

    private var chat: Chat? = null

    @PostConstruct
    fun init() {
        if (!alive) {
            return
        }
        val conf = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(nick, password)
                .setServiceName(domain)
                .setHost(server)
                .setPort(port)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setCompressionEnabled(false)
                .setHostnameVerifier({ p0, p1 -> true })
                .build()

        val connection = XMPPTCPConnection(conf)
        connection.connect()
        connection.login()

        val chatMan = ChatManager.getInstanceFor(connection)
        chat = chatMan.createChat(broadcaster, { chat, message -> process(message) })
    }

    private fun grabGroupName(message: String): String {
        return message.split(" ")[4].split("\n")[0]
    }

    private fun process(message: Message) {
        val groupName = grabGroupName(message.body)
        log.info("Received from [${message.from}] for [$groupName] message: [${message.body}]")
        if (groupName.toUpperCase().equals("ALL")) {
            log.info("Sending to everybody")
            globalExecutor.execute(Pilot(id = 0, characterName = "Jabber"), message.body)
        } else {
            log.info("Sending to group $groupName")
            groupExecutor.execute(Pilot(id = 0, characterName = "Jabber"), "$groupName ${message.body}")
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(JabberBot::class.java)
    }

}