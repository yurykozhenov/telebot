package ru.finnetrolle.telebot.telegramapi.jabber

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
import ru.finnetrolle.telebot.telegramapi.SimpleTelegramBot
import javax.annotation.PostConstruct
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 16.03.16.
 */

@Component
open class JabberBot @Autowired constructor (
        val telebot: SimpleTelegramBot
)  {

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

    private lateinit var chat: Chat

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
//                .setDebuggerEnabled(true)
                .setCompressionEnabled(false)
                .setHostnameVerifier(object : HostnameVerifier {
                    override fun verify(p0: String?, p1: SSLSession?): Boolean {
                        return true
                    }
                })
                .build()

        val connection = XMPPTCPConnection(conf)
//        val connection = XMPPTCPConnection(nick, password, server)
        connection.connect()
        connection.login()

        val chatMan = ChatManager.getInstanceFor(connection)
        chat = chatMan.createChat(broadcaster, { chat, message -> process(chat, message) })
//        chat = chatMan.createChat(broadcaster, { chat, message -> println("MESAGE: ${message.body}") })
        chat.sendMessage("Hello from bot!")
    }

    fun process(chat: Chat, message: Message) {
        println(message.body)
        log.info("Received from ${message.from} message: ${message.body}")
        telebot.broadcast(message.body)
    }

    companion object {
        private val log = LoggerFactory.getLogger(JabberBot::class.java)
    }

}