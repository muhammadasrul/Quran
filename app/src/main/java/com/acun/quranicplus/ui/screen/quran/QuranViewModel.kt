package com.acun.quranicplus.ui.screen.quran

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.acun.quranicplus.data.local.datastore.LastReadVerse
import com.acun.quranicplus.data.local.datastore.QuranDataStore
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.repository.QuranRepository
import com.acun.quranicplus.util.Language
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(
    val repository: QuranRepository,
    dataStore: QuranDataStore
) : ViewModel() {
    val preference = dataStore.versePreference.asLiveData()
    val lastRead: LiveData<LastReadVerse> = dataStore.lastRead.asLiveData()

    private val _surahList = MutableStateFlow(SurahState())
    val surahList = _surahList.asStateFlow()

    private val _juzList = MutableStateFlow(JuzState())
    val juzList = _juzList.asStateFlow()

    init {
        getSurahList()
        getJuzList()
    }

    private fun getJuzList() {
        viewModelScope.launch {
            repository.getAllJuz().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _juzList.update {
                            JuzState(
                                isLoading = true,
                                isError = false
                            )
                        }
                    }

                    is Resource.Failed -> {
                        _juzList.update {
                            JuzState(
                                isLoading = false,
                                isError = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _juzList.update {
                            JuzState(
                                juzList = resource.data ?: emptyList(),
                                isLoading = false,
                                isError = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getSurahList() {
        viewModelScope.launch {
            repository.getAllSurah().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _surahList.update {
                            SurahState(
                                isLoading = true,
                                isError = false
                            )
                        }
                    }

                    is Resource.Failed -> {
                        _surahList.update {
                            SurahState(
                                isLoading = false,
                                isError = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _surahList.update {
                            SurahState(
                                surahList = resource.data ?: emptyList(),
                                isLoading = false,
                                isError = false
                            )
                        }
                    }
                }
            }
        }
    }

    private val _filteredSurahList = MutableStateFlow(SurahState())
    val filteredSurahList = _filteredSurahList.asStateFlow()

    fun updateQuery(query: String) {
        val cleanQuery = query
            .replace("-", "")
            .replace(" ", "")
            .lowercase()

        val filtered = _surahList.value.surahList.filter {
            if (preference.value?.languagePos == Language.ID) {
                it.name.transliteration.id
                    .replace("-", "")
                    .lowercase()
                    .contains(cleanQuery)
            } else {
                it.name.transliteration.en
                    .replace("-", "")
                    .lowercase()
                    .contains(cleanQuery)
            }
        }

        _filteredSurahList.update {
            SurahState(
                surahList = if (query.isNotEmpty()) filtered else emptyList(),
                isLoading = false,
                isError = false
            )
        }
    }
}