package com.acun.quran.ui.quran.surah_detail

import androidx.lifecycle.*
import com.acun.quran.data.local.datastore.LastReadVerse
import com.acun.quran.data.local.datastore.QuranDataStore
import com.acun.quran.data.remote.response.Resource
import com.acun.quran.data.remote.response.surah.SurahDetail
import com.acun.quran.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurahDetailViewModel @Inject constructor(
    private val repository: QuranRepository,
    private val dataStore: QuranDataStore
): ViewModel() {

    private val _surahDetail = MutableLiveData< Resource<SurahDetail>>()
    val surahDetail: LiveData<Resource<SurahDetail>> = _surahDetail

    fun getSurah(number: Int) {
        viewModelScope.launch {
            repository.getSurah(number).collect {
                _surahDetail.postValue(it)
            }
        }
    }

    fun setLastRead(item: LastReadVerse) {
        viewModelScope.launch {
            dataStore.setLastRead(item)
        }
    }

    val lastRead = dataStore.lastRead.asLiveData()
    val versePreference = dataStore.versePreference.asLiveData()
}