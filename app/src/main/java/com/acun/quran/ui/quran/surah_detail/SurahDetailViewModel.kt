package com.acun.quran.ui.quran.surah_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acun.quran.data.local.datastore.LastReadDataStore
import com.acun.quran.data.local.datastore.LastReadVerse
import com.acun.quran.data.remote.response.Resource
import com.acun.quran.data.remote.response.surah.SurahDetail
import com.acun.quran.data.remote.response.surah.Verse
import com.acun.quran.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurahDetailViewModel @Inject constructor(
    private val repository: QuranRepository,
    private val dataStore: LastReadDataStore
): ViewModel() {

    private val _surahDetail = MutableLiveData<SurahDetail>()
    val surahDetail: LiveData<SurahDetail> = _surahDetail

    fun getSurah(number: Int) {
        viewModelScope.launch {
            repository.getSurah(number).collect {
                if (it is Resource.Success) {
                    it.data?.let { surah ->
                        _surahDetail.postValue(surah)
                    }
                }
            }
        }
    }

    fun setLastRead(item: LastReadVerse) {
        viewModelScope.launch {
            dataStore.setLastRead(item)
        }
    }
}