package com.acun.quranicplus.ui.quran.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.acun.quranicplus.ui.compose.QuranDetailScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SurahDetailFragment : Fragment() {

    private lateinit var composeView: ComposeView

    private val viewModel: DetailViewModel by viewModels()
    private val navArgs by navArgs<SurahDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val surah = navArgs.surah
        
        composeView.apply {
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