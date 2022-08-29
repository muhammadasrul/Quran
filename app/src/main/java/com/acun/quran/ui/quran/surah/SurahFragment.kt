package com.acun.quran.ui.quran.surah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.acun.quran.data.remote.response.surah_list.Surah
import com.acun.quran.databinding.FragmentSurahBinding
import com.acun.quran.ui.quran.QuranFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SurahFragment : Fragment() {

    private var _binding: FragmentSurahBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SurahViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurahBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSurah.layoutManager = LinearLayoutManager(requireContext())

        viewModel.surahList.observe(viewLifecycleOwner) {
            binding.rvSurah.adapter = SurahListAdapter(it, object : SurahListAdapter.OnItemClickListener {
                override fun onItemClicked(item: Surah) {
                    findNavController().navigate(QuranFragmentDirections.actionQuranFragmentToSurahDetailFragment(item))
                }
            })
        }
    }
}