package com.acun.quranicplus.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.acun.quranicplus.R
import com.acun.quranicplus.ui.quranicplus.QuranicPlusTabs
import com.acun.quranicplus.ui.theme.poppins

@Composable
fun BottomNavComponent(
    navController: NavController,
    tabs: Array<QuranicPlusTabs>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: QuranicPlusTabs.HOME

    val routes = remember { QuranicPlusTabs.values().map { it.route } }
    if (currentRoute in routes) {
        BottomNavigation {
            tabs.forEach { tab ->
                BottomNavigationItem(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .background(Color.White),
                    selected = currentRoute == tab.route,
                    onClick = {
                        if (tab.route != currentRoute) {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        val icon = if (tab.route == currentRoute) tab.selectedIcon else tab.icon
                        Icon(
                            modifier = Modifier.size(28.dp),
                            painter = painterResource(id = icon),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = tab.title),
                            fontFamily = poppins
                        )
                    },
                    alwaysShowLabel = false,
                    selectedContentColor = colorResource(id = R.color.primary_blue),
                    unselectedContentColor = colorResource(id = R.color.primary_blue)
                )
            }
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
                icon = { Icon(painter = painterResource(id = it.icon), contentDescription = null) },
                enabled = false,
                alwaysShowLabel = false,
                selectedContentColor = colorResource(id = R.color.primary_blue),
            )
        }
    }
}