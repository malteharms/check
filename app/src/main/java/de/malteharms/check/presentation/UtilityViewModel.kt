package de.malteharms.check.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.malteharms.check.CheckApp
import de.malteharms.database.tables.Birthday
import kotlinx.coroutines.launch

class UtilityViewModel: ViewModel() {

    companion object {
        val TAG: String? = UtilityViewModel::class.simpleName
    }

    fun syncBirthdaysFromContacts() {

        val dao: de.malteharms.database.CheckDao = CheckApp.appModule.db.itemDao()

        // TODO load from settings
        var allowedToSyncContacts = null

        if (allowedToSyncContacts == null) {
            Log.w(TAG, "Settings not yet loaded")
            return
        }

        if (allowedToSyncContacts as Nothing) {
            Log.w(TAG, "Not allowed to sync Contacts!")
            return
        }

        val birthdays: List<Birthday> = CheckApp.appModule.contactsProvider.getContactBirthdays()

        birthdays.forEach{
            // check, if birthday already exists
            val existingBirthday: de.malteharms.database.tables.Birthday? = dao.getBirthday(it.id)

            // if there is no birthday which was already saved in database,
            // create a row for for the birthday and the corresponding
            // reminder item
            if (existingBirthday == null) {
                viewModelScope.launch {
                    dao.insertBirthday(it)
                    de.malteharms.database.insertReminderItemForBirthday(dao, it)
                }
                return@forEach
            }

            // if the entry already exists, check, if the item
            // needs to be updated. For example, when the name
            // or the birthday changes
            // If the item is the same as before, continue with
            // the next birthday item

            // TODO #9
            //  load overdue from settings

            // TODD birthday from contacts is not updated in database
            val needsUpdate: Boolean = existingBirthday.birthday.hasPassed()

            // if nothing has changed, continue with the next birthday item
            if ((existingBirthday.birthday == it.birthday && existingBirthday.name == it.name) && !needsUpdate) {
                return@forEach
            }

            viewModelScope.launch {
                val linkedReminder: de.malteharms.database.tables.ReminderItem? = dao.getReminderItemForBirthdayId(it.id)
                de.malteharms.database.updateReminderItemForBirthday(dao, linkedReminder!!.id, it)
                Log.i(TAG, "Updated reminder item with ID ${linkedReminder.id} because birthday data changed")
            }
        }
    }
}
