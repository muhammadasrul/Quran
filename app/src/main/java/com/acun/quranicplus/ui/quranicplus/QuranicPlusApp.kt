package com.acun.quranicplus.ui.quranicplus

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.acun.quranicplus.ui.component.BottomNavComponent
import com.acun.quranicplus.ui.theme.QuranicPlusTheme

@Composable
fun QuranicPlusApp(finishActivity: () -> Unit) {
    QuranicPlusTheme {
        val tabs = remember { QuranicPlusTabs.values() }
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                BottomNavComponent(navController = navController, tabs = tabs)
            }
        ) { paddingValues ->
            NavGraph(
                modifier = Modifier.padding(paddingValues),
                finishActivity = finishActivity,
                navController = navController
            )
        }
    }
}