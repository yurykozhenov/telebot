package ru.finnetrolle.telebot.service.processing.commands.secured

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.*
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
@Component
class ListCommand : AbstractSecuredCommand() {

    @Autowired
    private lateinit var loc: MessageLocalization

    @Autowired
    private lateinit var pilots: PilotRepository

    @Autowired
    private lateinit var allies: AllianceRepository

    @Autowired
    private lateinit var corps: CorporationRepository

    override fun name() = "/LIST"

    override fun description() = loc.getMessage("telebot.command.description.list")

    override fun execute(pilot: Pilot, data: String): String {
        val list: List<String> = when (data.toUpperCase()) {
            "PILOTS" -> {
                pilots.findByRenegadeFalse().pilotNames()
            }
            "MODERATORS" -> {
                pilots.findByModeratorTrue().pilotNames()
            }
            "SPEAKERS" -> {
                pilots.findBySpeakerTrue().pilotNames()
            }
            "ALLIANCES" -> {
                allies.findAll().allyNames()
            }
            "CORPORATIONS" -> {
                corps.findAll().corpNames()
            }
            else -> listOf()
        }
        if (list.isEmpty()) {
            return loc.getMessage("messages.list.nothing")
        }
        return loc.getMessage("messages.list.${data.toUpperCase()}", list.size, list.joinToString("\n"))
    }

    fun List<Pilot>.pilotNames() = this.map { it.characterName }.sortedBy { it.toUpperCase() }
    fun List<Alliance>.allyNames() = this.sortedBy { it.title.toUpperCase() }.map { "[${it.ticker}] - ${it.title}" }
    fun List<Corporation>.corpNames() = this.sortedBy { it.title.toUpperCase() }.map { "[${it.ticker}] - ${it.title}" }

}