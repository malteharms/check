package de.malteharms.check.data.provider

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import de.malteharms.database.tables.Birthday
import de.malteharms.utils.model.DateExt
import java.time.LocalDate


class ContactsProvider(
    private val contentResolver: ContentResolver
) {

    fun getBirthdaysFromContacts(): List<Birthday> {
        /* To load the contacts from storage the android framework provides a
         * ContentProvider, which can query relevant information from an database.
         */
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Event.START_DATE
        )

        // build the selection in form of a SQL query
        val isContact = "${ContactsContract.Data.MIMETYPE} = ?"
        val isBirthday = "${ContactsContract.CommonDataKinds.Event.TYPE} = ${ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY}"
        val selection = "$isContact AND $isBirthday"

        // the argument specified with the '?' is provided via the following array
        val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)

        val contacts = mutableListOf<Birthday>()
        val uri: Uri = ContactsContract.Data.CONTENT_URI

        // perform the database query
        contentResolver.query(uri, projection, selection, selectionArgs, null)?.use { cursor ->

            // define the index of the column for each projection field
            val cIdColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val bDayColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
            val nameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

            // loop over the results
            while (cursor.moveToNext()) {
                // extract the data fields from the current row
                val cId: Long = cursor.getLong(cIdColumn)
                val bDay: String = cursor.getString(bDayColumn)
                val name: String = cursor.getString(nameColumn)

                // extract information from the provided date
                val bDaySplit = bDay.split('-')

                /* The user has the opportunity to leave the year of the birthday
                 * blank. The date would then be stored in form "--MM-dd"
                */

                val noYearPassed: Boolean = bDaySplit.size == 4 && bDaySplit[0] == ""

                val year: Int = if (noYearPassed) {
                    LocalDate.now().atStartOfDay().year
                } else bDaySplit[0].toInt()

                val monthIndex: Int = if (noYearPassed) 2 else 1
                val month: Int = bDaySplit[monthIndex].toInt()

                val dayIndex: Int = if (noYearPassed) 3 else 2
                val day: Int = bDaySplit[dayIndex].toInt()

                contacts.add(
                    Birthday(id = cId, name = name, birthday = DateExt.from(year, month, day))
                )
            }
        }
        return contacts
    }
}
