package com.acun.quranicplus.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.ui.theme.Poppins

@Composable
fun QiblaFinderComponent(
    kaabaDegree: Float,
    compassDegree: Float
) {
    Column {
        Text(
            modifier = Modifier.padding(start = 20.dp),
            text = stringResource(id = R.string.qibla_finder),
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )
        Divider(thickness = 8.dp, color = Color.Transparent)
        Box(
            modifier = Modifier.padding(start = 72.dp, end = 72.dp, top = 20.dp)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(compassDegree),
                painter = painterResource(id = R.drawable.compass),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(kaabaDegree),
                painter = painterResource(id = R.drawable.kaaba_directioin),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}