package com.acun.quran.ui.quran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.acun.quran.R
import com.acun.quran.databinding.FragmentQuranBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuranFragment : Fragment() {

    private var _binding : FragmentQuranBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuranViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.lastRead.observe(viewLifecycleOwner) {
            binding.textViewLastAyah.text = if (it.ayah != 0) "Ayah no ${it.ayah}" else "you haven't read anything"
            binding.textViewLastSurah.text = it.surah
        }

        binding.quranViewPager.isUserInputEnabled = false
        binding.quranViewPager.adapter = QuranViewPagerAdapter(this)
        binding.quranViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> binding.radioButtonSurahTab.isChecked = true
                    1 -> binding.radioButtonJuzTab.isChecked = true
                }
            }
        })

        radioButtonTabSwitch(binding.quranRadioGroupTab.checkedRadioButtonId)
        binding.quranRadioGroupTab.setOnCheckedChangeListener { radioGroup, _ ->
            radioButtonTabSwitch(radioGroup.checkedRadioButtonId)
        }
    }

    private fun radioButtonTabSwitch(id: Int) {
        when (id) {
            R.id.radioButtonSurahTab -> {
                binding.quranViewPager.setCurrentItem(0, true)
                binding.radioButtonSurahTab.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_blue_gradient_rounded)
                binding.radioButtonSurahTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

                binding.radioButtonJuzTab.background = ContextCompat.getDrawable(requireContext(), R.color.white)
                binding.radioButtonJuzTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_blue))
            }
            R.id.radioButtonJuzTab -> {
                binding.quranViewPager.setCurrentItem(1, true)
                binding.radioButtonJuzTab.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_blue_gradient_rounded)
                binding.radioButtonJuzTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

                binding.radioButtonSurahTab.background = ContextCompat.getDrawable(requireContext(), R.color.white)
                binding.radioButtonSurahTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_blue))
            }
        }
    }
}