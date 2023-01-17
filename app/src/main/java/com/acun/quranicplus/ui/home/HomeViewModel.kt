package com.acun.quranicplus.ui.home

import android.location.Location
import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.*
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.data.remote.response.prayer.PrayerTimeData
import com.acun.quranicplus.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: QuranRepository
): ViewModel() {

    private var countDownTimer: CountDownTimer? = null

    private var initialTime = MutableLiveData<Long>()
    private var currentTime = MutableLiveData<Long>()

    val timeString = Transformations.map(currentTime) {
        DateUtils.formatElapsedTime(it)
    }

    fun setInitialTime(time: Long) {
        initialTime.value = time
        currentTime.value = time
        countDownTimer = object : CountDownTimer(time, 6000) {
            override fun onTick(timeInMiles: Long) {
                currentTime.value = (timeInMiles / 1000) % 60
            }

            override fun onFinish() {

            }
        }
    }

    private val _prayer = MutableLiveData<Resource<List<PrayerTimeData>>>()
    val prayer: LiveData<Resource<List<PrayerTimeData>>> = _prayer

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> = _location

    fun getPrayer(lat: Double, long: Double) {
        val cal = Calendar.getInstance()
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

    fun setLocation(location: Location) {
        _location.postValue(location)
    }

    fun startTimer() {
        countDownTimer?.start()
    }
}