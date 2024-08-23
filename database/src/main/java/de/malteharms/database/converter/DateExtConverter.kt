package de.malteharms.database.converter

import androidx.room.TypeConverter
import de.malteharms.utils.model.DateExt

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
