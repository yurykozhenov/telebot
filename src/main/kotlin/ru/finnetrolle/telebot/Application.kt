package ru.finnetrolle.telebot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Created by maxsyachin on 12.03.16.
 */

@SpringBootApplication
open class Application


fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
