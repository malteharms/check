package de.malteharms.check.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import de.malteharms.check.pages.reminder.data.ReminderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckDao {

    /* REMINDER ITEM QUERY'S */
    @Insert
    suspend fun insertReminderItem(reminderItem: ReminderItem)

    @Update
    suspend fun updateReminderItem(reminderItem: ReminderItem)

    @Delete
    suspend fun removeReminderItem(reminderItem: ReminderItem)

    @Query("SELECT * FROM reminder_items ORDER BY title ASC")
    fun getReminderItemsOrderedByTitle(): Flow<List<ReminderItem>>

    @Query("SELECT * FROM reminder_items ORDER BY dueDate ASC")
    fun getReminderItemsOrderedByDueDate(): Flow<List<ReminderItem>>
}
