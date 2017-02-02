package ru.finnetrolle.telebot.util

import java.util.*

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

fun <T, R> Optional<T>.decide(success: (T) -> R, failure: () -> R): R {
    return if (this.isPresent) {
        success.invoke(this.get())
    } else {
        failure.invoke()
    }
}