package de.malteharms.check.domain

import kotlinx.coroutines.flow.Flow

interface RealtimeMessagingClient {

    fun getSocketStream(): Flow<CostResultWrapper>
    suspend fun sendAddItem(item: CostsAddItemOutgoingMessage)
    suspend fun close()

}