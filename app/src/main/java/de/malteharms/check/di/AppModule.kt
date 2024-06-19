package de.malteharms.check.di

import android.content.Context
import de.malteharms.check.data.connection.KtorRealtimeMessagingClient
import de.malteharms.check.data.connection.RealtimeMessagingClient
import de.malteharms.check.data.database.CheckDatabase
import de.malteharms.check.data.notification.AndroidAlarmScheduler
import de.malteharms.check.data.provider.ContactsProvider
import de.malteharms.check.domain.AlarmScheduler
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets


interface AppModule{
    val db: CheckDatabase
    val websocketClient: RealtimeMessagingClient
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

    override val websocketClient by lazy {
        KtorRealtimeMessagingClient(
            HttpClient(CIO) {
                install(Logging)
                install(WebSockets)
            }
        )
    }

    override val notificationScheduler by lazy {
        AndroidAlarmScheduler(context)
    }

    override val contactsProvider: ContactsProvider by lazy {
        ContactsProvider(context)
    }

}
