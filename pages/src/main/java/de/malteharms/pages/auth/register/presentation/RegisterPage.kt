package de.malteharms.pages.auth.register.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import de.malteharms.pages.R
import de.malteharms.pages.auth.AuthEvent
import de.malteharms.pages.auth.AuthState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPage(
    navController: NavController,
    state: AuthState,
    onEvent: (AuthEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold { paddingValues ->  
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        // hide the keyboard when tapping outside of the text fields
                        detectTapGestures(onTap = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        })
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Account erstellen",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 35.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Mit einem Account kannst du Informationen mit Freunden teilen, deine Daten synchronisieren und vieles mehr!",
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Clip,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light
                )

                Spacer(modifier = Modifier.height(30.dp))

                val borderWidth = 2.dp

                Image(
                    painter = painterResource(id = R.drawable.default_profile_picture),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .border(
                            BorderStroke(borderWidth, MaterialTheme.colorScheme.primary),
                            CircleShape
                        )
                        .padding(borderWidth)

                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    shape = RoundedCornerShape(10.dp),
                    value = state.username,
                    onValueChange = { onEvent(AuthEvent.SetUsername(it)) },
                    label = { Text("Nutzername", color = MaterialTheme.colorScheme.onBackground) },
                    singleLine = true,
                    isError = state.usernameEmpty
                )

                OutlinedTextField(
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    shape = RoundedCornerShape(10.dp),
                    value = state.email,
                    onValueChange = { onEvent(AuthEvent.SetEmail(it)) },
                    label = { Text("EMail", color = MaterialTheme.colorScheme.onBackground) },
                    singleLine = true,
                    isError = state.emailEmpty || state.emailInvalid
                )

                OutlinedTextField(
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    shape = RoundedCornerShape(10.dp),
                    value = state.password,
                    onValueChange = { onEvent(AuthEvent.SetPassword(it)) },
                    label = { Text("Passwort", color = MaterialTheme.colorScheme.onBackground) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = state.passwordEmpty || state.passwordTooWeak
                )

                OutlinedTextField(
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    shape = RoundedCornerShape(10.dp),
                    value = state.confirmPassword,
                    onValueChange = { onEvent(AuthEvent.SetConfirmPassword(it)) },
                    label = { Text("Passwort wiederholen", color = MaterialTheme.colorScheme.onBackground) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = state.confirmPasswordEmpty || state.confirmPasswordInvalid
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    shape = RoundedCornerShape(10.dp),
                    onClick = { onEvent(AuthEvent.Submit(navController)) }
                ) {
                    Text(text = "kostenlos Registrieren", color = MaterialTheme.colorScheme.onPrimary)
                }

            }
            
        }
    }
    
}
