package ru.finnetrolle.telebot.util

import org.junit.Assert.*
import org.junit.Test

/**
 * Created by finnetrolle on 08.03.2017.
 */
class MessageLocalizationTest {

    @Test
    fun globalLocalizationTest() {
        val loc = MessageLocalization()
        assertEquals("Message system loaded successfully", loc.getMessage("message.system.load.approve"))
        assertEquals("Some var", loc.getMessage("variable", "var"))
    }

}