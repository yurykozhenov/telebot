package ru.finnetrolle.telebot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by finnetrolle on 12.03.16.
 */

@SpringBootApplication
open class Application


fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
