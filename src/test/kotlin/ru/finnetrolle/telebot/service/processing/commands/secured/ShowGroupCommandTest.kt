package ru.finnetrolle.telebot.service.processing.commands.secured

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import ru.finnetrolle.telebot.service.external.MailbotService
import ru.finnetrolle.telebot.service.processing.commands.unsecured.MailCommand
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Ignore
class ShowGroupCommandTest {

    @Mock private lateinit var loc: MessageLocalization
    @Mock private lateinit var mailbotService: MailbotService

    @InjectMocks private var cx = MailCommand()

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(loc.getMessage(Mockito.anyString())).thenAnswer { a -> a.arguments[0] }
    }


    @Test
    fun execute() {
        throw UnsupportedOperationException()
    }

}