package ru.finnetrolle.telebot.util

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
object CommandParser {

    data class QuestCommand(val text: String, val options: List<String>, val mins: Long, val groupName: String)

    fun parseQuestData(data: String): QuestCommand {
        var res: Variable = cut(data)
        var options = listOf<String>()
        var text = ""
        var mins = 15L
        var groupName = "ALL"
        var counter = 0
        do {
            when (res) {
                is Variable.Options -> options = res.options
                is Variable.Time -> mins = res.minutes
                is Variable.Group -> groupName = res.name
                is Variable.Text -> text = res.text
            }
            if (res is Variable.Text) {
                return QuestCommand(text, options, mins, groupName)
            }
            res = cut(res.other)
            counter ++
        } while (counter < 5)
        return QuestCommand(text, options, mins, groupName)
    }

    sealed class Variable(val other: String) {
        class Group(val name: String, other: String) : Variable(other)
        class Time(val minutes: Long, other: String) : Variable(other)
        class Options(val options: List<String>, other: String) : Variable(other)
        class Text(val text: String) : Variable("")
    }

    private fun cut(data: String) : Variable {
        if (data.first() == '[') {
            val closed = data.indexOfFirst { it == ']' }
            if (closed != -1) {
                return Variable.Options(data.substring(1, closed).split("|"), data.substring(closed + 1, data.length))
            }
        }
        val gain = data.substringBefore(" ")
        try {
            val minutes = gain.toLong()
            return Variable.Time(minutes, data.substringAfter(" "))
        } catch (e : NumberFormatException) { }
        if (data.substringAfter(" ").contains('[')) {
            return Variable.Group(gain, data.substringAfter(" "))
        } else {
            return Variable.Text(data.substring(1, data.length))
        }
    }

}