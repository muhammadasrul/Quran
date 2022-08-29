package com.acun.quran.ui.quran

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.acun.quran.data.local.datastore.LastReadDataStore
import com.acun.quran.data.local.datastore.LastReadVerse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(dataStore: LastReadDataStore): ViewModel() {
    val lastRead:LiveData<LastReadVerse> = dataStore.lastRead.asLiveData()
}