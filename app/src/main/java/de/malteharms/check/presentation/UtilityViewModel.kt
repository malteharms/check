package de.malteharms.check.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.malteharms.check.CheckApp
import de.malteharms.check.data.database.insertReminderItemForBirthday
import de.malteharms.check.data.database.tables.Birthday
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.data.database.updateReminderItemForBirthday
import de.malteharms.check.domain.CheckDao
import de.malteharms.check.pages.reminder.data.calculateCorrectYearOfNextBirthday
import de.malteharms.check.pages.reminder.data.checkIfBirthdayNeedsToBeUpdated
import de.malteharms.check.pages.settings.data.ReminderSettings
import de.malteharms.check.pages.settings.data.SettingValue
import kotlinx.coroutines.launch

class UtilityViewModel: ViewModel() {

    companion object {
        val TAG: String? = UtilityViewModel::class.simpleName
    }

    fun syncBirthdaysFromContacts() {

        val dao: CheckDao = CheckApp.appModule.db.itemDao()

        var allowedToSyncContacts: SettingValue? = null

        viewModelScope.launch {
            allowedToSyncContacts =
                dao.getSettingsValue(ReminderSettings.SYNC_BIRTHDAYS_THROUGH_CONTACTS)
        }

        if (allowedToSyncContacts == null) {
            Log.w(TAG, "Settings not yet loaded")
            return
        }

        if (!allowedToSyncContacts?.boolean!!) {
            Log.w(TAG, "Not allowed to sync Contacts!")
            return
        }

        val birthdays: List<Birthday> = CheckApp.appModule.contactsProvider.getContactBirthdays()

        birthdays.forEach{
            // check, if birthday already exists
            val existingBirthday: Birthday? = dao.getBirthday(it.id)

            // if there is no birthday which was already saved in database,
            // create a row for for the birthday and the corresponding
            // reminder item
            if (existingBirthday == null) {
                viewModelScope.launch {
                    dao.insertBirthday(it)
                    insertReminderItemForBirthday(dao, it)
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
            val needsUpdate: Boolean = checkIfBirthdayNeedsToBeUpdated(
                dateToReview = existingBirthday.birthday
            )

            // if nothing has changed, continue with the next birthday item
            if ((existingBirthday.birthday == it.birthday && existingBirthday.name == it.name) && !needsUpdate) {
                return@forEach
            }

            viewModelScope.launch {
                val linkedReminder: ReminderItem? = dao.getReminderItemForBirthdayId(it.id)
                updateReminderItemForBirthday(dao, linkedReminder!!.id, it)
                Log.i(TAG, "Updated reminder item with ID ${linkedReminder.id} because birthday data changed")
            }
        }
    }
}
