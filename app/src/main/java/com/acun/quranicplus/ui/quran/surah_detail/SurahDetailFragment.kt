package com.acun.quranicplus.ui.quran.surah_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.acun.quranicplus.databinding.FragmentSurahDetailBinding
import com.acun.quranicplus.ui.compose.QuranDetailScreen
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
        
        binding.composeView.apply { 
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent { 
                MaterialTheme {
                    QuranDetailScreen(
                        surahNavArgs = surah,
                        viewModel = viewModel,
                        onBackPressed = { findNavController().navigateUp() },
                        onShareClicked = { findNavController().navigate(SurahDetailFragmentDirections.actionSurahDetailFragmentToShareFragment(it))}
                    )
                }
            }
        }
    }

}