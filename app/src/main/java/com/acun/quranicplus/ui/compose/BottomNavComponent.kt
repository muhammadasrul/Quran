package com.acun.quranicplus.ui.compose

import androidx.compose.foundation.background
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.acun.quranicplus.R
import com.acun.quranicplus.ui.compose.quranicplus.QuranicPlusTabs

@Composable
fun BottomNavComponent() {
    BottomNavigation {
        val tabs = QuranicPlusTabs.values()
        tabs.forEach {
            BottomNavigationItem(
                modifier = Modifier.background(Color.White),
                selected = true,
                onClick = { /*TODO*/ },
                icon = { Icon(painter = painterResource(id = it.icon), contentDescription = null)},
                enabled = false,
                alwaysShowLabel = false,
                selectedContentColor = colorResource(id = R.color.primary_blue),
            )
        }
    }
}

@Preview
@Composable
fun BottomNavPreview() {
    BottomNavigation {
        val tabs = QuranicPlusTabs.values()
        tabs.forEach {
            BottomNavigationItem(
                modifier = Modifier.background(Color.White),
                selected = true,
                onClick = { /*TODO*/ },
                icon = { Icon(painter = painterResource(id = it.icon), contentDescription = null)},
                enabled = false,
                alwaysShowLabel = false,
                selectedContentColor = colorResource(id = R.color.primary_blue),
            )
        }
    }
}