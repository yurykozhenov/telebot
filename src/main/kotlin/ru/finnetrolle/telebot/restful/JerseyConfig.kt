package ru.finnetrolle.telebot.restful

import org.glassfish.jersey.server.ResourceConfig
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

/**
* Licence: MIT
* Legion of xXDEATHXx notification bot for telegram
* Created by finnetrolle on 13.03.16.
*/
@Component
@Configuration
open class JerseyConfig: ResourceConfig {

    constructor() {
        packages(ru.finnetrolle.telebot.restful.JerseyConfig::class.java.`package`.name)
    }

}