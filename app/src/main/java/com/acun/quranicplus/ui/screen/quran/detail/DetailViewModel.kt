package com.acun.quranicplus.ui.screen.quran.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.fromUri
import androidx.media3.common.Player
import com.acun.quranicplus.data.local.datastore.LastReadVerse
import com.acun.quranicplus.data.local.datastore.QuranDataStore
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.data.remote.response.Resource.Failed
import com.acun.quranicplus.data.remote.response.Resource.Loading
import com.acun.quranicplus.data.remote.response.Resource.Success
import com.acun.quranicplus.data.remote.response.surah.Verse
import com.acun.quranicplus.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: QuranRepository,
    private val dataStore: QuranDataStore,
    private val player: Player
) : ViewModel() {
    private val mediaUris = MutableLiveData<List<String>>()
    private val nextUri = MutableLiveData<String>()
    private val _currentPlayIndex = MutableLiveData<Int>()
    val currentPlayIndex: LiveData<Int> = _currentPlayIndex
    private val _isPlay = MutableLiveData<Boolean>()
    val isPlay = _isPlay
    private val _playbackState = MutableLiveData<Int>()
    val playbackState: LiveData<Int> = _playbackState

    init {
        player.prepare()
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                _playbackState.value = playbackState
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        Log.d("waduh", "playbackState: buffering $playbackState")
                    }

                    Player.STATE_ENDED -> {
                        if (mediaUris.value?.lastIndex == currentPlayIndex.value) {
                            _currentPlayIndex.value = -1
                            return
                        }
                        nextUri.value?.let { play(it) }
                    }

                    Player.STATE_IDLE -> {
                        Log.d("waduh", "playbackState: idle $playbackState")
                    }

                    Player.STATE_READY -> {
                        Log.d("waduh", "playbackState: ready $playbackState")
                    }
                }
            }
        })
    }

    private val _surahDetail = MutableLiveData<SurahDetailState>()
    val surahDetail: LiveData<SurahDetailState> = _surahDetail

    fun getSurah(number: Int) {
        viewModelScope.launch {
            repository.getSurah(number).collect {
                it.data?.verses = it.data?.verses?.map { verse ->
                    verse.copy(
                        headerName = "",
                        surahName = "",
                        numberOfVerse = "",
                        surahNameTranslation = ""
                    )
                } ?: emptyList()

                mediaUris.value = it.data?.verses?.map { verse ->
                    player.addMediaItem(fromUri(verse.audio.primary))
                    verse.audio.primary
                }

                when (it) {
                    is Success -> {
                        _surahDetail.postValue(
                            SurahDetailState(
                                surahDetail = it.data,
                                isLoading = false,
                                isError = false
                            )
                        )
                    }

                    is Failed -> {
                        _surahDetail.postValue(
                            SurahDetailState(
                                isLoading = false,
                                isError = true
                            )
                        )
                    }

                    is Loading -> {
                        _surahDetail.postValue(
                            SurahDetailState(
                                isLoading = true,
                                isError = false
                            )
                        )
                    }
                }
            }
        }
    }

    private val _juzDetail = MutableLiveData<JuzDetailState>()
    val juzDetail: LiveData<JuzDetailState> = _juzDetail

    fun getJuzDetail(number: Int) {
        viewModelScope.launch {
            repository.getJuz(number).collect {
                when (it) {
                    is Success -> {
                        mediaUris.value = it.data?.verses?.map { verse ->
                            player.addMediaItem(fromUri(verse.audio.primary))
                            verse.audio.primary
                        }

                        _juzDetail.postValue(
                            JuzDetailState(
                                juzDetail = it.data,
                                isLoading = false,
                                isError = false
                            )
                        )
                    }

                    is Failed -> {
                        _juzDetail.postValue(
                            JuzDetailState(
                                isLoading = false,
                                isError = true
                            )
                        )
                    }

                    is Loading -> {
                        _juzDetail.postValue(
                            JuzDetailState(
                                isLoading = true,
                                isError = false
                            )
                        )
                    }
                }
            }
        }
    }

    fun setLastRead(item: LastReadVerse) {
        viewModelScope.launch {
            dataStore.setLastRead(item)
        }
    }

    val lastRead = dataStore.lastRead.asLiveData()
    val versePreference = dataStore.versePreference.asLiveData()

    fun play(stringUri: String) {
        _isPlay.value = true
        mediaUris.value?.let { list ->
            val current = list.firstOrNull { it == stringUri }
            val indexOfCurrent = list.indexOf(current)
            _currentPlayIndex.value = indexOfCurrent
            if (indexOfCurrent != list.lastIndex) {
                nextUri.value = list[indexOfCurrent + 1]
            }

            current?.let { uri ->
                player.setMediaItem(fromUri(uri))
                player.play()
            }
        }
    }

    fun stop() {
        _isPlay.value = false
        player.pause()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}