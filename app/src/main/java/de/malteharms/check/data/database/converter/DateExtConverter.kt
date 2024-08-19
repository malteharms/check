package de.malteharms.check.data.database.converter

import androidx.room.TypeConverter
import de.malteharms.utils.model.DateExt
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class DateExtConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): DateExt? {
        return value?.let {
            DateExt.from(it)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: DateExt?): Long? {
        return date?.toTimestamp()
    }
}
