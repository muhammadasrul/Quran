package com.acun.quranicplus.ui.screen.quran.share

import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.acun.quranicplus.BuildConfig
import com.acun.quranicplus.R
import com.acun.quranicplus.data.remote.response.surah.Verse
import com.acun.quranicplus.ui.component.TopBarComponent
import com.acun.quranicplus.ui.theme.BigStone
import com.acun.quranicplus.ui.theme.Dune
import com.acun.quranicplus.ui.theme.Misbah
import com.acun.quranicplus.ui.theme.Poppins
import com.acun.quranicplus.ui.theme.shareBackgroundList
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@Composable
fun ShareScreen(
    verse: Verse?,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val systemUiController = rememberSystemUiController()
    val captureController = rememberCaptureController()
    val coroutineScope = rememberCoroutineScope()
    var roundedCornerSize by remember { mutableStateOf(18.dp) }

    var primaryColor by remember { mutableStateOf(Color.White) }
    var secondaryColor by remember { mutableStateOf(Color.Black) }

    var isDarkTheme by remember { mutableStateOf(false) }

    val file: File = File.createTempFile("share", ".jpg", context.cacheDir)

    fun getInverseBWColor(color: Int): Color {
        val red = android.graphics.Color.red(color)
        val blue = android.graphics.Color.blue(color)
        val green = android.graphics.Color.green(color)

        val luminance = (red * 0.299) + (green * 0.7152) + (blue * 0.0722)
        return if (luminance < 140) Color.White else Color.Black
    }

    fun shareImage(bitmap: Bitmap) {
        val outputStream: OutputStream
        try {
            outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            // TODO: Show error message
            return
        }
        val fileUri = FileProvider.getUriForFile(
            context,
            BuildConfig.APPLICATION_ID + ".provider",
            file
        )
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, fileUri)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share"))
    }

    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            onDispose {
                if (isDarkTheme) {
                    systemUiController.setStatusBarColor(BigStone)
                } else {
                    systemUiController.setStatusBarColor(Color.White)
                }
            }
        }
    )

    Scaffold(
        topBar = {
            TopBarComponent(
                title = "Share",
                rightIcon = R.drawable.ic_arrow_left,
                onRightIconClicked = onBackPressed,
                foregroundColor = secondaryColor,
                backgroundColor = Color.Transparent,
                textAlignment = TextAlign.Center
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = secondaryColor,
                    contentColor = primaryColor
                ),
                onClick = {
                    roundedCornerSize = 0.dp
                    coroutineScope.launch {
                        delay(100)
                        captureController.capture()
                    }
                }
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 12.dp),
                    textAlign = TextAlign.Center,
                    text = "Share",
                    fontFamily = Poppins,
                    fontSize = 15.sp,
                    color = primaryColor
                )
            }
        },
        backgroundColor = if (primaryColor == Color.Black) Dune else primaryColor
    ) { paddingValues ->
        isDarkTheme = isSystemInDarkTheme()
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Tap for more",
                fontFamily = Poppins,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = secondaryColor
            )
            Divider(
                color = Color.Transparent,
                thickness = 12.dp
            )
            var paddingSize by remember { mutableStateOf(38.dp) }
            Capturable(
                modifier = Modifier
                    .animateContentSize()
                    .padding(paddingSize),
                controller = captureController,
                onCaptured = { imageBitmap, _ ->
                    roundedCornerSize = 18.dp
                    if (imageBitmap != null) {
                        shareImage(imageBitmap.asAndroidBitmap())
                    }
                }
            ) {
                systemUiController.setStatusBarColor(primaryColor)
                ShareCard(
                    title = buildString {
                        append("QS. ")
                        append(verse?.surahName)
                        append(": ")
                        append(verse?.number?.inSurah)
                    },
                    arabText = verse?.text?.arab ?: "",
                    text = verse?.translation?.en ?: "",
                    primaryColor = primaryColor,
                    secondaryColor = secondaryColor,
                    roundedCornerSize = roundedCornerSize,
                    onCardClicked = {
                        val currentIndex = shareBackgroundList.indexOfFirst { it == primaryColor }
                        val index =
                            if (currentIndex == shareBackgroundList.lastIndex) 0 else currentIndex + 1
                        primaryColor = shareBackgroundList[index]
                        secondaryColor = getInverseBWColor(shareBackgroundList[index].toArgb())

                        paddingSize = 42.dp
                        coroutineScope.launch {
                            delay(100)
                            paddingSize = 38.dp
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ShareCard(
    title: String,
    arabText: String,
    text: String,
    primaryColor: Color,
    secondaryColor: Color,
    roundedCornerSize: Dp,
    onCardClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(
                elevation = 18.dp,
                ambientColor = secondaryColor,
                shape = RoundedCornerShape(roundedCornerSize)
            )
            .clip(RoundedCornerShape(roundedCornerSize))
            .background(color = primaryColor)
            .clickable { onCardClicked() }
            .padding(20.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            fontFamily = Poppins,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = secondaryColor
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            text = arabText,
            fontFamily = Misbah,
            fontSize = 20.sp,
            color = secondaryColor,
            textAlign = TextAlign.Right
        )
        Text(
            text = text,
            fontFamily = Poppins,
            fontSize = 13.sp,
            color = secondaryColor
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.ic_quran_active),
                contentDescription = stringResource(id = R.string.app_name),
                colorFilter = ColorFilter.tint(secondaryColor),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                modifier = Modifier.padding(top = 1.dp),
                text = stringResource(id = R.string.app_name),
                fontFamily = Poppins,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = secondaryColor
            )
        }
    }
}