package de.malteharms.database.converter

import androidx.room.TypeConverter
import de.malteharms.database.tables.NotificationChannel

class NotificationChannelConverter {

    @TypeConverter
    fun fromNotificationChannel(channel: NotificationChannel): String {
        return channel.name.lowercase()
    }

    @TypeConverter
    fun toNotificationChannel(name: String): NotificationChannel {
        return NotificationChannel.valueOf(name.uppercase())
    }


}