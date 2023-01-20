package com.acun.quranicplus.ui.quran

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.acun.quranicplus.data.local.datastore.LastReadVerse
import com.acun.quranicplus.data.local.datastore.QuranDataStore
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.data.remote.response.juz_list.Juz
import com.acun.quranicplus.data.remote.response.surah_list.Surah
import com.acun.quranicplus.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    val repository: QuranRepository,
    dataStore: QuranDataStore
): ViewModel() {
    val lastRead:LiveData<LastReadVerse> = dataStore.lastRead.asLiveData()

    private val _surahList: MutableLiveData<Resource<List<Surah>>> = MutableLiveData()
    val surahList: LiveData<Resource<List<Surah>>> = _surahList

    private val _juzList = MutableLiveData<Resource<List<Juz>>>()
    val juzList: LiveData<Resource<List<Juz>>> = _juzList

    init {
        getSurahList()
        getJuzList()
    }

    private fun getJuzList() {
        viewModelScope.launch {
            repository.getAllJuz(context).collect {
                _juzList.postValue(it)
            }
        }
    }

    private fun getSurahList() {
        viewModelScope.launch {
            repository.getAllSurah().collect {
                _surahList.postValue(it)
            }
        }
    }

    override fun onCleared() {
        Log.d("QuranViewModel", "onCleared: cleared")
    }
}