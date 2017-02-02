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

fun <T> List<T>.getPagesCount(pageSize: Int): Int {
    return this.size / pageSize
}

fun <T> List<T>.getPage(pageSize: Int, page: Int): List<T> {
    val pages = this.getPagesCount(pageSize)
    if (page == pages) {
        return this.subList(page * pageSize, this.size - (page * pageSize))
    } else {
        return this.subList(page * pageSize, page * pageSize + pageSize)
    }
}