package com.acun.quranicplus.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.data.local.datastore.VersePreference
import com.acun.quranicplus.ui.compose.theme.poppins
import com.acun.quranicplus.ui.preference.PreferenceViewModel

@Composable
fun PreferenceScreen(viewModel: PreferenceViewModel) {
    val state = viewModel.versePreference.observeAsState()
    var transliterationState = state.value?.transliteration ?: false
    var translationState = state.value?.translation ?: false
    val textSizeState = state.value?.textSizePos ?: 0

    val textSizeArr = stringArrayResource(id = R.array.text_size)

    Column {
        Text(
            modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 12.dp),
            text = "Verse Preference",
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = colorResource(id = R.color.text_black_light)
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
            initValue = textSizeArr[textSizeState],
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