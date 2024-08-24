package de.malteharms.pages.auth

import androidx.navigation.NavController

sealed interface AuthEvent {

    data class SetUsername(val username: String): AuthEvent
    data class SetEmail(val email: String): AuthEvent
    data class SetPassword(val password: String): AuthEvent
    data class SetConfirmPassword(val confirmPassword: String): AuthEvent
    data class Submit(val navController: NavController): AuthEvent

}
