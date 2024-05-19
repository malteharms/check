package de.malteharms.check.domain

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import de.malteharms.check.data.database.tables.Birthday
import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.data.database.tables.ReminderNotification
import de.malteharms.check.data.database.tables.Setting
import de.malteharms.check.pages.settings.data.SettingValue
import de.malteharms.check.pages.settings.domain.AnySetting
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

    @Query("SELECT * FROM reminder_items ORDER BY dueDate ASC")
    fun getAllReminderItems(): Flow<List<ReminderItem>>

    @Query("SELECT * FROM reminder_items ORDER BY dueDate ASC LIMIT :limit")
    fun getAllReminderItemsLimited(limit: Int): Flow<List<ReminderItem>>

    @Query("SELECT * FROM reminder_items WHERE category IN (:categories) ORDER BY dueDate ASC")
    fun getFilteredReminderItems(categories: List<ReminderCategory>): Flow<List<ReminderItem>>

    @Query("SELECT * FROM reminder_items WHERE category IN (:categories) ORDER BY dueDate ASC LIMIT :limit")
    fun getFilteredReminderItemsLimited(categories: List<ReminderCategory>, limit: Int): Flow<List<ReminderItem>>

    @Query("SELECT * FROM reminder_items WHERE birthdayRelation = :birthdayId LIMIT 1")
    fun getReminderItemForBirthdayId(birthdayId: Long): ReminderItem?

    /* REMINDER NOTIFICATION QUERY'S */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReminderNotification(reminderNotification: ReminderNotification)

    @Delete
    suspend fun removeReminderNotification(reminderNotification: ReminderNotification)

    @Query("DELETE FROM reminder_notifications WHERE reminderItem = :reminderItemId")
    fun removeReminderNotificationsForReminderItem(reminderItemId: Long)

    @Query("SELECT * FROM reminder_notifications WHERE reminderItem = :itemId ORDER BY notificationDate")
    fun getNotificationsForReminderItem(itemId: Long): List<ReminderNotification>

    /* SETTINGS QUERY'S */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSetting(setting: Setting)

    @Update
    suspend fun updateSetting(setting: Setting)

    @Delete
    suspend fun deleteSetting(setting: Setting)

    @Query("SELECT value FROM settings WHERE item = :item")
    fun getSettingsValue(item: AnySetting): SettingValue

    @Insert
    suspend fun insertBirthday(item: Birthday)

    @Query("SELECT * FROM birthdays WHERE id = :birthdayId")
    fun getBirthday(birthdayId: Long): Birthday?
}
