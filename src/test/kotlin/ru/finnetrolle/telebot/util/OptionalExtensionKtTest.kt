package ru.finnetrolle.telebot.util

import org.junit.Assert.*
import org.junit.Test

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
class OptionalExtensionKtTest {

    @Test
    fun testPagesCount() {
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        assertEquals(1, list.getPagesCount(11))
        assertEquals(1, list.getPagesCount(21))
        assertEquals(1, list.getPagesCount(10))
        assertEquals(2, list.getPagesCount(9))
        assertEquals(2, list.getPagesCount(5))
        assertEquals(3, list.getPagesCount(4))
        assertEquals(4, list.getPagesCount(3))
        assertEquals(5, list.getPagesCount(2))
        assertEquals(10, list.getPagesCount(1))
    }

}