package com.acun.quran.ui.quran.juz_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acun.quran.data.local.datastore.LastReadVerse
import com.acun.quran.data.remote.response.juz_list.JuzSurah
import com.acun.quran.data.remote.response.surah.Verse
import com.acun.quran.databinding.ItemJuzListBinding
import com.acun.quran.ui.quran.surah_detail.VerseListAdapter

class JuzVerseListAdapter(
    private val juzSurah: List<JuzSurah>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<JuzVerseListAdapter.SurahListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahListViewHolder {
        val binding = ItemJuzListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SurahListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SurahListViewHolder, position: Int) {
        holder.bind(juzSurah[position])
    }

    override fun getItemCount(): Int = juzSurah.size

    inner class SurahListViewHolder(private val binding: ItemJuzListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val ctx = binding.root.context
        fun bind(juzSurah: JuzSurah) {
            with(binding) {
                tvJuz.text = juzSurah.name
                tvJuz.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    endToEnd = ConstraintSet.PARENT_ID
                }
                tvJuz.updatePadding(bottom = 16)
                tvJuz.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                rvSurah.layoutManager = LinearLayoutManager(ctx)
                juzSurah.verses?.let {
                    rvSurah.adapter = VerseListAdapter(it, object : VerseListAdapter.OnItemClickListener {
                        override fun onItemClicked(item: Verse) {
                            onItemClickListener.onItemClicked(LastReadVerse(ayah = item.number.inSurah, surah = juzSurah.name))
                        }
                    })
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(lastRead: LastReadVerse)
    }
}