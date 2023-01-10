package com.acun.quranicplus.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.ui.compose.theme.poppins

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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
                color = colorResource(id = R.color.primary_blue),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppins,
                fontSize = 22.sp
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 300, widthDp = 300)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}