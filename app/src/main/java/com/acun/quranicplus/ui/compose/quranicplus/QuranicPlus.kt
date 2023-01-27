package com.acun.quranicplus.ui.compose.quranicplus

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.acun.quranicplus.R
import com.acun.quranicplus.ui.compose.HomeScreen
import com.acun.quranicplus.ui.compose.PreferenceScreen
import com.acun.quranicplus.ui.compose.QuranScreen
import com.acun.quranicplus.ui.compose.quranicplus.QuranicPlusDestinations.HOME_ROUTE
import com.acun.quranicplus.ui.compose.quranicplus.QuranicPlusDestinations.QURAN_ROUTE
import com.acun.quranicplus.ui.compose.quranicplus.QuranicPlusDestinations.SETTING_ROUTE
import com.acun.quranicplus.ui.home.HomeViewModel
import com.acun.quranicplus.ui.preference.PreferenceViewModel
import com.acun.quranicplus.ui.quran.QuranViewModel

fun NavGraphBuilder.quranicPlus(
    navHostController: NavHostController
) {
    composable(QuranicPlusTabs.QURAN.route) {
        val viewModel: QuranViewModel = hiltViewModel()
        QuranScreen(
            viewModel = viewModel,
            onSurahDetailClicked = {

            },
            onJuzDetailClicked = { juz, pos ->

            }
        )
    }
    composable(QuranicPlusTabs.HOME.route) {
        val viewModel: HomeViewModel = hiltViewModel()
        HomeScreen(viewModel = viewModel)
    }
    composable(QuranicPlusTabs.SETTING.route) {
        val viewModel: PreferenceViewModel = hiltViewModel()
        PreferenceScreen(viewModel = viewModel)
    }
}

enum class QuranicPlusTabs(
    val title: Int,
    val icon: Int,
    val route: String
) {
    HOME(R.string.home, R.drawable.ic_home, HOME_ROUTE),
    QURAN(R.string.quran, R.drawable.ic_quran, QURAN_ROUTE),
    SETTING(R.string.settings, R.drawable.ic_setting, SETTING_ROUTE)
}

private object QuranicPlusDestinations {
    const val HOME_ROUTE = "quranicplus/home"
    const val QURAN_ROUTE = "quranicplus/quran"
    const val SETTING_ROUTE = "quranicplus/setting"
}