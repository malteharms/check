package de.malteharms.check.domain

import de.malteharms.check.data.NotificationResult
import de.malteharms.check.data.notification.dataclasses.AlarmItem
import kotlin.random.Random

interface AlarmScheduler {

    fun schedule(notificationId: Int?, item: AlarmItem): NotificationResult
    fun cancel(nId: Int): NotificationResult

}