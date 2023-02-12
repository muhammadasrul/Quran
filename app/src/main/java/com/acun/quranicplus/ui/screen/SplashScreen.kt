package com.acun.quranicplus.ui.screen

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.acun.quranicplus.R
import com.acun.quranicplus.ui.theme.Mariner
import com.acun.quranicplus.ui.theme.Poppins
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashScreen(
    onPermissionGranted: () -> Unit,
    onCloseClicked: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    when {
        permissionsState.allPermissionsGranted -> {
            LaunchedEffect(key1 = true) {
                delay(1000)
                onPermissionGranted()
            }
        }
        else -> {
            AlertDialog(
                backgroundColor = MaterialTheme.colors.surface,
                text = {
                    Text(text = "Location permission required for this feature to be available. Please grant the permission")
                },
                onDismissRequest = { /*TODO*/ },
                confirmButton = {
                    Button(
                        onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${context.packageName}"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Mariner
                        )
                    ) {
                        Text(
                            text = "Open Setting",
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                },
                dismissButton = {
                    Button(onClick = {
                            onCloseClicked()
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Mariner
                        )
                    ) {
                        Text(
                            text = "Close",
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }
            )
        }
    }

    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionsState.launchMultiplePermissionRequest()
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    Scaffold(
        backgroundColor = MaterialTheme.colors.surface
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = painterResource(id = R.drawable.splash_screen_bg),
                contentDescription = null
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(72.dp),
                    contentScale = ContentScale.Fit,
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = stringResource(id = R.string.app_name)
                )
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = stringResource(id = R.string.app_name),
                    color = Mariner,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Poppins,
                    fontSize = 22.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(onPermissionGranted = {}, onCloseClicked = {})
}