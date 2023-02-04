package com.acun.quranicplus.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.ui.theme.blue
import com.acun.quranicplus.ui.theme.poppins
import com.acun.quranicplus.ui.theme.textBlack

@Composable
fun DropdownPrefComponent(
    title: String,
    values: Array<String>,
    initValue: String,
    onItemClicked: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf(initValue) }

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
            color = textBlack
        )

        Box {
            TextButton(onClick = {
                expanded = !expanded
            }) {
                Row {
                    Text(
                        text = value,
                        fontFamily = poppins,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = textBlack
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "DropDown",
                        tint = blue
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = !expanded
                }
            ) {
                values.forEachIndexed { index, s ->
                    DropdownMenuItem(onClick = {
                        value = s
                        expanded = !expanded
                        onItemClicked(index)
                    }) {
                        Text(
                            text = s,
                            fontFamily = poppins,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
fun DropdownPrefComponentPreview() {
    DropdownPrefComponent(title = "Text Size", values = arrayOf("Medium"), initValue = "Medium") {}
}