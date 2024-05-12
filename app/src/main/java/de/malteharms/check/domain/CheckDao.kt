package de.malteharms.check.domain

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.malteharms.check.pages.reminder.data.database.ReminderItem
import de.malteharms.check.pages.reminder.data.database.ReminderNotification
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckDao {

    /* REMINDER ITEM QUERY'S */
    @Insert
    suspend fun insertReminderItem(reminderItem: ReminderItem): Long

    @Update
    suspend fun updateReminderItem(reminderItem: ReminderItem)

    @Delete
    suspend fun removeReminderItem(reminderItem: ReminderItem)

    @Query("SELECT * FROM reminder_items ORDER BY title ASC")
    fun getReminderItemsOrderedByTitle(): Flow<List<ReminderItem>>

    @Query("SELECT * FROM reminder_items ORDER BY dueDate ASC")
    fun getReminderItemsOrderedByDueDate(): Flow<List<ReminderItem>>

    /* REMINDER NOTIFICATION QUERY'S */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReminderNotification(reminderNotification: ReminderNotification)

    @Delete
    suspend fun removeReminderNotification(reminderNotification: ReminderNotification)

    @Query("DELETE FROM reminder_notifications WHERE reminderItem = :reminderItemId")
    fun removeReminderNotificationsForReminderItem(reminderItemId: Long)

    @Query("SELECT * FROM reminder_notifications WHERE reminderItem = :itemId ORDER BY notificationDate")
    fun getNotificationsForReminderItem(itemId: Long): List<ReminderNotification>

}
