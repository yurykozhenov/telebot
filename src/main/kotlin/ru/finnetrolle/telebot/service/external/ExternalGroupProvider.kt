package ru.finnetrolle.telebot.service.external

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import javax.ws.rs.core.UriBuilder

@Component
class ExternalGroupProvider {

    @Value("\${telebot.external.groups.secret}")
    lateinit private var secret: String

    @Value("\${telebot.external.groups.url}")
    lateinit private var url: String

    data class Rows(var rows:List<String> = listOf(), val success: Boolean = true, val total: Int = 0)

    fun getMembers(group: String): Set<String> {
        val template = RestTemplate()
        val uri = UriBuilder.fromUri(url).queryParam("groupName", group).queryParam("secret", secret).build()
        try {
            val names = template.getForObject(uri, Rows::class.java).rows.distinct().toSet()
            log.info("Found ${names.size} users for $group")
            return names
        } catch (e: Exception) {
            log.error("Some error in query for group ${group} ${e.message}")
            return setOf()
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(ExternalGroupProvider::class.java)
    }


}