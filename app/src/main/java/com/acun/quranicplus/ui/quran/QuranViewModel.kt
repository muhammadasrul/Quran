package com.acun.quranicplus.ui.quran

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.acun.quranicplus.data.local.datastore.QuranDataStore
import com.acun.quranicplus.data.local.datastore.LastReadVerse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(dataStore: QuranDataStore): ViewModel() {
    val lastRead:LiveData<LastReadVerse> = dataStore.lastRead.asLiveData()
}