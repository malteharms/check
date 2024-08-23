package de.malteharms.notification.data.dataclasses


data class NotificationResult (
    val state: NotificationState,
    val notificationId: Int
)


enum class NotificationState {

    SUCCESS,

    NOTIFICATION_IS_DISABLED,


}