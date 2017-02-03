package ru.finnetrolle.telebot.service.external

import feign.Feign
import feign.form.FormEncoder
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
class YandexTest {

    @Test
    @Ignore
    fun test() {
        val t = Feign.builder()
                .encoder(FormEncoder(GsonEncoder()))
                .decoder(GsonDecoder())
                .target(YandexTranslate::class.java, "https://translate.yandex.net")

        assertEquals("Hello, assholes", t.translate("Привет, придурки", "en").text[0])
    }

}