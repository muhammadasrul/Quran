package com.acun.quran.ui.quran.juz_detail

import androidx.lifecycle.*
import com.acun.quran.data.local.datastore.LastReadVerse
import com.acun.quran.data.local.datastore.QuranDataStore
import com.acun.quran.data.remote.response.Resource
import com.acun.quran.data.remote.response.juz.JuzDetail
import com.acun.quran.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JuzDetailViewModel @Inject constructor(
    private val repository: QuranRepository,
    private val dataStore: QuranDataStore
): ViewModel() {

    private val _juz = MutableLiveData<Resource<JuzDetail>>()
    val juz: LiveData<Resource<JuzDetail>> = _juz

    fun getJuzDetail(number: Int) {
        viewModelScope.launch {
            repository.getJuz(number).collect {
                _juz.postValue(it)
            }
        }
    }

    fun setLastRead(lastRead: LastReadVerse) {
        viewModelScope.launch {
            dataStore.setLastRead(lastRead)
        }
    }

    val lastRead = dataStore.lastRead.asLiveData()
    val versePreference = dataStore.versePreference.asLiveData()
}