package de.malteharms.check.data.internal


data class NotificationResult (
    val state: NotificationState,
    val notificationId: Int
)


enum class NotificationState {

    SUCCESS,

    NOTIFICATION_IS_DISABLED,


}