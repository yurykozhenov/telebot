package ru.finnetrolle.telebot.service.external

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.test.util.ReflectionTestUtils
import org.telegram.telegrambots.api.objects.User
import ru.finnetrolle.telebot.service.internal.UserService

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
class ExternalRegistrationServiceTest {

    @Mock lateinit private var userService: UserService

    @InjectMocks private var service = ExternalRegistrationService()

    private val TELE_ID: Int = 1000
    private val USER_NAME: String = "oleg"
    private val USER_ID: Long = 1L
    private val USER = User()

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        ReflectionTestUtils.setField(USER, "id", TELE_ID)
        Mockito.`when`(userService.singleCheck(USER_ID)).thenReturn(UserService.SingleCheckResult.OK(USER_NAME, "", ""))
    }

    @Test
    fun clientCanRegisterContenderAndReceive6SymbolKey() {
        assertEquals(0, getContenders().size)
        val key = service.registerContender(USER_NAME, USER_ID)
        assertEquals(6, key.length)
        assertEquals(1, getContenders().size)
    }

    @Test
    fun validKeyApprovesRegistration() {
        val key = service.registerContender(USER_NAME, USER_ID)
        val result = service.tryToApproveContender(key, USER)
        assertEquals(true, result is ExternalRegistrationService.ApproveResult.Success)
        assertEquals(USER_NAME, (result as ExternalRegistrationService.ApproveResult.Success).name)
    }

    @Test
    fun validKeyRemovesContenderFromMap() {
        val key = service.registerContender(USER_NAME, USER_ID)
        assertEquals(1, getContenders().size)
        service.tryToApproveContender(key, USER)
        assertEquals(0, getContenders().size)
    }

    @Test
    fun lateKeyRemovesContenderAndFailsReg() {
        ReflectionTestUtils.setField(service, "TIMEOUT", 1)
        val key = service.registerContender(USER_NAME, USER_ID)
        Thread.sleep(10)
        assertEquals(true, service.tryToApproveContender(key, USER) is ExternalRegistrationService.ApproveResult.TimedOut)
    }

    @Test
    fun pilotFromAllianceNotInListTakesForbidden() {
        Mockito.`when`(userService.singleCheck(USER_ID)).thenReturn(UserService.SingleCheckResult.Renegade(USER_NAME, "", ""))
        val key = service.registerContender(USER_NAME, USER_ID)
        assertTrue(service.tryToApproveContender(key, USER) is ExternalRegistrationService.ApproveResult.Forbidden)
    }

    @Test
    fun pilotCheckReturnsSomeUnknownShitAndServiceReturnsForbidden() {
        Mockito.`when`(userService.singleCheck(USER_ID)).thenReturn(object : UserService.SingleCheckResult{})
        val key = service.registerContender(USER_NAME, USER_ID)
        assertTrue(service.tryToApproveContender(key, USER) is ExternalRegistrationService.ApproveResult.Forbidden)
    }

    @Test
    fun wrongKeyReturnsNotAKey() {
        val key = service.registerContender(USER_NAME, USER_ID)
        assertTrue(service.tryToApproveContender(key + "lol", USER) is ExternalRegistrationService.ApproveResult.NotAKey)
    }


    private fun getContenders(): Map<String, ExternalRegistrationService.PreData> {
        return ReflectionTestUtils.getField(service, "contenders") as Map<String, ExternalRegistrationService.PreData>
    }

}