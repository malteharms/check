package de.malteharms.check.domain

import de.malteharms.check.data.NotificationResult
import de.malteharms.check.data.notification.dataclasses.AlarmItem

interface AlarmScheduler {

    fun schedule(item: AlarmItem): NotificationResult
    fun cancel(nId: Int): NotificationResult

}