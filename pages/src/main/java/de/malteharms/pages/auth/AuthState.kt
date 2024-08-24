package de.malteharms.pages.auth

data class AuthState (
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    val usernameEmpty: Boolean = false,
    val emailEmpty: Boolean = false,
    val emailInvalid: Boolean = false,
    val passwordEmpty: Boolean = false,
    val passwordTooWeak: Boolean = false,
    val confirmPasswordEmpty: Boolean = false,
    val confirmPasswordInvalid: Boolean = false,

)