package com.acun.quran.ui.quran.juz_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.acun.quran.R
import com.acun.quran.data.local.datastore.LastReadVerse
import com.acun.quran.data.remote.response.juz_list.JuzSurah
import com.acun.quran.data.remote.response.surah.Verse
import com.acun.quran.databinding.FragmentJuzDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JuzDetailFragment : Fragment() {

    private var _binding: FragmentJuzDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JuzDetailViewModel by viewModels()
    private val navArgs: JuzDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJuzDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvJuzVerse.layoutManager = LinearLayoutManager(requireContext())
        binding.toolbar.title = requireContext().getString(R.string.juz, navArgs.juz.juz)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getJuzDetail(navArgs.juz.juz)
        viewModel.juz.observe(viewLifecycleOwner) {
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

            binding.rvJuzVerse.apply {
                adapter = JuzVerseListAdapter(juzSurah.toList(), object : JuzVerseListAdapter.OnItemClickListener {
                    override fun onItemClicked(lastRead: LastReadVerse) {
                        viewModel.setLastRead(lastRead)
                        Toast.makeText(requireContext(), "Marked as last read.", Toast.LENGTH_SHORT).show()
                    }
                })
                scrollToPosition(navArgs.pos)
            }
        }
    }
}