package com.acun.quranicplus.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.ui.theme.poppins

@Composable
fun SwitchPrefComponent(
    title: String,
    isChecked: Boolean,
    onCheckChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 28.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = colorResource(id = R.color.text_black)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = colorResource(id = R.color.primary_blue)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SwitchComponentPreview() {
    SwitchPrefComponent(title = "Transliteration", false) {}
}