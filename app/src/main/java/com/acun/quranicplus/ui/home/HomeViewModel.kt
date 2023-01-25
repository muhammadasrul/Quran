package com.acun.quranicplus.ui.home

import android.location.Location
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.data.remote.response.prayer.PrayerTimeData
import com.acun.quranicplus.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: QuranRepository
): ViewModel() {
    private var countDownTimer: CountDownTimer? = null

    private var initialTime = MutableLiveData<Long>()
    private var currentTime = MutableLiveData<Long>()
    private var _isTimerStarted = MutableLiveData(false)
    val isTimerStarted = _isTimerStarted

    val timeString = Transformations.map(currentTime) {
        val hour = it / (1000 * 60 * 60)
        val minute = it / (1000 * 60) % 60
        val second = (it / 1000) % 60
        fun timeFormat(time: Long) = String.format("%02d", time)

        "in ${timeFormat(hour)}h ${timeFormat(minute)}m ${timeFormat(second)}s"
    }

    fun setInitialTime(time: Long) {
        initialTime.value = time
        currentTime.value = time
        countDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(timeInMiles: Long) {
                currentTime.value = timeInMiles
            }

            override fun onFinish() {
                stopTimer()
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
        _isTimerStarted.value = true
        countDownTimer?.start()
    }

    fun stopTimer() {
        _isTimerStarted.value = false
        countDownTimer?.cancel()
    }

    private val _compassDegree = MutableLiveData(0f)
    val compassDegree: LiveData<Float> = _compassDegree

    private val _kaabaDegree = MutableLiveData(0f)
    val kaabaDegree: LiveData<Float> = _kaabaDegree

    fun setCompassDegree(degree: Float) {
        _compassDegree.value = degree
    }

    fun setKaabaDegree(degree: Float) {
        _kaabaDegree.value = degree
    }
}