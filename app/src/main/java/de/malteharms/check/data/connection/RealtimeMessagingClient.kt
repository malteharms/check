package de.malteharms.check.data.connection

import de.malteharms.check.data.connection.models.Message
import kotlinx.coroutines.flow.Flow

interface RealtimeMessagingClient {

    fun getSocketStream(): Flow<Message>
    suspend fun send(item: Message)
    suspend fun close()

}