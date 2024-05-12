package de.malteharms.check.data


data class NotificationResult (
    val state: NotificationState,
    val notificationId: Int
)


enum class NotificationState {

    SUCCESS,

    NOTIFICATION_IS_DISABLED,


}