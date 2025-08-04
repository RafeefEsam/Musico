package com.example.musico.presentation.splash

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.musico.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    var showGif by remember { mutableStateOf(true) }
    var permissionGranted by remember { mutableStateOf<Boolean?>(null) }
    var hasRequestedPermission by remember { mutableStateOf(false) }

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components { add(GifDecoder.Factory()) }
            .build()
    }

    val imageRequest = remember {
        ImageRequest.Builder(context)
            .data(R.drawable.splash_animated)
            .build()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        permissionGranted = allGranted
        hasRequestedPermission = true
    }

    // Show animated GIF for 5 seconds, then show static image and request permissions
    LaunchedEffect(Unit) {
        delay(3500L)
        showGif = false
        // Request permissions after showing static image
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        permissionLauncher.launch(permissions)
    }

    // Navigate after permission result (both granted and denied)
    LaunchedEffect(permissionGranted) {
        if (permissionGranted != null) {
            // Navigate regardless of permission result
            // MediaListScreen will handle showing permission warning if denied
            navController.navigate("main") {
                popUpTo("splash") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Box(contentAlignment = Alignment.Center) {
        if (showGif) {
            // 🎬 Show GIF once
            AsyncImage(
                model = imageRequest,
                contentDescription = "Splash Screen",
                imageLoader = imageLoader,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillHeight
            )
        } else {
            // Show static image and handle permission states
            Image(
                painter = painterResource(id = R.drawable.musico),
                contentDescription = "App Logo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Show loading indicator while waiting for permission response
            if (hasRequestedPermission && permissionGranted == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    val navController = rememberNavController()
    SplashScreen(navController)
}