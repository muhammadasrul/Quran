package com.acun.quranicplus.ui.quran.juz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.data.remote.response.juz_list.Juz
import com.acun.quranicplus.databinding.FragmentSurahBinding
import com.acun.quranicplus.ui.quran.QuranFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JuzFragment : Fragment() {

    private var _binding: FragmentSurahBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JuzViewModel by viewModels()

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
        viewModel.juzList.observe(viewLifecycleOwner) { resource ->

            when(resource) {
                is Resource.Loading -> binding.loadingAnimation.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.loadingAnimation.visibility = View.GONE
                    resource.data?.let {
                        binding.rvSurah.adapter = JuzListAdapter(it, object : JuzListAdapter.OnItemClickListener {
                            override fun onItemClicked(item: Juz, pos: Int) {
                                findNavController().navigate(QuranFragmentDirections.actionQuranFragmentToJuzDetailFragment(item, pos))
                            }
                        })
                    }
                }
                is Resource.Failed -> binding.loadingAnimation.visibility = View.VISIBLE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.rvSurah.scrollToPosition(0)
    }
}