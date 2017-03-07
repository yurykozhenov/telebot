package ru.finnetrolle.telebot.util

import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 11.07.16.
 */

@Component
open class MessageLocalization {

    private var messageSource: MessageSource? = null

    constructor() {
        this.messageSource = getMessageSource()
    }

    constructor(messageSource: ReloadableResourceBundleMessageSource) {
        this.messageSource = messageSource
    }

    private fun getMessageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setBasename("messages")
        messageSource.setDefaultEncoding("UTF-8")
        return messageSource
    }

    open fun getMessage(message: String): String {
        return this.messageSource!!.getMessage(message, null, Locale.getDefault())
    }

    open fun getMessage(message: String, vararg params: Any): String {
        return messageSource!!.getMessage(message, params, Locale.getDefault())
    }

    private val log = LoggerFactory.getLogger(MessageLocalization::class.java)

}
