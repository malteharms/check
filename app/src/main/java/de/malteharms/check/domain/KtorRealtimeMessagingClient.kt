package de.malteharms.check.domain

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow

const val HOST_IP = "192.168.178.28"
const val HOST_PORT = 8080
const val PATH = "app"

class KtorRealtimeMessagingClient (
    private val client: HttpClient
): RealtimeMessagingClient {

    private var session: WebSocketSession? = null

    override fun getSocketStream(): Flow<CostResultWrapper> {
        return flow {
            session = client.webSocketSession {
                url("ws://$HOST_IP:$HOST_PORT/$PATH")
            }

            val costStates = session!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .mapNotNull { Json.decodeFromString<CostResultWrapper>(it.readText()) }
                .onEach { costsResultWrapper ->
                    Log.i("KtorRealtimeMessagingClient", "Receiving message from websocket server")
                    Log.i("KtorRealtimeMessagingClient", costsResultWrapper.toString())
                }

            emitAll(costStates)
        }
    }

    override suspend fun sendAddItem(item: CostsAddItemOutgoingMessage) {
        Log.i("CostPage", "Sending item to websocket server")
        Log.i("CostPage", Json.encodeToString(item))

        session?.outgoing?.send(
            Frame.Text(Json.encodeToString(item))
        )
    }

    override suspend fun close() {
        session?.close()
        session = null
    }


}