package ru.finnetrolle.telebot.service.external

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.test.util.ReflectionTestUtils
import ru.finnetrolle.telebot.service.processing.commands.unsecured.GlobalBroadcasterCommand
import ru.finnetrolle.telebot.service.processing.commands.secured.GroupBroadcastCommand

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
class JabberBotTest {

    @Mock private lateinit var groupExecutor: GroupBroadcastCommand
    @Mock private lateinit var globalExecutor: GlobalBroadcasterCommand

    @InjectMocks private var jabber = JabberBot()

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun activeSettingTurnsJabberBotOfIfFalse () {
        ReflectionTestUtils.setField(jabber, "alive", false)
        jabber.init()
        assertTrue(ReflectionTestUtils.getField(jabber, "chat") == null)
    }

}