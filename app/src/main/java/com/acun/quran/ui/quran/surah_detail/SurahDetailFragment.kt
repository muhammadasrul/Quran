package com.acun.quran.ui.quran.surah_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.acun.quran.R
import com.acun.quran.data.local.datastore.LastReadVerse
import com.acun.quran.data.remote.response.surah.Verse
import com.acun.quran.databinding.FragmentSurahDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SurahDetailFragment : Fragment() {

    private var _binding : FragmentSurahDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SurahDetailViewModel by viewModels()
    private val navArgs by navArgs<SurahDetailFragmentArgs>()

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
            tvSurahNameArab.text = surah.name.short
            tvNumberOfVerses.text = requireContext().getString(R.string.number_of_verses, surah.numberOfVerses)
        }

        viewModel.getSurah(surah.number)
        viewModel.surahDetail.observe(viewLifecycleOwner) {
            binding.rvVerse.adapter = VerseListAdapter(it.verses, object : VerseListAdapter.OnItemClickListener {
                override fun onItemClicked(item: Verse) {
                    viewModel.setLastRead(LastReadVerse(ayah = item.number.inSurah, surah = surah.name.transliteration.en))
                    Toast.makeText(requireContext(), "Marked as last read.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}