package de.malteharms.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import de.malteharms.database.tables.Birthday
import de.malteharms.database.tables.NotificationChannel
import de.malteharms.database.tables.NotificationItem
import de.malteharms.database.tables.reminder.ReminderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckDao {

    // BIRTHDAY QUERIES
    @Insert
    suspend fun insertBirthday(item: Birthday)

    @Query("SELECT * FROM birthdays WHERE id = :birthdayId")
    fun getBirthday(birthdayId: Long): Birthday?

    // REMINDER QUERIES
    @Upsert
    suspend fun upsertReminder(item: ReminderItem): Long

    @Delete
    suspend fun removeReminder(item: ReminderItem)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun removeReminderById(id: Long)

    @Query("SELECT * FROM reminders WHERE due > :today ORDER BY due ASC LIMIT :limit")
    fun getAllReminders(limit: Int?, today: Long): Flow<List<ReminderItem>>

    @Query("SELECT * FROM reminders WHERE id = :reminderId")
    fun getReminder(reminderId: Long): ReminderItem?

    // NOTIFICATION QUERIES
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotification(notification: NotificationItem)

    @Delete
    suspend fun removeNotification(notification: NotificationItem)

    @Query("SELECT * FROM notifications")
    fun getAllNotifications(): List<NotificationItem>

    @Query("DELETE FROM notifications WHERE connectedItem = :connectedItemId AND channel = :channel")
    fun removeNotificationsForConnectedItem(connectedItemId: Long)

    @Query("SELECT * FROM notifications WHERE connectedItem = :itemId ORDER BY notificationDate")
    fun getNotificationsForConnectedItem(itemId: Long): List<NotificationItem>

}
