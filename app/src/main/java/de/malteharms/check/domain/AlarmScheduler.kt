package de.malteharms.check.domain

import de.malteharms.check.data.notification.dataclasses.NotificationResult
import de.malteharms.check.data.notification.dataclasses.AlarmItem

interface AlarmScheduler {

    fun schedule(notificationId: Int?, item: AlarmItem): NotificationResult
    fun cancel(nId: Int): NotificationResult

}