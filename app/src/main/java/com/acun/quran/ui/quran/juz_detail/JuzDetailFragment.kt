package com.acun.quran.ui.quran.juz_detail

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.acun.quran.R
import com.acun.quran.data.local.datastore.LastReadVerse
import com.acun.quran.data.local.datastore.VersePreference
import com.acun.quran.data.remote.response.Resource
import com.acun.quran.data.remote.response.surah.Verse
import com.acun.quran.databinding.FragmentJuzDetailBinding
import com.acun.quran.ui.quran.surah_detail.VerseListAdapter
import com.acun.quran.util.SnackBarOnClickListener
import com.acun.quran.util.customToast
import com.acun.quran.util.toGone
import com.acun.quran.util.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JuzDetailFragment : Fragment() {

    private var _binding: FragmentJuzDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JuzDetailViewModel by viewModels()
    private val navArgs: JuzDetailFragmentArgs by navArgs()

    private var lastReadVerse: LastReadVerse? = null

    private val verses = mutableListOf<Verse>()
    private lateinit var verseAdapter: VerseListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJuzDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verseAdapter = VerseListAdapter(verses, VersePreference(
            transliteration = true,
            translation = true,
            textSizePos = 1
        ), object : VerseListAdapter.OnClickListener {
            override fun onPlayButtonClicked(item: Verse, position: Int) {
                MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    setDataSource(item.audio.primary)
                    prepare()
                    start()
                }
            }

            override fun onShareButtonClicked(item: Verse) {
                findNavController().navigate(JuzDetailFragmentDirections.actionJuzDetailFragmentToShareFragment(item))
            }

            override fun onSaveButtonClicked(item: LastReadVerse) {
                val temp = lastReadVerse
                viewModel.setLastRead(item)
                customToast(view, "Marked as last read.", object :
                    SnackBarOnClickListener {
                    override fun onClicked() {
                        temp?.let { last -> viewModel.setLastRead(last) }
                    } })
            }
        })

        with(binding) {
            rvJuzVerse.layoutManager = LinearLayoutManager(requireContext())
            rvJuzVerse.adapter = verseAdapter
            toolbar.title = requireContext().getString(R.string.juz, navArgs.juz.juz)
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        viewModel.lastRead.observe(viewLifecycleOwner) { lastReadVerse = it }
        viewModel.versePreference.observe(viewLifecycleOwner) {
            verseAdapter.setPreference(it)
        }

        verses.ifEmpty { viewModel.getJuzDetail(navArgs.juz.juz) }
        viewModel.juz.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> binding.loadingAnimation.toVisible()
                is Resource.Success -> {
                    binding.loadingAnimation.toGone()
                    resource.data?.let { juzDetail ->
                        val pos = mutableListOf<Int>()
                        juzDetail.verses.forEachIndexed { i, verse ->
                            if ((verse.number.inSurah != 1 && i == 0) || verse.number.inSurah == 1) {
                                pos.add(pos.size)
                                verses.add(verse.copy(
                                    headerName = navArgs.juz.surah[pos.lastIndex].name,
                                    numberOfVerse = "",
                                    surahNameTranslation = "",
                                    surahName = navArgs.juz.surah[pos.lastIndex].name)
                                )
                            } else {
                                verses.add(verse.copy(
                                    headerName = "",
                                    numberOfVerse = "",
                                    surahNameTranslation = "",
                                    surahName = navArgs.juz.surah[pos.lastIndex].name)
                                )
                            }
                        }
                        var saveIndex = -1
                        verses.firstOrNull { it.number.inQuran == lastReadVerse?.numberInQuran}?.let {
                            saveIndex = verses.indexOf(it)
                            verses[saveIndex].isBookmark = true
                        }
                        verseAdapter.setVerseList(verses)
                        verseAdapter.setSavePosition(saveIndex)
                        binding.rvJuzVerse.scrollToPosition(navArgs.pos)
                    }
                }
                is Resource.Failed -> binding.loadingAnimation.toGone()
            }
        }
    }
}