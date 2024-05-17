package de.malteharms.check.data.provider

import android.net.Uri
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import de.malteharms.check.data.database.tables.Birthday
import java.time.LocalDate
import java.time.LocalDateTime

class ContactsProvider(
    private val context: Context
) {

    fun getContactBirthdays(): List<Birthday> {
        val contentResolver = context.contentResolver

        val uri: Uri = ContactsContract.Data.CONTENT_URI;

        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Event.START_DATE
        )

        val selection = ContactsContract.Data.MIMETYPE + "= ? AND " + ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY;
        val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
        val sortOrder = null;

        val contacts = mutableListOf<Birthday>()

        contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val cIdColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val bDayColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
            val nameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val cId: Long = cursor.getLong(cIdColumn)
                val bDay: String = cursor.getString(bDayColumn)
                val name: String = cursor.getString(nameColumn)

                val bDaySplit = bDay.split('-')
                val year: Int = bDaySplit[0].toInt()
                val month: Int = bDaySplit[1].toInt()
                val day: Int = bDaySplit[2].toInt()

                contacts.add(Birthday(
                    id = cId,
                    name = name,
                    birthday = LocalDate.of(year, month, day).atStartOfDay()
                ))
            }
        }

        return contacts
    }

}
