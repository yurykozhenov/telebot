package ru.finnetrolle.telebot.service.processing.engine

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.service.processing.engine.CommandExecutor
import javax.annotation.PostConstruct

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

//@Configuration
@Component
open class CommandExecutorService : BeanPostProcessor {

    private val executors = mutableListOf<CommandExecutor>()

    override fun postProcessBeforeInitialization(bean: Any?, name: String?) = bean

    override fun postProcessAfterInitialization(bean: Any?, name: String?): Any? {
        if (bean is CommandExecutor) {
            println("Executor found with name ${bean!!.javaClass.name}")
            executors.add(bean)
        }
        return bean
    }

    open fun commands() = executors

    companion object {
        val log = LoggerFactory.getLogger(CommandExecutorService::class.java)
    }
}