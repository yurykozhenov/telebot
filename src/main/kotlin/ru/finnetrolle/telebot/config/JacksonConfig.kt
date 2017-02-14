package ru.finnetrolle.telebot.config

import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
@Configuration
open class JacksonConfig {

    @Bean open fun kotlinModule() = KotlinModule()

}