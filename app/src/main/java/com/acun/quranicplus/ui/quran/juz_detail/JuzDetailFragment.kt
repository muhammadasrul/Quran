package com.acun.quranicplus.ui.quran.juz_detail

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
import com.acun.quranicplus.databinding.FragmentJuzDetailBinding
import com.acun.quranicplus.ui.compose.QuranDetailScreen
import com.acun.quranicplus.ui.quran.surah_detail.SurahDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JuzDetailFragment : Fragment() {

    private var _binding: FragmentJuzDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SurahDetailViewModel by viewModels()
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

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    QuranDetailScreen(
                        juzNavArgs = navArgs.juz,
                        juzPos = navArgs.pos,
                        viewModel = viewModel,
                        onBackPressed = { findNavController().navigateUp() },
                        onShareClicked = {
                            findNavController().navigate(JuzDetailFragmentDirections.actionJuzDetailFragmentToShareFragment(it))
                        }
                    )
                }
            }
        }
    }
}