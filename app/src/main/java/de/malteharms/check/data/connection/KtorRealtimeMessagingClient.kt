package de.malteharms.check.data.connection

import android.util.Log
import de.malteharms.check.data.connection.models.Message
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val HOST_IP = "192.168.178.28"
const val HOST_PORT = 8080
const val PATH = "app"

class KtorRealtimeMessagingClient (
    private val client: HttpClient
): RealtimeMessagingClient {

    companion object {
        val TAG: String? = KtorRealtimeMessagingClient::class.java.simpleName
    }

    private var session: WebSocketSession? = null

    override fun getSocketStream(): Flow<Message> {
        return flow {
            session = client.webSocketSession {
                url("ws://$HOST_IP:$HOST_PORT/$PATH")
            }

            val costStates = session!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .mapNotNull { Json.decodeFromString<Message>(it.readText()) }
                .onEach { message ->
                    Log.i(TAG, "Receiving ${message.messageType} message from websocket server")
                    // TODO handle message
                }

            emitAll(costStates)
        }
    }

    override suspend fun send(item: Message) {
        Log.i(TAG, "Sending ${item.messageType} to websocket server")
        Log.i(TAG, Json.encodeToString(item))

        session?.outgoing?.send(
            Frame.Text(Json.encodeToString(item))
        )
    }

    override suspend fun close() {
        session?.close()
        session = null
    }


}