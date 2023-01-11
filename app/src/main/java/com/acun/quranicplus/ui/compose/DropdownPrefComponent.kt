package com.acun.quranicplus.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.ui.compose.theme.poppins

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
            color = colorResource(id = R.color.text_black)
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
                        color = colorResource(id = R.color.text_black)
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "DropDown",
                        tint = colorResource(id = R.color.primary_blue)
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