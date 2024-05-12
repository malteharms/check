package de.malteharms.check.data.notification.dataclasses

import java.time.LocalDateTime

data class AlarmItem(
    val channel: NotificationChannel,

    val time: LocalDateTime,
    val title: String,
    val message: String,
)
