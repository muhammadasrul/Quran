package com.acun.quranicplus.ui.preference

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.acun.quranicplus.data.local.datastore.QuranDataStore
import com.acun.quranicplus.data.local.datastore.VersePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferenceViewModel @Inject constructor(private val quranDataStore: QuranDataStore): ViewModel() {

    fun setVersePreference(versePreference: VersePreference) {
        viewModelScope.launch {
            quranDataStore.setVersePreference(versePreference)
        }
    }

    val versePreference = quranDataStore.versePreference.asLiveData()
}