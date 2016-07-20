package ru.finnetrolle.telebot.service.telegram

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.util.ReflectionTestUtils
import org.telegram.telegrambots.api.methods.SendMessage
import org.telegram.telegrambots.api.objects.Message
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.api.objects.User
import ru.finnetrolle.telebot.assertMessage
import ru.finnetrolle.telebot.service.telegram.processors.CommandProcessor
import ru.finnetrolle.telebot.service.processing.MessageBuilder
import ru.finnetrolle.telebot.service.external.ExternalRegistrationService
import ru.finnetrolle.telebot.service.telegram.processors.AuthPreprocessor
import ru.finnetrolle.telebot.service.internal.UserService
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
class ThirdTelegramBotTest {

//    @InjectMocks private var bot = ThirdTelegramBot()
    @Mock private lateinit var authPreprocessor: AuthPreprocessor
    @Mock private lateinit var UPDATE: Update
    @Mock private lateinit var USER: User
    @Mock private lateinit var MESSAGE: Message

    private val USER_ID: Int = 12345
    private val CHAT_ID: Long =  1234567
    private val TEXT: String = "some text"
    private val SEND_MESSAGE: SendMessage = MessageBuilder.build(CHAT_ID.toString(), TEXT)

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
//        Mockito.`when`(authPreprocessor.selectResponse(TEXT, USER, CHAT_ID.toString())).thenReturn(SEND_MESSAGE)
        Mockito.`when`(UPDATE.hasMessage()).thenReturn(true)
        Mockito.`when`(UPDATE.message).thenReturn(MESSAGE)
        Mockito.`when`(MESSAGE.from).thenReturn(USER)
        Mockito.`when`(MESSAGE.text).thenReturn(TEXT)
        Mockito.`when`(MESSAGE.chatId).thenReturn(CHAT_ID)
        Mockito.`when`(USER.id).thenReturn(USER_ID)

//        ReflectionTestUtils.setField(bot, "botToken", "hello")
//        ReflectionTestUtils.setField(bot, "botUsername", "bot")
//        ReflectionTestUtils.setField(bot, "messagesPerSecond", 30)
//
//        Mockito.`when`(bot.sendMessage(SEND_MESSAGE)).thenReturn(Message())
    }

    @Test
    fun listenerWorksFine() {
//        bot.onUpdateReceived(UPDATE)
        verify(authPreprocessor, Mockito.times(1)).selectResponse(TEXT, USER, CHAT_ID.toString())
    }

}