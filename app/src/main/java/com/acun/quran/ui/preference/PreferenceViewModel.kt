package com.acun.quran.ui.preference

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.acun.quran.data.local.datastore.QuranDataStore
import com.acun.quran.data.local.datastore.VersePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferenceViewModel @Inject constructor(val quranDataStore: QuranDataStore): ViewModel() {

    fun setVersePreference(versePreference: VersePreference) {
        viewModelScope.launch {
            quranDataStore.setVersePreference(versePreference)
        }
    }

    val versePreference = quranDataStore.versePreference.asLiveData()
}