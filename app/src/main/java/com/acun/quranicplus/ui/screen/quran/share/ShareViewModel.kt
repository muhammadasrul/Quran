package com.acun.quranicplus.ui.screen.quran.share

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.acun.quranicplus.data.local.datastore.QuranDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    dataStore: QuranDataStore
): ViewModel() {
    val pref = dataStore.versePreference.asLiveData()
}