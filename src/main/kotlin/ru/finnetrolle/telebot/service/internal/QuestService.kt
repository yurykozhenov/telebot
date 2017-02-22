package ru.finnetrolle.telebot.service.internal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.finnetrolle.telebot.model.*
import ru.finnetrolle.telebot.util.decide
import java.time.LocalDateTime
import java.util.*

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
open class QuestService {


    @Autowired
    private lateinit var pilotRepo: PilotRepository

    @Autowired
    private lateinit var questRepo: QuestRepository

    @Autowired
    private lateinit var optionRepo: QuestOptionRepository

    @Transactional
    open fun create(pilot: Pilot, questText: String, options: List<String>,
                       activeMinutes: Long = 15L, groupName: String = ""): QuestRepresentation {
        val quest = Quest(
                author = pilot.id,
                expires = LocalDateTime.now().plusMinutes(activeMinutes),
                groupName = groupName,
                text = questText)

        quest.options = options.map { QuestOption(text = it, quest = quest) }.toMutableList()
        return represent(questRepo.save(quest))
    }

    private fun represent(quest: Quest) = QuestRepresentation(
            quest.id,
            quest.text,
            quest.expires,
            quest.options.map { OptionRepresentation(
                    it.id,
                    it.text,
                    it.voters.size,
                    it.voters.map { i -> i.characterName }.toSet()) })

    @Transactional
    open fun represent(questId: String): QuestRepresentation {
        return represent(questRepo.findOne(questId).get())
    }

    @Transactional
    open fun stringified(questId: String): String {
        val quest = questRepo.findOne(questId).get()
        val sb = StringBuilder()
        sb.append(quest.text).append("\n")
        quest.options.forEach { sb.append("\n").append(it.text).append(" : ").append(it.voters.size) }
        return sb.toString()
    }

    data class QuestRepresentation(
            val id: String,
            val text: String,
            val expires: LocalDateTime,
            val options: List<OptionRepresentation>
    )

    data class OptionRepresentation(
            val id: String,
            val text: String,
            val voted: Int,
            val voters: Set<String>
    )

    sealed class VoteResult {
        object Success : VoteResult()
        object AlreadyVoted : VoteResult()
        object OptionNotFound : VoteResult()
    }

    @Transactional
    open fun vote(pilotId: Int, optionId: String): VoteResult {
        val pilot = pilotRepo.findOne(pilotId).get()
        return optionRepo.findOne(optionId).decide({
            if (it.voters.contains(pilot)) {
                VoteResult.AlreadyVoted
            } else {
                it.voters.add(pilot)
                optionRepo.save(it)
                VoteResult.Success
            }
        },{
            VoteResult.OptionNotFound
        })
    }

}