package de.malteharms.notification.data.dataclasses

import de.malteharms.database.tables.NotificationChannel
import de.malteharms.utils.model.DateExt

data class AlarmItem(
    val channel: NotificationChannel,

    val time: DateExt,
    val title: String,
    val message: String,
)
