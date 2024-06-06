package de.malteharms.check.pages.settings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.malteharms.check.data.Screens

@Composable
fun NoAccountComponent(
    navController: NavController
) {

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ){

        Button(onClick = {
            navController.navigate(Screens.RegisterRoute.route)
        }) {
            Text(text = "Registrieren")
        }

        Button(onClick = {
            navController.navigate(Screens.LoginRoute.route)
        }) {
            Text(text = "Anmelden")
        }

    }

}