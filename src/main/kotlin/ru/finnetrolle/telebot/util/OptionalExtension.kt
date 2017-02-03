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
    return if (this.size % pageSize == 0) this.size / pageSize else this.size / pageSize + 1
}

fun <T> List<T>.getPage(page: Int, pageSize: Int): List<T> {
    val pages = this.getPagesCount(pageSize)
    if (page == pages - 1) {
        return this.subList(page * pageSize, this.size)
    } else {
        return this.subList(page * pageSize, page * pageSize + pageSize)
    }
}