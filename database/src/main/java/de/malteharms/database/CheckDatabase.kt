package de.malteharms.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.malteharms.database.converter.DateExtConverter
import de.malteharms.database.converter.NotificationChannelConverter
import de.malteharms.database.tables.Birthday
import de.malteharms.database.tables.ReminderItem
import de.malteharms.database.tables.NotificationItem
import de.malteharms.database.tables.todo.TodoGroup
import de.malteharms.database.tables.todo.TodoItem
import de.malteharms.database.tables.todo.TodoLabel
import de.malteharms.database.tables.todo.TodoPriority


@Database(
    entities = [
        ReminderItem::class,
        Birthday::class,
        NotificationItem::class,
        TodoItem::class,
        TodoGroup::class,
        TodoLabel::class,
        TodoPriority::class,
    ],
    version = 25,
    exportSchema = false
)
@TypeConverters(
    value = [
        DateExtConverter::class,
        NotificationChannelConverter::class,
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
