package com.acun.quranicplus.ui.quranicplus

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
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
import com.acun.quranicplus.ui.screen.quran.QuranViewModel
import com.acun.quranicplus.ui.screen.quran.SearchScreen
import com.acun.quranicplus.ui.screen.quran.detail.DetailViewModel
import com.acun.quranicplus.ui.screen.quran.detail.QuranDetailScreen
import com.acun.quranicplus.ui.screen.quran.share.ShareScreen
import com.acun.quranicplus.ui.screen.quran.share.ShareViewModel

object QuranicPlusDestinations {
    const val SPLASH_SCREEN_ROUTE = "splash_screen"
    const val QURANIC_PLUS_ROUTE = "quranicplus"
    const val QURAN_DETAIL_ROUTE = "quran_detail"
    const val SHARE_ROUTE = "quran_detail/share"
    const val QURAN_SEARCH = "quran_search"
}

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    finishActivity: () -> Unit = {},
    navController: NavHostController = rememberNavController(),
    startDestination: String = QuranicPlusDestinations.SPLASH_SCREEN_ROUTE
) {
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
                    val navBuilder = NavOptions.Builder()
                        .setPopUpTo(QuranicPlusDestinations.SPLASH_SCREEN_ROUTE, true)
                        .build()
                    navController.navigate(QuranicPlusTabs.HOME.route, navBuilder)
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
            quranicPlus(navController)
        }
        composable(
            route = QuranicPlusDestinations.QURAN_DETAIL_ROUTE
        ) {
            val viewModel: DetailViewModel = hiltViewModel()
            val surahNavArgs = navController.previousBackStackEntry?.savedStateHandle?.get<Surah>("surah")
            val juzNavArgs = navController.previousBackStackEntry?.savedStateHandle?.get<Juz>("juz")
            val juzPos = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("pos") ?: 0
            QuranDetailScreen(
                viewModel = viewModel,
                onBackPressed = { navController.navigateUp() },
                onShareClicked = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("verse", it.copy())
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
            val viewModel: ShareViewModel = hiltViewModel()
            ShareScreen(
                viewModel = viewModel,
                verse = verse,
                onBackPressed = { navController.navigateUp() }
            )
        }
        composable(
            route = QuranicPlusDestinations.QURAN_SEARCH
        ) {
            val viewModel: QuranViewModel = hiltViewModel()
            SearchScreen(
                viewModel = viewModel,
                onSurahDetailClicked = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("juz", null)
                    navController.currentBackStackEntry?.savedStateHandle?.set("surah", it)
                    navController.navigate(QuranicPlusDestinations.QURAN_DETAIL_ROUTE)
                }
            )
        }
    }
}