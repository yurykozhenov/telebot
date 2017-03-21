package ru.finnetrolle.telebot.service.processing.engine

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.annotation.Configuration

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Configuration
open class CommandExecutorBeanPostProcessor : BeanPostProcessor {

    @Autowired
    private lateinit var ces: CommandExecutorService

    private val excludes: List<String> = listOf("/JOKE", "/ADDJOKE", "/QUEST")

    override fun postProcessBeforeInitialization(bean: Any?, name: String?): Any? {
        if (bean is CommandExecutor && !excludes.contains(bean.name())) {
            log.info("Registering${securedMark(bean)}command executor ${bean.name()} ${bean.description()}")
            ces.addExecutor(bean)
        }
        return bean
    }

    private fun securedMark(ce: CommandExecutor) = if (ce.secured()) " secured " else " "

    override fun postProcessAfterInitialization(bean: Any?, name: String?) = bean

    private val log = LoggerFactory.getLogger(CommandExecutorBeanPostProcessor::class.java)
}