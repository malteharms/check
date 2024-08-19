package de.malteharms.check.data.notification.dataclasses

import de.malteharms.utils.model.DateExt

data class AlarmItem(
    val channel: NotificationChannel,

    val time: DateExt,
    val title: String,
    val message: String,
)
