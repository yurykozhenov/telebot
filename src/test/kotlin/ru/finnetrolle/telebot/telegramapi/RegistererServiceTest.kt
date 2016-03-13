package ru.finnetrolle.telebot.telegramapi

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.springframework.test.util.ReflectionTestUtils
import org.telegram.telegrambots.api.objects.User
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by maxsyachin on 13.03.16.
 */
class RegistererServiceTest {

    val user = User()

    @Before
    fun setUp() {
        ReflectionTestUtils.setField(user, "firstName", "firstName")
        ReflectionTestUtils.setField(user, "lastName", "lastName")
        ReflectionTestUtils.setField(user, "userName", "userName")
        ReflectionTestUtils.setField(user, "id", 10)

    }

    @Test
    fun testStartRegistration() {
    }

    @Test
    fun testIsInProcess() {

    }
}