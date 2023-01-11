package com.acun.quranicplus.ui.preference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.acun.quranicplus.BuildConfig
import com.acun.quranicplus.R
import com.acun.quranicplus.data.local.datastore.VersePreference
import com.acun.quranicplus.databinding.FragmentPreferenceBinding
import com.acun.quranicplus.ui.compose.PreferenceScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreferenceFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentPreferenceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PreferenceViewModel by viewModels()
    private var textSizeArr = arrayOf<String>()
    private var textSize: String = ""
    private var translation: Boolean = false
    private var transliteration: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreferenceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    PreferenceScreen(viewModel = viewModel)
                }
            }
        }

        textSizeArr = resources.getStringArray(R.array.text_size)
        viewModel.versePreference.observe(viewLifecycleOwner) {
//            binding.switchTranslation.isChecked = it.translation
//            binding.switchTransliteration.isChecked = it.transliteration
//            binding.spinnerTextSize.setSelection(it.textSizePos)

            translation = it.translation
            transliteration = it.transliteration
            textSize = if (it.textSizePos != -1) textSizeArr[it.textSizePos] else textSizeArr[1]
        }

//        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, textSizeArr)
//        binding.spinnerTextSize.adapter = spinnerAdapter
//        binding.spinnerTextSize.onItemSelectedListener = this

//        binding.switchTranslation.setOnCheckedChangeListener { _, b ->
//            translation = b
//        }
//
//        binding.switchTransliteration.setOnCheckedChangeListener { _, b ->
//            transliteration = b
//        }

        binding.tvVersion.text = BuildConfig.VERSION_NAME
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, long: Long) {
        textSize = textSizeArr[pos]
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) = Unit

    override fun onPause() {
        super.onPause()
        viewModel.setVersePreference(VersePreference(
            transliteration = transliteration,
            translation = translation,
            textSizePos = textSizeArr.indexOf(textSize)
        ))
    }
}