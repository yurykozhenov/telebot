//package ru.finnetrolle.telebot.service.telegram
//
//import org.junit.Assert.*
//import org.junit.Before
//import org.junit.Test
//import org.mockito.InjectMocks
//import org.mockito.Mock
//import org.mockito.Mockito
//import org.mockito.MockitoAnnotations
//import org.telegram.telegrambots.api.methods.SendMessage
//import org.telegram.telegrambots.api.objects.User
//import ru.finnetrolle.telebot.assertMessage
//import ru.finnetrolle.telebot.model.Pilot
//import ru.finnetrolle.telebot.service.telegram.processors.CommandProcessor
//import ru.finnetrolle.telebot.service.processing.MessageBuilder
//import ru.finnetrolle.telebot.service.external.ExternalRegistrationService
//import ru.finnetrolle.telebot.service.telegram.processors.AuthPreprocessor
//import ru.finnetrolle.telebot.service.internal.UserService
//import ru.finnetrolle.telebot.util.MessageLocalization
//
///**
// * Telegram bot
// * Licence: MIT
// * Author: Finne Trolle
// */
//class UpdatePreprocessorTest {
//
//    @InjectMocks
//    private var preprocessor = AuthPreprocessor()
//
//    @Mock private lateinit var commandProcessor: CommandProcessor
//    @Mock private lateinit var externalRegistrationService: ExternalRegistrationService
//    @Mock private lateinit var userService: UserService
//    @Mock private lateinit var loc: MessageLocalization
//    @Mock private var USER = User()
//
//    private val DATA = "some data"
//    private val COMMAND = "command"
//    private val TEXT = "$COMMAND $DATA"
//
//
//    @Before
//    fun init() {
//        MockitoAnnotations.initMocks(this)
//        Mockito.doAnswer { i -> i.arguments[0] }.`when`(loc).getMessage(Mockito.anyString(), Mockito.anyString())
//        Mockito.doAnswer { i -> i.arguments[0] }.`when`(loc).getMessage(Mockito.anyString())
//        Mockito.`when`(USER.id).thenReturn(USER_ID)
//        val message = MessageBuilder.build(CHAT_ID, DATA)
//        Mockito.doReturn(message).`when`(commandProcessor).process(COMMAND, DATA, USER, CHAT_ID)
//    }
//
//    private val RESP_WELCOME = "telebot.fastreg.welcome"
//    private val RESP_FORBIDDEN = "telebot.fastreg.forbidden"
//    private val RESP_EXPIRED = "telebot.fastreg.expired"
//    private val RESP_REGISTER = "messages.please.register"
//    private val RESP_RENEGADE = "messages.renegade"
//
//    private val PIN = "123456"
//    private val NOT_A_PIN = "1234567"
//    private val CHAT_ID = "id"
//    private val USER_ID = 10
//
//    fun unregisteredUser() {
//        Mockito.`when`(userService.getPilot(USER_ID)).thenReturn(null)
//    }
//
//    fun registeredUser() {
//        Mockito.`when`(userService.getPilot(USER_ID)).thenReturn(Pilot(USER_ID, "name", renegade = false))
//    }
//
//    fun renegadeUser() {
//        Mockito.`when`(userService.getPilot(USER_ID)).thenReturn(Pilot(USER_ID, "name", renegade = true))
//    }
//
//
//
//
//    @Test
//    fun guestEnterCorrectPinThenBecameUser() {
//        unregisteredUser()
//        Mockito.`when`(externalRegistrationService.tryToApproveContender(PIN, USER))
//                .thenReturn(ExternalRegistrationService.ApproveResult.Success("name","corp","ally"))
//        assertMessage(
//                preprocessor.selectResponse(PIN, USER, CHAT_ID),
//                MessageBuilder.build(CHAT_ID, RESP_WELCOME))
//    }
//
//    @Test
//    fun guestEnterBadPinThenAskedToRegister() {
//        unregisteredUser()
//        Mockito.`when`(externalRegistrationService.tryToApproveContender(PIN, USER))
//                .thenReturn(ExternalRegistrationService.ApproveResult.NotAKey("not important"))
//        assertMessage(
//                preprocessor.selectResponse(PIN, USER, CHAT_ID),
//                MessageBuilder.build(CHAT_ID, RESP_REGISTER))
//        Mockito.verify(externalRegistrationService, Mockito.times(1)).tryToApproveContender(PIN, USER)
//    }
//
//    @Test
//    fun guestEnterNotPinThenAskedToRegister() {
//        unregisteredUser()
//        assertMessage(
//                preprocessor.selectResponse(NOT_A_PIN, USER, CHAT_ID),
//                MessageBuilder.build(CHAT_ID, RESP_REGISTER))
//        Mockito.verify(externalRegistrationService, Mockito.times(0)).tryToApproveContender(NOT_A_PIN, USER)
//    }
//
//    @Test
//    fun guestEnterPinAndLateThenAskedToRetry() {
//        unregisteredUser()
//        Mockito.`when`(externalRegistrationService.tryToApproveContender(PIN, USER))
//                .thenReturn(ExternalRegistrationService.ApproveResult.TimedOut(100))
//        assertMessage(
//                preprocessor.selectResponse(PIN, USER, CHAT_ID),
//                MessageBuilder.build(CHAT_ID, RESP_EXPIRED))
//    }
//
//    @Test
//    fun guestEnterPinButHeNotFromAllowedAllianceOrCorpThenForbidden() {
//        unregisteredUser()
//        Mockito.`when`(externalRegistrationService.tryToApproveContender(PIN, USER))
//                .thenReturn(ExternalRegistrationService.ApproveResult.Forbidden("name"))
//        assertMessage(
//                preprocessor.selectResponse(PIN, USER, CHAT_ID),
//                MessageBuilder.build(CHAT_ID, RESP_FORBIDDEN))
//    }
//
//    @Test
//    fun userSendCorrectCommandThenGetResultMessage() {
//        registeredUser()
//        assertMessage(
//                preprocessor.selectResponse(TEXT, USER, CHAT_ID),
//                MessageBuilder.build(CHAT_ID, DATA))
//    }
//
//    @Test
//    fun userMarkedAsRenegadeSendAnyThenGetForbidden() {
//        renegadeUser()
//        assertMessage(
//                preprocessor.selectResponse(TEXT, USER, CHAT_ID),
//                MessageBuilder.build(CHAT_ID, RESP_RENEGADE))
//    }
//}

