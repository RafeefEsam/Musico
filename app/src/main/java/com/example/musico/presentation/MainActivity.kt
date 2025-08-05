package com.example.musico.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.musico.presentation.media.MediaListScreen
import com.example.musico.presentation.player.PlayerScreen
import com.example.musico.presentation.splash.SplashScreen
import com.example.musico.ui.theme.MusicoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        private const val REQUEST_NOTIFICATION_PERMISSION = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_NOTIFICATION_PERMISSION
            )
        }
        enableEdgeToEdge()
        setContent {
            MusicoTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") {
                        SplashScreen(navController = navController)
                    }
                    composable("main") {
                        MediaListScreen(navController = navController)
                    }
                    composable(
                        route = "player/{audioFileId}?position={position}",
                        arguments = listOf(
                            navArgument("audioFileId") {
                                type = NavType.StringType
                                defaultValue = "0"
                            },
                            navArgument("position") {
                                type = NavType.LongType
                                defaultValue = 0L
                            }
                        ),
                        deepLinks = listOf(
                            navDeepLink {
                                uriPattern = "musico://player?id={audioFileId}&position={position}"
                            }
                        )
                    ) { backStackEntry ->
                        val audioFileId = backStackEntry.arguments?.getString("audioFileId") ?: "0"
                        val position = backStackEntry.arguments?.getLong("position") ?: 0L
                        PlayerScreen(
                            navController = navController,
                            audioFileId = audioFileId,
                            initialPosition = position
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MusicoTheme {
        Greeting("Android")
    }
}