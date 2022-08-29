package com.acun.quran.ui.quran.juz

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acun.quran.data.remote.response.Resource
import com.acun.quran.data.remote.response.juz_list.Juz
import com.acun.quran.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JuzViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: QuranRepository
) : ViewModel() {

    private val _juzList = MutableLiveData<List<Juz>>()
    val juzList: LiveData<List<Juz>> = _juzList

    init {
        getJuzList()
    }

    private fun getJuzList() {
        viewModelScope.launch {
            repository.getAllJuz(context).collect {
                if (it is Resource.Success) {
                    it.data?.let { juz ->
                        _juzList.postValue(juz)
                    }
                }
            }
        }
    }
}