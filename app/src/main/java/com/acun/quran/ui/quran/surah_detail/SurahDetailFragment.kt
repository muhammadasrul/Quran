package com.acun.quran.ui.quran.surah_detail

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
import com.acun.quran.databinding.FragmentSurahDetailBinding
import com.acun.quran.util.SnackBarOnClickListener
import com.acun.quran.util.customToast
import com.acun.quran.util.hide
import com.acun.quran.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SurahDetailFragment : Fragment() {

    private var _binding : FragmentSurahDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SurahDetailViewModel by viewModels()
    private val navArgs by navArgs<SurahDetailFragmentArgs>()

    private var lastReadVerse: LastReadVerse? = null
    private var versePreference: VersePreference? = null

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

        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            rvVerse.layoutManager = LinearLayoutManager(requireContext())
            toolbar.title = surah.name.transliteration.en
            tvSurahName.text = surah.name.transliteration.en
            tvSurahNameTranslation.text = requireContext().getString(R.string.surah_detail_verse_name, surah.name.translation.en)
            tvNumberOfVerses.text = requireContext().getString(R.string.number_of_verses, surah.numberOfVerses)
        }

        viewModel.lastRead.observe(viewLifecycleOwner) { lastReadVerse = it }
        viewModel.versePreference.observe(viewLifecycleOwner) {versePreference = it}
        viewModel.getSurah(surah.number)
        viewModel.surahDetail.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> binding.loadingAnimation.show()
                is Resource.Success -> {
                    binding.loadingAnimation.hide()
                    resource.data?.let {
                        versePreference?.let { preference ->
                            binding.rvVerse.adapter = VerseListAdapter(it.verses, preference, object : VerseListAdapter.OnItemClickListener {
                                override fun onItemClicked(item: Verse) {
                                    val temp = lastReadVerse
                                    viewModel.setLastRead(LastReadVerse(ayah = item.number.inSurah, surah = surah.name.transliteration.en))
                                    customToast(view, "Marked as last read.", object : SnackBarOnClickListener {
                                        override fun onClicked() {
                                            temp?.let { last -> viewModel.setLastRead(last) }
                                        }
                                    })
                                }
                            })
                        }
                    }
                }
                is Resource.Failed -> binding.loadingAnimation.hide()
            }
        }
    }
}