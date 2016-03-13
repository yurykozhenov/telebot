package ru.finnetrolle.telebot.restful

import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.server.ServerProperties
import org.springframework.stereotype.Component

/**
 * Created by maxsyachin on 13.03.16.
 */
@Component
open class JerseyConfig: ResourceConfig {

    constructor() {
        packages(JerseyConfig::class.java.`package`.name)
        register(com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider::class.java)
        property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true)
        property(ServerProperties.WADL_FEATURE_DISABLE, true)
    }

}