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
import com.acun.quran.data.remote.response.juz_list.JuzSurah
import com.acun.quran.data.remote.response.surah.Verse
import com.acun.quran.databinding.FragmentJuzDetailBinding
import com.acun.quran.util.SnackBarOnClickListener
import com.acun.quran.util.customToast
import com.acun.quran.util.hide
import com.acun.quran.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JuzDetailFragment : Fragment() {

    private var _binding: FragmentJuzDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JuzDetailViewModel by viewModels()
    private val navArgs: JuzDetailFragmentArgs by navArgs()

    private var lastReadVerse: LastReadVerse? = null
    private var versePreference: VersePreference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJuzDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvJuzVerse.layoutManager = LinearLayoutManager(requireContext())
            toolbar.title = requireContext().getString(R.string.juz, navArgs.juz.juz)
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        viewModel.lastRead.observe(viewLifecycleOwner) { lastReadVerse = it }
        viewModel.versePreference.observe(viewLifecycleOwner) { versePreference = it}
        viewModel.getJuzDetail(navArgs.juz.juz)
        viewModel.juz.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> binding.loadingAnimation.show()
                is Resource.Success -> {
                    binding.loadingAnimation.hide()
                    resource.data?.let {
                        val juzSurah = mutableListOf<JuzSurah>()

                        val versesMap = mutableMapOf<Int, List<Verse>>()
                        val verses = mutableListOf<Verse>()
                        it.verses.forEachIndexed { i, verse ->
                            if ((i != 0 && verse.number.inSurah == 1) || it.verses.lastIndex == i) {
                                if (versesMap.isEmpty()) {
                                    versesMap[0] = verses.toList()
                                } else {
                                    versesMap[versesMap.size] = verses.toList()
                                }
                                verses.clear()
                            }
                            verses.add(verse)
                        }

                        navArgs.juz.surah.forEachIndexed { i, surah ->
                            juzSurah.add(
                                JuzSurah(
                                    end = surah.end,
                                    name = surah.name,
                                    name_arab = surah.name_arab,
                                    no = surah.no,
                                    start = surah.start,
                                    verses = versesMap[i]
                                )
                            )
                        }

                        versePreference?.let {
                            binding.rvJuzVerse.apply {
                                adapter = JuzVerseListAdapter(juzSurah.toList(), it, object : JuzVerseListAdapter.OnItemClickListener {
                                    override fun onItemClicked(lastRead: LastReadVerse) {
                                        val temp = lastReadVerse
                                        viewModel.setLastRead(lastRead)
                                        customToast(view, "Marked as last read.", object : SnackBarOnClickListener {
                                            override fun onClicked() {
                                                temp?.let { last -> viewModel.setLastRead(last) }
                                            }
                                        })
                                    }

                                    override fun onPlayButtonClicked(item: Verse) {
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
                                })
                                scrollToPosition(navArgs.pos)
                            }
                        }
                    }
                }
                is Resource.Failed -> binding.loadingAnimation.hide()
            }
        }
    }
}