package de.malteharms.check.di

import android.content.Context
import de.malteharms.check.data.provider.ContactsProvider
import de.malteharms.database.CheckDatabase
import de.malteharms.notification.data.AndroidAlarmScheduler
import de.malteharms.notification.domain.AlarmScheduler


interface AppModule{
    val db: CheckDatabase
    val notificationScheduler: AlarmScheduler
    val contactsProvider: ContactsProvider
}

class AppModuleImpl(
    context: Context
) : AppModule {

    companion object {
        val TAG: String? = AppModuleImpl::class.simpleName
    }

    override val db by lazy {
        CheckDatabase.getDatabase(context)
    }

    override val notificationScheduler by lazy {
        AndroidAlarmScheduler(context)
    }

    override val contactsProvider: ContactsProvider by lazy {
        ContactsProvider(context.contentResolver)
    }

}
