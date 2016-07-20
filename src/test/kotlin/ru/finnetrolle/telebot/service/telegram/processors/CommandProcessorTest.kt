package ru.finnetrolle.telebot.service.telegram.processors

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.processing.engine.CommandExecutorService
import ru.finnetrolle.telebot.util.MessageBuilder

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
class CommandProcessorTest {

    @Mock private lateinit var ces: CommandExecutorService

    @InjectMocks private var processor = CommandProcessor()

    private val SEND_MESSAGE = MessageBuilder.build("","")
    private val PILOT = Pilot(id = 1)

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun processCallsCES() {
        Mockito.`when`(ces.execute(
                "",
                "",
                PILOT,
                "1"))
                .thenReturn(SEND_MESSAGE)
        assertEquals(SEND_MESSAGE, processor.process("", "", PILOT))
    }

}