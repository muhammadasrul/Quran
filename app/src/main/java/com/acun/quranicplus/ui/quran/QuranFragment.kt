package com.acun.quranicplus.ui.quran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.acun.quranicplus.ui.compose.QuranScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuranFragment : Fragment() {

    private lateinit var composeView: ComposeView

    private val viewModel: QuranViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    QuranScreen(
                        viewModel = viewModel,
                        onSurahDetailClicked = {
                            findNavController().navigate(QuranFragmentDirections.actionQuranFragmentToSurahDetailFragment(it))
                        },
                        onJuzDetailClicked = { juz, pos ->
                            findNavController().navigate(QuranFragmentDirections.actionQuranFragmentToJuzDetailFragment(juz, pos))
                        }
                    )
                }
            }
        }
    }
}