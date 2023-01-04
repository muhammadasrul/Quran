package com.acun.quran.ui.quran.surah_detail

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.acun.quran.R
import com.acun.quran.data.local.datastore.LastReadVerse
import com.acun.quran.data.local.datastore.VersePreference
import com.acun.quran.data.remote.response.Resource
import com.acun.quran.data.remote.response.surah.Verse
import com.acun.quran.databinding.FragmentSurahDetailBinding
import com.acun.quran.util.SnackBarOnClickListener
import com.acun.quran.util.customToast
import com.acun.quran.util.toGone
import com.acun.quran.util.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SurahDetailFragment : Fragment() {

    private var _binding : FragmentSurahDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SurahDetailViewModel by viewModels()
    private val navArgs by navArgs<SurahDetailFragmentArgs>()

    private var lastReadVerse: LastReadVerse? = null
    private var versePreference: VersePreference? = null

    private lateinit var mediaPlayer: MediaPlayer
    private var lastVersePosition: Int = -1
    private var verseList = listOf<Verse>()

    private lateinit var verseAdapter: VerseListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurahDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val surah = navArgs.surah

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
        }

        verseAdapter = VerseListAdapter(listOf(), VersePreference(
            transliteration = true,
            translation = true,
            textSizePos = 1
        ), object : VerseListAdapter.OnClickListener {
            override fun onItemClicked(item: Verse) {
                val temp = lastReadVerse
                viewModel.setLastRead(LastReadVerse(ayah = item.number.inSurah, surah = surah.name.transliteration.en))
                customToast(view, "Marked as last read.", object : SnackBarOnClickListener {
                    override fun onClicked() {
                        temp?.let { last -> viewModel.setLastRead(last) }
                    }
                })
            }

            override fun onPlayButtonClicked(item: Verse, position: Int) {
                mediaPlayer.setOnBufferingUpdateListener { mediaPlayer, _ ->
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            var current = 0
                            while (mediaPlayer.isPlaying) {
                                val progress = (mediaPlayer.currentPosition.toFloat()/mediaPlayer.duration.toFloat()).times(100).toInt()
                                if (current != progress) {
                                    viewModel.setAudioProgress(progress)
                                    current = progress
                                }
                            }
                        }
                    }
                }

                mediaPlayer.setOnCompletionListener {
                    verseAdapter.setNewPosition(position)
                    mediaPlayer.reset()
                    if (lastVersePosition != verseList.size) {
                        lastVersePosition++
                    }
                    if (lastVersePosition == verseList.size) return@setOnCompletionListener
                    mediaPlayer.setDataSource(verseList[lastVersePosition].audio.primary)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
//                    buttonView.setImageResource(R.drawable.ic_play)
                }
                if (lastVersePosition != -1) {
                    if (lastVersePosition != position) {
                        mediaPlayer.reset()
                        mediaPlayer.setDataSource(verseList[position].audio.primary)
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                        lastVersePosition = position
//                        buttonView.setImageResource(R.drawable.ic_pause)
                    } else {
                        if (mediaPlayer.isPlaying) {
                            mediaPlayer.pause()
//                            buttonView.setImageResource(R.drawable.ic_play)
                        } else {
                            mediaPlayer.start()
//                            buttonView.setImageResource(R.drawable.ic_pause)
                        }
                    }
                } else {
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(verseList[position].audio.primary)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    lastVersePosition = position
//                    buttonView.setImageResource(R.drawable.ic_pause)
                }


//                binding.playerSurahName.text = "${verseList[lastVersePosition].surahName} (${verseList[lastVersePosition].number}/${verseList.size})"
            }

            override fun onShareButtonClicked(item: Verse) {
                findNavController().navigate(SurahDetailFragmentDirections.actionSurahDetailFragmentToShareFragment(item, surah))
            }
        })

        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            rvVerse.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = verseAdapter
            }
            toolbar.title = surah.name.transliteration.en
//            tvSurahName.text = surah.name.transliteration.en
//            tvSurahNameTranslation.text = requireContext().getString(R.string.surah_detail_verse_name, surah.name.translation.en)
//            tvNumberOfVerses.text = requireContext().getString(R.string.number_of_verses, surah.numberOfVerses)
        }

        viewModel.lastRead.observe(viewLifecycleOwner) { lastReadVerse = it }
        viewModel.versePreference.observe(viewLifecycleOwner) {versePreference = it}

        verseList.ifEmpty { viewModel.getSurah(surah.number) }
        viewModel.surahDetail.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> binding.loadingAnimation.toVisible()
                is Resource.Success -> {
                    resource.data?.let { surah ->
                        verseList = surah.verses
                        if (verseList.isNotEmpty()) {
                            verseList[0].surahName = navArgs.surah.name.transliteration.en
                            verseList[0].surahNameTranslation = requireContext().getString(R.string.surah_detail_verse_name, surah.name.translation.en)
                            verseList[0].numberOfVerse = requireContext().getString(R.string.number_of_verses, surah.numberOfVerses)
                        }
                        verseAdapter.setVerseList(surah.verses)
                        binding.loadingAnimation.toGone()
                    }
                }
                is Resource.Failed -> binding.loadingAnimation.toGone()
            }
        }

        viewModel.versePreference.observe(viewLifecycleOwner) { verseAdapter.setPreference(it) }
        viewModel.audioProgress.observe(viewLifecycleOwner) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        mediaPlayer.release()
    }
}