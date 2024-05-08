package de.malteharms.check.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import de.malteharms.check.pages.reminder.data.ReminderItem


@Database(
    entities = [ReminderItem::class],
    version = 5,
    exportSchema = false
)
abstract class CheckDatabase: RoomDatabase() {
    abstract fun itemDao(): CheckDao

    companion object {
        @Volatile
        private var Instance: CheckDatabase? = null

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create the new table with the added column
                db.execSQL(
                    "ALTER TABLE reminder_items ADD COLUMN notification TEXT"
                )
            }
        }

        fun getDatabase(context: Context): CheckDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CheckDatabase::class.java, "check_database.db")
                    .build()
                    .also { Instance = it }
            }
        }
    }

}
