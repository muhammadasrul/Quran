package com.acun.quran.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acun.quran.data.remote.response.Resource
import com.acun.quran.data.remote.response.prayer.PrayerTimeData
import com.acun.quran.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: QuranRepository
): ViewModel() {

    private val _prayer = MutableLiveData< Resource<List<PrayerTimeData>>>()
    val prayer: LiveData<Resource<List<PrayerTimeData>>> = _prayer

    fun getPrayer(lat: Double, long: Double) {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"), Locale.getDefault())
        val month = cal.get(Calendar.MONTH)+1
        val year = cal.get(Calendar.YEAR)
        viewModelScope.launch {
            repository.getPrayer(
                latitude = lat,
                longitude = long,
                methode = 2,
                month = month,
                year = year
            ).collect { resource ->
                _prayer.postValue(resource)
            }
        }
    }
}