package com.acun.quranicplus.ui.screen.home

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.data.remote.response.prayer.model.Prayer
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
    private var _isTimerStarted = MutableLiveData(false)
    val isTimerStarted = _isTimerStarted

    private val _timeMap = MutableLiveData<Map<Int, String>>()
    val timeMap: LiveData<Map<Int, String>> = _timeMap

    fun setInitialTime(time: Long) {
        initialTime.value = time
        countDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(timeInMiles: Long) {
                val hour = timeInMiles / (1000 * 60 * 60)
                val minute = timeInMiles / (1000 * 60) % 60
                val second = (timeInMiles / 1000) % 60
                fun timeFormat(time: Long) = String.format("%02d", time)

                _timeMap.value = mapOf(
                    Calendar.HOUR to timeFormat(hour),
                    Calendar.MINUTE to timeFormat(minute),
                    Calendar.SECOND to timeFormat(second)
                )
            }

            override fun onFinish() {
                stopTimer()
            }
        }
    }

    private val _prayerList = MutableLiveData<PrayerState>()
    val prayerList: LiveData<PrayerState> = _prayerList

    private val _locationString = MutableLiveData<String>()
    val locationString: LiveData<String> = _locationString

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
            ).collect {
                _prayerList.postValue(PrayerState(isLoading = true))

                when (it) {
                    is Resource.Loading -> _prayerList.postValue(
                        PrayerState(isLoading = true)
                    )
                    is Resource.Failed -> _prayerList.postValue(
                        PrayerState(isLoading = false, isError = true)
                    )
                    is Resource.Success -> {
                        _prayerList.postValue(PrayerState(isLoading = false, isError = false, prayerList = it.data ?: emptyList()))
                    }
                }
            }
        }
    }

    fun setLocation(location: String) {
        _locationString.postValue(location)
    }

    fun startTimer() {
        _isTimerStarted.value = true
        countDownTimer?.start()
    }

    fun stopTimer() {
        _isTimerStarted.value = false
        countDownTimer?.cancel()
    }

    fun updatePrayer(prayer: Prayer) {
        viewModelScope.launch {
            repository.updateLocalPrayer(prayer)
        }
    }
}