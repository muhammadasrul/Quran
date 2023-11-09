package com.acun.quranicplus.ui.screen.home

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.data.remote.response.prayer.model.Prayer
import com.acun.quranicplus.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: QuranRepository
) : ViewModel() {
    private var countDownTimer: CountDownTimer? = null

    private var initialTime = MutableStateFlow(0L)
    private var _isTimerStarted = MutableStateFlow(false)
    val isTimerStarted = _isTimerStarted.asStateFlow()

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    fun setInitialTime(time: Long) {
        initialTime.update { time }
        countDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(timeInMiles: Long) {
                val hour = timeInMiles / (1000 * 60 * 60)
                val minute = timeInMiles / (1000 * 60) % 60
                val second = (timeInMiles / 1000) % 60
                fun timeFormat(time: Long) = String.format("%02d", time)

                val prayerTime = PrayerTime(
                    hour = timeFormat(hour),
                    minute = timeFormat(minute),
                    second = timeFormat(second)
                )
                _homeState.update { it.copy(time = prayerTime) }
            }

            override fun onFinish() = stopTimer()
        }
    }

    private val _locationString = MutableStateFlow("")
    val locationString = _locationString.asStateFlow()

    fun getPrayer(lat: Double, long: Double) {
        val cal = Calendar.getInstance()
        val month = cal.get(Calendar.MONTH) + 1
        val year = cal.get(Calendar.YEAR)
        viewModelScope.launch {
            repository.getPrayer(
                latitude = lat,
                longitude = long,
                methode = 2,
                month = month,
                year = year
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _homeState.update { it.copy(isLoading = true) }
                    is Resource.Failed -> _homeState.update {
                        it.copy(
                            isLoading = false,
                            isError = true
                        )
                    }

                    is Resource.Success -> _homeState.update {
                        it.copy(
                            isLoading = false,
                            isError = false,
                            prayerList = resource.data ?: emptyList()
                        )
                    }
                }
            }
        }
    }

    fun setLocation(location: String) {
        _locationString.update { location }
    }

    fun startTimer() {
        _isTimerStarted.update { true }
        countDownTimer?.start()
    }

    fun stopTimer() {
        _isTimerStarted.update { false }
        countDownTimer?.cancel()
    }

    fun updatePrayer(prayer: Prayer) {
        viewModelScope.launch {
            repository.updateLocalPrayer(prayer)
        }
    }
}