package de.malteharms.check.data.connection.models

import kotlinx.serialization.Serializable

@Serializable
enum class MessageType {
    // == AUTHENTICATION == //
    REGISTER,
    LOGIN,

    // == REMINDER == //
    REMINDER_ADD,
    REMINDER_UPDATE,
    REMINDER_REMOVE,
    REMINDER_GET_ALL,

}
