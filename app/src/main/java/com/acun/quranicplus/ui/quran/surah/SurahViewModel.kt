package com.acun.quranicplus.ui.quran.surah

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.data.remote.response.surah_list.Surah
import com.acun.quranicplus.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurahViewModel @Inject constructor(private val repository: QuranRepository) : ViewModel() {

    private val _surahList: MutableLiveData<Resource<List<Surah>>> = MutableLiveData()
    val surahList: LiveData<Resource<List<Surah>>> = _surahList

    init {
        getSurahList()
    }

    private fun getSurahList() {
        viewModelScope.launch {
            repository.getAllSurah().collect {
                _surahList.postValue(it)
            }
        }
    }
}