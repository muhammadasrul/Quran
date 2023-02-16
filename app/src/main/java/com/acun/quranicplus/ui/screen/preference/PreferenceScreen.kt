package com.acun.quranicplus.ui.screen.preference

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import com.acun.quranicplus.BuildConfig
import com.acun.quranicplus.R
import com.acun.quranicplus.data.local.datastore.VersePreference
import com.acun.quranicplus.ui.component.DropdownPrefComponent
import com.acun.quranicplus.ui.component.SwitchPrefComponent
import com.acun.quranicplus.ui.component.TopBarComponent
import com.acun.quranicplus.ui.theme.Poppins
import com.acun.quranicplus.util.Language.ENG
import com.acun.quranicplus.util.Language.ID

@Composable
fun PreferenceScreen(viewModel: PreferenceViewModel) {
    val state = viewModel.versePreference.observeAsState()
    val transliterationState = state.value?.transliteration ?: false
    val translationState = state.value?.translation ?: false
    val textSizeState = state.value?.textSizePos ?: 0
    val languageState = state.value?.languagePos ?: 0

    val textSizeArr = stringArrayResource(id = R.array.text_size)
    val languageArr = stringArrayResource(id = R.array.lang)

    Scaffold(
        topBar = { TopBarComponent(title = "Settings") },
        backgroundColor = MaterialTheme.colors.surface,
        bottomBar = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp),
                text = BuildConfig.VERSION_NAME,
                fontSize = 14.sp,
                color = MaterialTheme.colors.onSecondary,
                textAlign = TextAlign.Center
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Text(
                modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 12.dp),
                text = stringResource(R.string.verse_preference),
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSecondary
            )
            SwitchPrefComponent(title = stringResource(R.string.transliteration), isChecked = transliterationState) {
                viewModel.setVersePreference(
                    VersePreference(
                        transliteration = it,
                        translation = translationState,
                        textSizePos = textSizeState,
                        languagePos = languageState
                    ))
            }
            SwitchPrefComponent(title = stringResource(R.string.translation), isChecked = translationState) {
                viewModel.setVersePreference(
                    VersePreference(
                        transliteration = transliterationState,
                        translation = it,
                        textSizePos = textSizeState,
                        languagePos = languageState
                    )
                )
            }
            DropdownPrefComponent(
                title = stringResource(R.string.text_size),
                values = textSizeArr,
                selected = textSizeArr[textSizeState],
                onItemClicked = {
                    viewModel.setVersePreference(
                        VersePreference(
                            transliteration = transliterationState,
                            translation = translationState,
                            textSizePos = it,
                            languagePos = languageState
                        )
                    )
                }
            )
            DropdownPrefComponent(
                title = stringResource(R.string.language),
                values = languageArr,
                selected = languageArr[languageState],
                onItemClicked = {
                    AppCompatDelegate.setApplicationLocales(
                        LocaleListCompat.forLanguageTags(
                            when (it) {
                                ENG -> "en"
                                ID -> "id"
                                else -> "en"
                            }
                        )
                    )
                    viewModel.setVersePreference(
                        VersePreference(
                            transliteration = transliterationState,
                            translation = translationState,
                            textSizePos = textSizeState, 
                            languagePos = it,
                        )
                    )
                }
            )
        }
    }
}