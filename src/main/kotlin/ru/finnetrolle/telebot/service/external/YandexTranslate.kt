package ru.finnetrolle.telebot.service.external

import feign.Headers
import feign.Param
import feign.RequestLine

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

interface YandexTranslate {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @RequestLine("POST /api/v1.5/tr.json/translate?lang={lang}&key={key}")
    fun translate(@Param("text") text: String,
                  @Param("lang") lang: String,
                  @Param("key") key: String = "somekey"): Translated

    data class Translated(var text: List<String> = mutableListOf(), var lang: String = "", var code: Int = 0)
}

