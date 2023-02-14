package com.acun.quranicplus.ui.screen.preference

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.BuildConfig
import com.acun.quranicplus.R
import com.acun.quranicplus.data.local.datastore.VersePreference
import com.acun.quranicplus.ui.component.DropdownPrefComponent
import com.acun.quranicplus.ui.component.SwitchPrefComponent
import com.acun.quranicplus.ui.component.TopBarComponent
import com.acun.quranicplus.ui.theme.Poppins

@Composable
fun PreferenceScreen(viewModel: PreferenceViewModel) {
    val state = viewModel.versePreference.observeAsState()
    var transliterationState = state.value?.transliteration ?: false
    var translationState = state.value?.translation ?: false
    val textSizeState = state.value?.textSizePos ?: 0

    val textSizeArr = stringArrayResource(id = R.array.text_size)

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
                text = "Verse Preference",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSecondary
            )
            SwitchPrefComponent(title = "Transliteration", isChecked = transliterationState) {
                transliterationState = it
                viewModel.setVersePreference(VersePreference(transliterationState, translationState, textSizeState))
            }
            SwitchPrefComponent(title = "Translation", isChecked = translationState) {
                translationState = it
                viewModel.setVersePreference(VersePreference(transliterationState, translationState, textSizeState))
            }
            DropdownPrefComponent(
                title = "Text Size",
                values = textSizeArr,
                selected = textSizeArr[textSizeState],
                onItemClicked = {
                    viewModel.setVersePreference(
                        VersePreference(
                            transliteration = transliterationState,
                            translation = translationState,
                            textSizePos = it
                        )
                    )
                }
            )
        }
    }
}