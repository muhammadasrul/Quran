package com.acun.quranicplus.ui.compose

import android.graphics.Bitmap
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.data.remote.response.surah.Verse
import com.acun.quranicplus.data.remote.response.surah_list.Surah
import com.acun.quranicplus.ui.compose.theme.misbah
import com.acun.quranicplus.ui.compose.theme.poppins
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ShareScreen(
    verse: Verse,
    surah: Surah?,
    primaryColor: Int,
    secondaryColor: Int,
    onBackPressed: () -> Unit,
    onCardClicked: () -> Unit,
    onShareCaptured: (Bitmap) -> Unit
) {
    val captureController = rememberCaptureController()
    var roundedCornerSize by remember { mutableStateOf(18.dp) }
    Scaffold(
        topBar = {
            TopBarComponent(
                title = "Share",
                rightIcon = R.drawable.ic_arrow_left,
                onRightIconClicked = onBackPressed,
                foregroundColor = colorResource(id = secondaryColor),
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
                    backgroundColor = colorResource(id = secondaryColor),
                    contentColor = colorResource(id = primaryColor)),
                onClick = {
                    roundedCornerSize = 0.dp
                    CoroutineScope(Dispatchers.IO).launch {
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
                    fontFamily = poppins,
                    fontSize = 15.sp,
                    color = colorResource(id = primaryColor)
                )
            }
        },
        backgroundColor = colorResource(id = if (primaryColor == R.color.black) R.color.text_black else primaryColor)
    ) { paddingValues ->
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
                fontFamily = poppins,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(id = secondaryColor)
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
                        onShareCaptured(imageBitmap.asAndroidBitmap())
                    }
                }
            ) {
                ShareCard(
                    title = buildString {
                        append("QS. ")
                        append(surah?.name?.transliteration?.en ?: verse.surahName)
                        append(": ")
                        append(verse.number.inSurah) },
                    arabText = verse.text.arab,
                    text = verse.translation.en,
                    primaryColor = primaryColor,
                    secondaryColor = secondaryColor,
                    roundedCornerSize = roundedCornerSize,
                    onCardClicked = {
                        onCardClicked()
                        paddingSize = 42.dp
                        CoroutineScope(Dispatchers.IO).launch {
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
    primaryColor: Int,
    secondaryColor: Int,
    roundedCornerSize: Dp,
    onCardClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(
                elevation = 18.dp,
                ambientColor = colorResource(id = secondaryColor),
                shape = RoundedCornerShape(roundedCornerSize)
            )
            .clip(RoundedCornerShape(roundedCornerSize))
            .background(color = colorResource(id = primaryColor))
            .clickable { onCardClicked() }
            .padding(20.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            fontFamily = poppins,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = colorResource(id = secondaryColor)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            text = arabText,
            fontFamily = misbah,
            fontSize = 20.sp,
            color = colorResource(id = secondaryColor),
            textAlign = TextAlign.Right
        )
        Text(
            text = text,
            fontFamily = poppins,
            fontSize = 13.sp,
            color = colorResource(id = secondaryColor)
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
                colorFilter = ColorFilter.tint(colorResource(id = secondaryColor)),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                modifier = Modifier.padding(top = 1.dp),
                text = stringResource(id = R.string.app_name),
                fontFamily = poppins,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(id = secondaryColor)
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 500, widthDp = 300)
@Composable
fun ShareCardPreview() {
    ShareCard(title = "Al-Fathiha", arabText = "'asdkfangabdkgekbefbcwjkeoifhbvb", text = "bfdkjbvkbkkbksjdnf", R.color.white, R.color.black, 12.dp) {}
}