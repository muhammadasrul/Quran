package com.acun.quranicplus.ui.quranicplus

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.acun.quranicplus.R
import com.acun.quranicplus.ui.quranicplus.QuranicPlusTabDestinations.HOME_ROUTE
import com.acun.quranicplus.ui.quranicplus.QuranicPlusTabDestinations.QURAN_ROUTE
import com.acun.quranicplus.ui.quranicplus.QuranicPlusTabDestinations.SETTING_ROUTE
import com.acun.quranicplus.ui.screen.home.HomeScreen
import com.acun.quranicplus.ui.screen.home.HomeViewModel
import com.acun.quranicplus.ui.screen.preference.PreferenceScreen
import com.acun.quranicplus.ui.screen.preference.PreferenceViewModel
import com.acun.quranicplus.ui.screen.quran.QuranScreen
import com.acun.quranicplus.ui.screen.quran.QuranViewModel

fun NavGraphBuilder.quranicPlus(
    navHostController: NavHostController
) {
    composable(QuranicPlusTabs.HOME.route) {
        val viewModel: HomeViewModel = hiltViewModel()
        HomeScreen(viewModel = viewModel)
    }
    composable(QuranicPlusTabs.QURAN.route) {
        val viewModel: QuranViewModel = hiltViewModel()
        QuranScreen(
            viewModel = viewModel,
            onSurahDetailClicked = {
                navHostController.currentBackStackEntry?.savedStateHandle?.set("juz", null)
                navHostController.currentBackStackEntry?.savedStateHandle?.set("surah", it)
                navHostController.navigate(QuranicPlusDestinations.QURAN_DETAIL_ROUTE)
            },
            onJuzDetailClicked = { juz, pos ->
                navHostController.currentBackStackEntry?.savedStateHandle?.set("juz", juz)
                navHostController.currentBackStackEntry?.savedStateHandle?.set("surah", null)
                navHostController.currentBackStackEntry?.savedStateHandle?.set("pos", pos)
                navHostController.navigate(QuranicPlusDestinations.QURAN_DETAIL_ROUTE)
            }
        )
    }
    composable(QuranicPlusTabs.SETTING.route) {
        val viewModel: PreferenceViewModel = hiltViewModel()
        PreferenceScreen(viewModel = viewModel)
    }
}

enum class QuranicPlusTabs(
    val title: Int,
    val icon: Int,
    val selectedIcon: Int,
    val route: String
) {
    HOME(R.string.home, R.drawable.ic_home, R.drawable.ic_home_active, HOME_ROUTE),
    QURAN(R.string.quran, R.drawable.ic_quran, R.drawable.ic_quran_active, QURAN_ROUTE),
    SETTING(R.string.settings, R.drawable.ic_setting, R.drawable.ic_setting_active, SETTING_ROUTE)
}

private object QuranicPlusTabDestinations {
    const val HOME_ROUTE = "quranicplus/home"
    const val QURAN_ROUTE = "quranicplus/quran"
    const val SETTING_ROUTE = "quranicplus/setting"
}