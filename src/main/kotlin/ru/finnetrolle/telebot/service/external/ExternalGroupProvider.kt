package ru.finnetrolle.telebot.service.external

import org.springframework.stereotype.Component

@Component
class ExternalGroupProvider {

    fun getMembers(group: String): Set<String> {
        return setOf("Finne Trolle")
    }


}