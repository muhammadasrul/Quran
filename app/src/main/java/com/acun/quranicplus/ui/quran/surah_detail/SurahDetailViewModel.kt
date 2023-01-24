package com.acun.quranicplus.ui.quran.surah_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.acun.quranicplus.data.local.datastore.LastReadVerse
import com.acun.quranicplus.data.local.datastore.QuranDataStore
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.data.remote.response.juz.JuzDetail
import com.acun.quranicplus.data.remote.response.surah.SurahDetail
import com.acun.quranicplus.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurahDetailViewModel @Inject constructor(
    private val repository: QuranRepository,
    private val dataStore: QuranDataStore
): ViewModel() {

    private val _surahDetail = MutableLiveData<Resource<SurahDetail>>()
    val surahDetail: LiveData<Resource<SurahDetail>> = _surahDetail

    fun getSurah(number: Int) {
        viewModelScope.launch {
            repository.getSurah(number).collect {
                val verses = it.data?.verses?.map { verse ->
                    verse.copy(
                        headerName = "",
                        surahName = "",
                        numberOfVerse = "",
                        surahNameTranslation = ""
                    )
                }
                it.data?.verses = verses ?: listOf()
                _surahDetail.postValue(it)
            }
        }
    }

    private val _juzDetail = MutableLiveData<Resource<JuzDetail>>()
    val juzDetail: LiveData<Resource<JuzDetail>> = _juzDetail

    fun getJuzDetail(number: Int) {
        viewModelScope.launch {
            repository.getJuz(number).collect {
                _juzDetail.postValue(it)
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