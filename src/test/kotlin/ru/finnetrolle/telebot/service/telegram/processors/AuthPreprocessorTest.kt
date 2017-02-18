package ru.finnetrolle.telebot.service.telegram.processors

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.telegram.telegrambots.api.objects.User
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.external.ExternalRegistrationService
import ru.finnetrolle.telebot.service.internal.PilotService
import ru.finnetrolle.telebot.util.MessageBuilder
import ru.finnetrolle.telebot.util.MessageLocalization
import java.util.*

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
class AuthPreprocessorTest {

    @InjectMocks
    private var preprocessor = AuthPreprocessor()

    @Mock private lateinit var externalRegistrationService: ExternalRegistrationService
    @Mock private lateinit var pilotService: PilotService
    @Mock private lateinit var loc: MessageLocalization
    @Mock private var USER = User()

    private val DATA = "some data"
    private val COMMAND = "command"
    private val TEXT = "$COMMAND $DATA"


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        Mockito.doAnswer { i -> i.arguments[0] }.`when`(loc).getMessage(Mockito.anyString(), Mockito.anyString())
        Mockito.doAnswer { i -> i.arguments[0] }.`when`(loc).getMessage(Mockito.anyString())
        Mockito.`when`(USER.id).thenReturn(USER_ID)
        val message = MessageBuilder.build(CHAT_ID, DATA)
        Mockito.`when`(externalRegistrationService.getKeyLength()).thenReturn(6)
    }

    private val RESP_WELCOME = "telebot.fastreg.welcome"
    private val RESP_FORBIDDEN = "telebot.fastreg.forbidden"
    private val RESP_EXPIRED = "telebot.fastreg.expired"
    private val RESP_REGISTER = "messages.please.register"
    private val RESP_RENEGADE = "messages.renegade"

    private val PIN = "123456"
    private val NOT_A_PIN = "1234567"
    private val CHAT_ID = "id"
    private val USER_ID = 10

    fun unregisteredUser() {
        Mockito.`when`(pilotService.getPilot(USER_ID)).thenReturn(Optional.empty())
    }

    fun registeredUser() {
        Mockito.`when`(pilotService.getPilot(USER_ID)).thenReturn(Optional.of(Pilot(USER_ID, "name", renegade = false)))
    }

    fun renegadeUser() {
        Mockito.`when`(pilotService.getPilot(USER_ID)).thenReturn(Optional.of(Pilot(USER_ID, "name", renegade = true)))
    }

    @Test
    fun guestEnterCorrectPinThenBecameUser() {
        unregisteredUser()
        Mockito.`when`(externalRegistrationService.tryToApproveContender(PIN, USER))
                .thenReturn(ExternalRegistrationService.ApproveResult.Success("name", "corp", "ally"))
        val result = preprocessor.selectResponse(PIN, USER, CHAT_ID)
        assertTrue(result is AuthPreprocessor.Auth.Intercepted)
        assertEquals(RESP_WELCOME, (result as AuthPreprocessor.Auth.Intercepted).response.text)
    }

    @Test
    fun guestEnterBadPinThenAskedToRegister() {
        unregisteredUser()
        Mockito.`when`(externalRegistrationService.tryToApproveContender(PIN, USER))
                .thenReturn(ExternalRegistrationService.ApproveResult.NotAKey("not important"))
        val result = preprocessor.selectResponse(PIN, USER, CHAT_ID)
        assertTrue(result is AuthPreprocessor.Auth.Intercepted)
        assertEquals(RESP_REGISTER, (result as AuthPreprocessor.Auth.Intercepted).response.text)
        Mockito.verify(externalRegistrationService, Mockito.times(1)).tryToApproveContender(PIN, USER)
    }

    @Test
    fun guestEnterNotPinThenAskedToRegister() {
        unregisteredUser()
        val result = preprocessor.selectResponse(NOT_A_PIN, USER, CHAT_ID)
        assertTrue(result is AuthPreprocessor.Auth.Intercepted)
        assertEquals(RESP_REGISTER, (result as AuthPreprocessor.Auth.Intercepted).response.text)
        Mockito.verify(externalRegistrationService, Mockito.times(0)).tryToApproveContender(NOT_A_PIN, USER)
    }

    @Test
    fun guestEnterPinAndLateThenAskedToRetry() {
        unregisteredUser()
        Mockito.`when`(externalRegistrationService.tryToApproveContender(PIN, USER))
                .thenReturn(ExternalRegistrationService.ApproveResult.TimedOut(100))

        val result = preprocessor.selectResponse(PIN, USER, CHAT_ID)
        assertTrue(result is AuthPreprocessor.Auth.Intercepted)
        assertEquals(RESP_EXPIRED, (result as AuthPreprocessor.Auth.Intercepted).response.text)
    }

    @Test
    fun guestEnterPinButHeNotFromAllowedAllianceOrCorpThenForbidden() {
        unregisteredUser()
        Mockito.`when`(externalRegistrationService.tryToApproveContender(PIN, USER))
                .thenReturn(ExternalRegistrationService.ApproveResult.Forbidden("name"))
        val result = preprocessor.selectResponse(PIN, USER, CHAT_ID)
        assertTrue(result is AuthPreprocessor.Auth.Intercepted)
        assertEquals(RESP_FORBIDDEN, (result as AuthPreprocessor.Auth.Intercepted).response.text)
    }

    @Test
    @Ignore
    fun userSendCorrectCommandThenGetResultMessage() {
        registeredUser()
        val result = preprocessor.selectResponse(TEXT, USER, CHAT_ID)
        assertTrue(result is AuthPreprocessor.Auth.Authorized)
        assertEquals(DATA, (result as AuthPreprocessor.Auth.Authorized).data)
    }

    @Test
    fun userMarkedAsRenegadeSendAnyThenGetForbidden() {
        renegadeUser()
        val result = preprocessor.selectResponse(TEXT, USER, CHAT_ID)
        assertTrue(result is AuthPreprocessor.Auth.Intercepted)
        assertEquals(RESP_RENEGADE, (result as AuthPreprocessor.Auth.Intercepted).response.text)
    }

    @Test
    fun unquthorizedUserSendsShitThenGetRegisterPlease() {
        unregisteredUser()
        val result = preprocessor.selectResponse(TEXT + "888", USER, CHAT_ID)
        assertTrue(result is AuthPreprocessor.Auth.Intercepted)
        assertEquals(RESP_REGISTER, (result as AuthPreprocessor.Auth.Intercepted).response.text)
    }
}

