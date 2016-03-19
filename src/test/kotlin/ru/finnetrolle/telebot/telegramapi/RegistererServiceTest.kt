package ru.finnetrolle.telebot.telegramapi

import org.junit.Before
import org.junit.Test

import org.springframework.test.util.ReflectionTestUtils
import org.telegram.telegrambots.api.objects.User
import ru.finnetrolle.telebot.model.Alliance

/**
* Licence: MIT
* Legion of xXDEATHXx notification bot for telegram
* Created by finnetrolle on 13.03.16.
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