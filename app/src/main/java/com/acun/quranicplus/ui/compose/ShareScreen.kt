package com.acun.quranicplus.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.data.remote.response.surah.Verse
import com.acun.quranicplus.data.remote.response.surah_list.Surah
import com.acun.quranicplus.ui.compose.theme.misbah
import com.acun.quranicplus.ui.compose.theme.poppins

@Composable
fun ShareScreen(
    verse: Verse,
    surah: Surah?,
    primaryColor: Int,
    secondaryColor: Int,
    onBackPressed: () -> Unit,
    onCardClicked: () -> Unit,
    onShareClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBarComponent(
                title = "Share",
                rightIcon = R.drawable.ic_arrow_left,
                onRightIconClicked = onBackPressed,
                color = colorResource(id = secondaryColor)
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .padding(vertical = 18.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = secondaryColor),
                    contentColor = colorResource(id = primaryColor)),
                onClick = {
                    onShareClicked()
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
        backgroundColor = colorResource(id = primaryColor)
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
                color = colorResource(id = secondaryColor)
            )
            Divider(
                color = Color.Transparent,
                thickness = 20.dp
            )
            ShareCard(
                title = buildString {
                    append("QS. ")
                    append(surah?.name?.transliteration?.en ?: verse.surahName)
                    append(": ")
                    append(verse.number.inSurah)
                },
                arabText = verse.text.arab,
                text = verse.translation.en,
                primaryColor = primaryColor,
                secondaryColor = secondaryColor,
                onCardClicked = { onCardClicked() }
            )
        }
    }
}

@Composable
fun ShareCard(
    title: String,
    arabText: String,
    text: String,
    onCardClicked: () -> Unit,
    primaryColor: Int,
    secondaryColor: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 38.dp)
            .shadow(
                elevation = 20.dp,
                ambientColor = colorResource(id = secondaryColor),
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
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
            text = arabText,
            fontFamily = misbah,
            fontSize = 20.sp,
            color = colorResource(id = secondaryColor)
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
                .padding(top = 14.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = R.drawable.icon),
                contentDescription = stringResource(id = R.string.app_name),
                colorFilter = ColorFilter.tint(colorResource(id = secondaryColor))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                fontFamily = poppins,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(id = secondaryColor)
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 500, widthDp = 300)
@Composable
fun ShareCardPreview() {
    ShareCard(title = "Al-Fathiha", arabText = "'asdkfangabdkgekbefbcwjkeoifhbvb", text = "bfdkjbvkbkkbksjdnf", {}, R.color.white, R.color.black)
}