package ru.finnetrolle.telebot.service.external

import feign.Feign
import feign.form.FormEncoder
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
class TranslateService {

    @Value("\${yandex.key}")
    private lateinit var key: String

    private lateinit var yandex: YandexTranslate

    private val log = LoggerFactory.getLogger(TranslateService::class.java)

    @PostConstruct
    fun init() {
        yandex = Feign.builder()
                .encoder(FormEncoder(GsonEncoder()))
                .decoder(GsonDecoder())
                .target(YandexTranslate::class.java, "https://translate.yandex.net")
    }

    fun translate(text: String, lang: String): String {
        try {
            return yandex.translate(text, lang, key).text.joinToString("\n")
        } catch (e: Exception) {
            log.warn("Yandex is broken", e)
            return "Can not be translated yet"
        }
    }

}