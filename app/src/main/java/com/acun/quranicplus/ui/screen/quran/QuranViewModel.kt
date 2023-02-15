package com.acun.quranicplus.ui.screen.quran

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.acun.quranicplus.data.local.datastore.LastReadVerse
import com.acun.quranicplus.data.local.datastore.QuranDataStore
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.repository.QuranRepository
import com.acun.quranicplus.ui.screen.quran.share.JuzState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(
    val repository: QuranRepository,
    dataStore: QuranDataStore
): ViewModel() {
    val lastRead:LiveData<LastReadVerse> = dataStore.lastRead.asLiveData()

    private val _surahList: MutableLiveData<SurahState> = MutableLiveData()
    val surahList: LiveData<SurahState> = _surahList

    private val _juzList = MutableLiveData<JuzState>()
    val juzList: LiveData<JuzState> = _juzList

    init {
        getSurahList()
        getJuzList()
    }

    private fun getJuzList() {
        viewModelScope.launch {
            repository.getAllJuz().collect {
                when (it) {
                    is Resource.Loading -> {
                        _juzList.postValue(JuzState(
                            isLoading = true,
                            isError = false
                        ))
                    }
                    is Resource.Failed -> {
                        _juzList.postValue(JuzState(
                            isLoading = false,
                            isError = true
                        ))
                    }
                    is Resource.Success -> {
                        _juzList.postValue(JuzState(
                            juzList = it.data ?: emptyList(),
                            isLoading = false,
                            isError = false
                        ))
                    }
                }
            }
        }
    }

    private fun getSurahList() {
        viewModelScope.launch {
            repository.getAllSurah().collect {
                when (it) {
                    is Resource.Loading -> {
                        _surahList.postValue(SurahState(
                            isLoading = true,
                            isError = false
                        ))
                    }
                    is Resource.Failed -> {
                        _surahList.postValue(SurahState(
                            isLoading = false,
                            isError = true
                        ))
                    }
                    is Resource.Success -> {
                        _surahList.postValue(SurahState(
                            surahList = it.data ?: emptyList(),
                            isLoading = false,
                            isError = false
                        ))
                    }
                }
            }
        }
    }
}