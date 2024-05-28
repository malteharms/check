package de.malteharms.check.data.utils

import de.malteharms.check.CheckApp
import de.malteharms.check.data.database.converter.LocalDateTimeConverter
import de.malteharms.check.data.database.tables.NotificationItem
import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.data.notification.NotificationHandler
import de.malteharms.check.data.notification.dataclasses.NotificationChannel
import de.malteharms.check.domain.CheckDao
import de.malteharms.check.domain.Notificationable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate


