package de.malteharms.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.malteharms.database.converter.DateExtConverter
import de.malteharms.database.tables.Birthday
import de.malteharms.database.tables.reminder.ReminderCategory
import de.malteharms.database.tables.reminder.ReminderItem

@Database(
    entities = [
        Birthday::class,
        ReminderItem::class,
        ReminderCategory::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [
        DateExtConverter::class
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
