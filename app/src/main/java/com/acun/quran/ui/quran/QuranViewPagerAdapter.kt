package com.acun.quran.ui.quran

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.acun.quran.R
import com.acun.quran.ui.quran.juz.JuzFragment
import com.acun.quran.ui.quran.surah.SurahFragment

class QuranViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    private val titleList = listOf(R.string.surah_title, R.string.juz_title)

    override fun getItemCount(): Int = titleList.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SurahFragment()
            1 -> JuzFragment()
            else -> SurahFragment()
        }
    }
}