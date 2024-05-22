package de.malteharms.check.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.malteharms.check.data.database.converter.AnySettingConverter
import de.malteharms.check.data.database.converter.LocalDateTimeConverter
import de.malteharms.check.data.database.converter.SettingValueConverter
import de.malteharms.check.data.database.tables.Birthday
import de.malteharms.check.domain.CheckDao
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.data.database.tables.ReminderNotification
import de.malteharms.check.data.database.tables.Setting


@Database(
    entities = [
        ReminderItem::class,
        Birthday::class,
        ReminderNotification::class,
        Setting::class
    ],
    version = 20,
    exportSchema = false
)
@TypeConverters(
    value = [
        LocalDateTimeConverter::class, AnySettingConverter::class, SettingValueConverter::class
    ]
)
abstract class CheckDatabase: RoomDatabase() {
    abstract fun itemDao(): CheckDao

    companion object {
        @Volatile
        private var Instance: CheckDatabase? = null

        fun getDatabase(context: Context): CheckDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CheckDatabase::class.java, "check_database.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                    .also { Instance = it }
            }
        }
    }

}
