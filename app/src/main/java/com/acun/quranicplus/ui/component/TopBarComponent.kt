package com.acun.quranicplus.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.ui.theme.poppins

@Composable
fun TopBarComponent(
    title: String,
    foregroundColor: Color = colorResource(id = R.color.text_black),
    backgroundColor: Color = Color.White,
    rightIcon: Int = 0,
    textAlignment: TextAlign = TextAlign.Start,
    onRightIconClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (rightIcon != 0) {
                IconButton(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onClick = onRightIconClicked
                ) {
                    Icon(
                        tint = foregroundColor,
                        painter = painterResource(id = rightIcon),
                        contentDescription = "Right Icon"
                    )
                }
            }
            Text(
                modifier = Modifier
                    .padding(end = if (rightIcon != 0) 56.dp else 0.dp)
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 6.dp),
                text = title,
                color = foregroundColor,
                fontSize = 20.sp,
                fontFamily = poppins,
                fontWeight = FontWeight.SemiBold,
                textAlign = textAlignment
            )
        }
        Divider(
            modifier = Modifier.alpha(.15f),
            thickness = .6.dp,
            color = colorResource(id = R.color.text_black_light),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TobBarComponentPreview() {
    MaterialTheme {
        TopBarComponent(title = "Setting", rightIcon = R.drawable.ic_arrow_left, onRightIconClicked = {}, foregroundColor = colorResource(id = R.color.black))
    }
}