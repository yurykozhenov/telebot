package ru.finnetrolle.telebot.config

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
import javax.persistence.AttributeConverter
import javax.persistence.Converter
import java.sql.Timestamp
import java.time.LocalDateTime

@Converter(autoApply = true)
class DateTimeConverter : AttributeConverter<LocalDateTime, Timestamp> {

    override fun convertToDatabaseColumn(localDateTime: LocalDateTime?): Timestamp? {
        return if (localDateTime != null) Timestamp.valueOf(localDateTime) else null
    }

    override fun convertToEntityAttribute(timestamp: Timestamp?): LocalDateTime? {
        return timestamp?.toLocalDateTime()
    }
}