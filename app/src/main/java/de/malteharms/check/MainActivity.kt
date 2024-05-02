package de.malteharms.check

import android.os.Bundle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import de.malteharms.check.ui.Navigation
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import de.malteharms.check.ui.theme.CheckTheme
import androidx.compose.material3.MaterialTheme
import de.malteharms.check.ui.components.TopBar
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.compose.rememberNavController
import de.malteharms.check.data.SCREEN_INNER_PADDING
import de.malteharms.check.data.getBottomNavigationItems
import de.malteharms.check.ui.components.FloatingBottomNavigation
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets


class MainActivity : ComponentActivity() {

    private val client = HttpClient(CIO) {
        install(Logging)
        install(WebSockets)
    }

    private val realtimeMessagingClient = KtorRealtimeMessagingClient(client)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CheckTheme() {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    App()
                }
            }
        }
    }
}


@Composable
fun App() {
    val navController = rememberNavController()

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { FloatingBottomNavigation( navController, getBottomNavigationItems() ) },
        modifier = Modifier.fillMaxSize()
    ) {  innerPadding ->
        Box(modifier = Modifier
            .padding(top = innerPadding.calculateTopPadding())
            .fillMaxSize()
            .padding(SCREEN_INNER_PADDING.dp)
        ) {
            Navigation(navController)
        }
    }
}


@Preview
@Composable
fun CheckWrapperPreview() { CheckTheme { App() } }

















