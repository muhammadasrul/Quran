package com.acun.quranicplus.ui.quranicplus

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.acun.quranicplus.data.remote.response.juz_list.Juz
import com.acun.quranicplus.data.remote.response.surah.Verse
import com.acun.quranicplus.data.remote.response.surah_list.Surah
import com.acun.quranicplus.ui.screen.SplashScreen
import com.acun.quranicplus.ui.screen.quran.detail.DetailViewModel
import com.acun.quranicplus.ui.screen.quran.detail.QuranDetailScreen
import com.acun.quranicplus.ui.screen.quran.share.ShareScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object QuranicPlusDestinations {
    const val SPLASH_SCREEN_ROUTE = "splash_screen"
    const val QURANIC_PLUS_ROUTE = "quranicplus"
    const val QURAN_DETAIL_ROUTE = "quran_detail"
    const val SHARE_ROUTE = "quran_detail/share"
}

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    finishActivity: () -> Unit = {},
    navController: NavHostController = rememberNavController(),
    startDestination: String = QuranicPlusDestinations.QURANIC_PLUS_ROUTE
) {
    val coroutineScope = rememberCoroutineScope()
    val isPermissionGranted = remember(true) { mutableStateOf(false) }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(QuranicPlusDestinations.SPLASH_SCREEN_ROUTE) {
            BackHandler {
                finishActivity()
            }

            SplashScreen(
                onPermissionGranted = {
                    coroutineScope.launch {
                        delay(1000)
                        isPermissionGranted.value = true

                        val navBuilder = NavOptions.Builder()
                            .setPopUpTo(QuranicPlusDestinations.QURANIC_PLUS_ROUTE, true)
                            .build()
                        navController.navigate(QuranicPlusTabs.HOME.route, navBuilder)
                    }
                },
                onCloseClicked = {
                    finishActivity()
                }
            )
        }
        navigation(
            route = QuranicPlusDestinations.QURANIC_PLUS_ROUTE,
            startDestination = QuranicPlusTabs.HOME.route
        ) {
            quranicPlus(
                onSplashScreenComplete = isPermissionGranted,
                navHostController = navController
            )
        }
        composable(
            route = QuranicPlusDestinations.QURAN_DETAIL_ROUTE
        ) {
            val viewModel: DetailViewModel = hiltViewModel()
            val surahNavArgs =
                navController.previousBackStackEntry?.savedStateHandle?.get<Surah>("surah")
            val juzNavArgs = navController.previousBackStackEntry?.savedStateHandle?.get<Juz>("juz")
            val juzPos =
                navController.previousBackStackEntry?.savedStateHandle?.get<Int>("pos") ?: 0
            QuranDetailScreen(
                viewModel = viewModel,
                onBackPressed = { navController.navigateUp() },
                onShareClicked = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("verse", it)
                    navController.currentBackStackEntry?.savedStateHandle?.set("surah", surahNavArgs)

                    navController.navigate(QuranicPlusDestinations.SHARE_ROUTE)
                },
                surahNavArgs = surahNavArgs,
                juzNavArgs = juzNavArgs,
                juzPos = juzPos
            )
        }
        composable(
            route = QuranicPlusDestinations.SHARE_ROUTE
        ) {
            val verse = navController.previousBackStackEntry?.savedStateHandle?.get<Verse>("verse")
            val surah = navController.previousBackStackEntry?.savedStateHandle?.get<Surah>("surah")

            ShareScreen(
                verse = verse,
                surah = surah,
                onBackPressed = { navController.navigateUp() }
            )
        }
    }
}