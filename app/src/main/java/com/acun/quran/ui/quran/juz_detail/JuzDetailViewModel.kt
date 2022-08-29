package com.acun.quran.ui.quran.juz_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acun.quran.data.local.datastore.LastReadDataStore
import com.acun.quran.data.local.datastore.LastReadVerse
import com.acun.quran.data.remote.response.Resource
import com.acun.quran.data.remote.response.juz.JuzDetail
import com.acun.quran.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JuzDetailViewModel @Inject constructor(
    private val repository: QuranRepository,
    private val dataStore: LastReadDataStore
): ViewModel() {

    private val _juz = MutableLiveData<JuzDetail>()
    val juz: LiveData<JuzDetail> = _juz

    fun getJuzDetail(number: Int) {
        viewModelScope.launch {
            repository.getJuz(number).collect {
                if (it is Resource.Success) {
                    it.data?.let { juzDetail ->
                        _juz.postValue(juzDetail)
                    }
                }
            }
        }
    }

    fun setLastRead(lastRead: LastReadVerse) {
        viewModelScope.launch {
            dataStore.setLastRead(lastRead)
        }
    }
}