package de.malteharms.check.data.provider

import android.net.Uri
import android.content.Context
import android.provider.ContactsContract
import de.malteharms.check.data.database.tables.Birthday
import java.time.LocalDate

class ContactsProvider(
    private val context: Context
) {

    fun getContactBirthdays(): List<Birthday> {
        val contentResolver = context.contentResolver

        val uri: Uri = ContactsContract.Data.CONTENT_URI

        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Event.START_DATE
        )

        // the user needs to set the birthday field
        // if the user sets a custom field, this will not be supported!
        val selection = ContactsContract.Data.MIMETYPE + "= ? AND " + ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY
        val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)

        val contacts = mutableListOf<Birthday>()

        contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            val cIdColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val bDayColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
            val nameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val cId: Long = cursor.getLong(cIdColumn)
                val bDay: String = cursor.getString(bDayColumn)
                val name: String = cursor.getString(nameColumn)

                val bDaySplit = bDay.split('-')

                // it could happen, that the user does not provide a year for
                // a contacts birthday. If the year is not present, it will be
                // set to the current years date
                // The format, when no year is passed, is "--MM-dd"
                val noYearPassed: Boolean = bDaySplit.size == 4 && bDaySplit[0] == ""

                val year: Int = if (noYearPassed) {
                    LocalDate.now().atStartOfDay().year
                } else bDaySplit[0].toInt()

                val monthIndex: Int = if (noYearPassed) 2 else 1
                val month: Int = bDaySplit[monthIndex].toInt()

                val dayIndex: Int = if (noYearPassed) 3 else 2
                val day: Int = bDaySplit[dayIndex].toInt()

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
