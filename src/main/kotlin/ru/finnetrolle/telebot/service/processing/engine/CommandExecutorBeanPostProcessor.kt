package ru.finnetrolle.telebot.service.processing.engine

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.annotation.Configuration

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Configuration
open class CommandExecutorBeanPostProcessor: BeanPostProcessor {

    @Autowired
    private lateinit var ces: CommandExecutorService

    override fun postProcessBeforeInitialization(bean: Any?, name: String?): Any? {
        if (bean is CommandExecutor) {
            println("Executor found with name ${bean!!.javaClass.name} for command ${bean.name()}")
            ces.addExecutor(bean)
        }
        return bean
    }

    override fun postProcessAfterInitialization(bean: Any?, name: String?) = bean
}