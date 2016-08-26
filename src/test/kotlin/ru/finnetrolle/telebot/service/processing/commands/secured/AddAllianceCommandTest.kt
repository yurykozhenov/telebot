package ru.finnetrolle.telebot.service.processing.commands.secured

import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import ru.finnetrolle.telebot.service.internal.AllyService
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
class AddAllianceCommandTest {

    @Mock private lateinit var loc: MessageLocalization
    @Mock private lateinit var service: AllyService

    @InjectMocks private var cx = AddAllianceCommand()

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