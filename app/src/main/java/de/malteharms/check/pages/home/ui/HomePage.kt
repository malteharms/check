package de.malteharms.check.pages.home.ui

import android.provider.ContactsContract
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun Home(
    navController: NavController
) {
    Box(
       modifier = Modifier.fillMaxSize()
    ){
        Button(onClick = { getDataFromContacts() }) {
            Text("Sync")
        }
    }
}

fun getDataFromContacts() {

    val ob = ContactsContract()
    Log.i("Hi", "HI")

}