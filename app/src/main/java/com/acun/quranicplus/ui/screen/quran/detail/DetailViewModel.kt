package com.acun.quranicplus.ui.screen.quran.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.acun.quranicplus.data.local.datastore.LastReadVerse
import com.acun.quranicplus.data.local.datastore.QuranDataStore
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: QuranRepository,
    private val dataStore: QuranDataStore
): ViewModel() {

    private val _surahDetail = MutableLiveData<SurahDetailState>()
    val surahDetail: LiveData<SurahDetailState> = _surahDetail

    fun getSurah(number: Int) {
        viewModelScope.launch {
            repository.getSurah(number).collect {
                it.data?.verses = it.data?.verses?.map { verse ->
                    verse.copy(
                        headerName = "",
                        surahName = "",
                        numberOfVerse = "",
                        surahNameTranslation = ""
                    )
                } ?: emptyList()

                when (it) {
                    is Resource.Success -> {
                        _surahDetail.postValue(SurahDetailState(
                            surahDetail = it.data,
                            isLoading = false,
                            isError = false
                        ))
                    }
                    is Resource.Failed -> {
                        _surahDetail.postValue(SurahDetailState(
                            isLoading = false,
                            isError = true
                        ))
                    }
                    is Resource.Loading -> {
                        _surahDetail.postValue(SurahDetailState(
                            isLoading = true,
                            isError = false
                        ))
                    }
                }
            }
        }
    }

    private val _juzDetail = MutableLiveData<JuzDetailState>()
    val juzDetail: LiveData<JuzDetailState> = _juzDetail

    fun getJuzDetail(number: Int) {
        viewModelScope.launch {
            repository.getJuz(number).collect {
                when (it) {
                    is Resource.Success -> {
                        _juzDetail.postValue(JuzDetailState(
                            juzDetail = it.data,
                            isLoading = false,
                            isError = false
                        ))
                    }
                    is Resource.Failed -> {
                        _juzDetail.postValue(JuzDetailState(
                            isLoading = false,
                            isError = true
                        ))
                    }
                    is Resource.Loading -> {
                        _juzDetail.postValue(JuzDetailState(
                            isLoading = true,
                            isError = false
                        ))
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

    val lastRead = dataStore.lastRead.asLiveData()
    val versePreference = dataStore.versePreference.asLiveData()
}