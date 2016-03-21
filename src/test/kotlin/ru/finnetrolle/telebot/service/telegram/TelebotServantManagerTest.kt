package ru.finnetrolle.telebot.service.telegram

import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.telegram.ServantManager.Command
import ru.finnetrolle.telebot.telegramapi.AllyService
import ru.finnetrolle.telebot.telegramapi.CorpService
import ru.finnetrolle.telebot.telegramapi.UserService

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 21.03.16.
 */
class TelebotServantManagerTest {

    @Mock lateinit var userService: UserService
    @Mock lateinit var allyService: AllyService
    @Mock lateinit var corpService: CorpService
    @Mock lateinit var messageProcessor: MessageProcessor

    @InjectMocks
    lateinit var manager: TelebotServantManager

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        manager = TelebotServantManager(messageProcessor, userService)
        Mockito.`when`(userService.getLegalUsers()).thenReturn(listOf(Pilot(username = "tester", id = 100500)))
        Mockito.`when`(userService.isModerator(1)).thenReturn(true)
        Mockito.`when`(userService.isModerator(2)).thenReturn(false)
        manager.configure()
    }

    @Test
    fun testCast() {
        Mockito.`when`(userService.getLegalUsers()).thenReturn(listOf(
                Pilot(id = 10),
                Pilot(id = 10),
                Pilot(id = 10)))
        val result = manager.serve(Command("/cast", TEXT, 1, "2"))
        assert(result.size == 3)
        result.forEach { m ->
            assert(m.chatId.equals(10.toString()))
            assert(m.text.equals(TEXT))
        }
    }

    @Test
    fun testFailSecurity() = listOf(
                Command("/addally", TEXT, USER_USUAL, CHAT_BACK),
                Command("/rmally", TEXT, USER_USUAL, CHAT_BACK),
                Command("/addcorp", TEXT, USER_USUAL, CHAT_BACK),
                Command("/rmcorp", TEXT, USER_USUAL, CHAT_BACK),
                Command("/check", TEXT, USER_USUAL, CHAT_BACK),
                Command("/dem", TEXT, USER_USUAL, CHAT_BACK),
                Command("/pro", TEXT, USER_USUAL, CHAT_BACK),
                Command("/renegade", TEXT, USER_USUAL, CHAT_BACK),
                Command("/legalize", TEXT, USER_USUAL, CHAT_BACK),
                Command("/lu", TEXT, USER_USUAL, CHAT_BACK),
                Command("/cast", TEXT, USER_USUAL, CHAT_BACK)
        ).forEach { c -> assert(manager.serve(c)[0].text.equals(Messages.ACCESS_DENIED)) }

    @Test
    fun testNoSecurity() {
        Mockito.`when`(messageProcessor.listOfAlliances()).thenReturn(DATA)
        Mockito.`when`(messageProcessor.listOfCorporations()).thenReturn(DATA)
        Mockito.`when`(messageProcessor.listOfModerators()).thenReturn(DATA)
        listOf(
            Command("/joke", TEXT, USER_USUAL, CHAT_BACK),
            Command("/lm", TEXT, USER_USUAL, CHAT_BACK),
            Command("/la", TEXT, USER_USUAL, CHAT_BACK),
            Command("/lc", TEXT, USER_USUAL, CHAT_BACK)
        ).forEach { c -> assert(!manager.serve(c)[0].text.equals(Messages.ACCESS_DENIED))}
    }

    companion object {
        val USER_MODERATOR: Int = 1
        val USER_USUAL: Int = 2

        val CHAT_BACK: String = "BACK"

        val TEXT: String = "TEXT"
        val DATA: String = "DATA"
    }
}