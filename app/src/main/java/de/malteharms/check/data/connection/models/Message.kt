package de.malteharms.check.data.connection.models

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val timestamp: Long,
    val messageType: MessageType,
    val data: MessageData
)