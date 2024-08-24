package de.malteharms.notification.domain

import de.malteharms.notification.data.dataclasses.AlarmItem
import de.malteharms.notification.data.dataclasses.NotificationResult

interface AlarmScheduler {

    fun schedule(notificationId: Int?, item: AlarmItem): NotificationResult
    fun cancel(nId: Int): NotificationResult

}