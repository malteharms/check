package de.malteharms.check.pages.auth

import androidx.lifecycle.ViewModel
import de.malteharms.check.data.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel: ViewModel() {

    val state = MutableStateFlow(AuthState())


    fun onEvent(event: AuthEvent) {
        when(event) {
            is AuthEvent.SetUsername -> {
                state.update { it.copy(
                    username = event.username
                ) }
            }
            is AuthEvent.SetEmail -> {
                state.update { it.copy(
                    email = event.email
                ) }
            }
            is AuthEvent.SetPassword -> {
                state.update { it.copy(
                    password = event.password
                ) }
            }
            is AuthEvent.SetConfirmPassword -> {
                state.update { it.copy(
                    confirmPassword = event.confirmPassword
                ) }
            }

            is AuthEvent.Submit -> {
                // client validation
                val validationResult: ValidationResult = validateInput()
                if (validationResult == ValidationResult.DECLINED) return

                // todo send to server
                // todo server validation

                // navigate to next screen
                event.navController.navigate(de.malteharms.pages.components.data.Screens.HomeRoute.route)
            }
        }
    }

    private fun validateInput(): ValidationResult {

        val isDeclined: Boolean = listOf(
            validateUsername(),
            validateEmail(),
            validatePassword(),
            validateConfirmPassword()
        ).any { it == ValidationResult.DECLINED }

        return if (isDeclined) ValidationResult.DECLINED else ValidationResult.SUCCESS
    }

    private fun validateUsername(): ValidationResult {
        val username = state.value.username

        if (username.isBlank()) {
            state.update { it.copy(usernameEmpty = true)}
            return ValidationResult.DECLINED
        }
        return ValidationResult.SUCCESS
    }

    private fun validateEmail(): ValidationResult {
        val email = state.value.email

        if (email.isBlank()) {
            state.update { it.copy(emailEmpty = true)}
            return ValidationResult.DECLINED
        }

        if (!email.contains('@') && !email.contains('.')) {
            state.update { it.copy(emailInvalid = true)}
            return ValidationResult.DECLINED
        }

        return ValidationResult.SUCCESS
    }

    private fun validatePassword(): ValidationResult {
        val password = state.value.password

        if (password.isBlank()) {
            state.update { it.copy(passwordEmpty = true)}
            return ValidationResult.DECLINED
        }

        if (!password.isStrongPassword()) {
            state.update { it.copy(passwordTooWeak = true)}
            return ValidationResult.DECLINED
        }

        return ValidationResult.SUCCESS
    }

    private fun validateConfirmPassword(): ValidationResult {
        val password = state.value.password
        val confirmPassword = state.value.confirmPassword

        if (confirmPassword.isBlank()) {
            state.update { it.copy(confirmPasswordEmpty = true)}
            return ValidationResult.DECLINED
        }

        if (confirmPassword != password) {
            state.update { it.copy(confirmPasswordInvalid = true)}
            return ValidationResult.DECLINED
        }

        return ValidationResult.SUCCESS
    }

}

fun String.isStrongPassword(): Boolean {
    val hasUpperCase = any { it.isUpperCase() }
    val hasLowerCase = any { it.isLowerCase() }
    val hasDigit = any { it.isDigit() }
    val hasSpecialChar = any { !it.isLetterOrDigit() }
    return length >= 8 && hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar
}