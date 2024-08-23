package de.malteharms.check.pages.auth.login.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import de.malteharms.pages.components.presentation.CustomTextField

@Composable
fun LoginPage(
    navController: NavController
) {

    val textFieldModifier: Modifier = Modifier
        .border(BorderStroke(2.dp, MaterialTheme.colorScheme.primary), shape = CircleShape)
        .padding(horizontal = 30.dp, vertical = 10.dp)

    var email: String by remember {
        mutableStateOf("")
    }

    var password: String by remember {
        mutableStateOf("")
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Login",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 35.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(50.dp))

                CustomTextField(
                    modifier = textFieldModifier,
                    value = email,
                    fontSize = 15.sp,
                    placeholderText = "E-Mail",
                    onValueChange = { newValue -> email = newValue }
                )

                CustomTextField(
                    modifier = textFieldModifier,
                    value = email,
                    fontSize = 15.sp,
                    placeholderText = "Password",
                    onValueChange = { newValue -> password = newValue }
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(onClick = {
                    navController.navigate(de.malteharms.pages.components.data.Screens.HomeRoute.route)
                }) {
                    Text(text = "kostenlos Registrieren")
                }

            }

        }
    }

}


@Preview
@Composable
fun LoginPagePreview() {
    LoginPage(rememberNavController())
}