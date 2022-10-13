package com.acun.quran.ui.quran.juz_detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acun.quran.R
import com.acun.quran.data.local.datastore.LastReadVerse
import com.acun.quran.data.local.datastore.VersePreference
import com.acun.quran.data.remote.response.juz_list.JuzSurah
import com.acun.quran.data.remote.response.surah.Verse
import com.acun.quran.databinding.ItemJuzListBinding
import com.acun.quran.ui.quran.surah_detail.VerseListAdapter

class JuzVerseListAdapter(
    private val juzSurah: List<JuzSurah>,
    private val versePreference: VersePreference,
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
        fun bind(juzSurah: JuzSurah) {
            with(binding) {
                tvJuz.text = juzSurah.name
                tvJuz.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    width = 0
                    endToEnd = ConstraintSet.PARENT_ID
                    bottomToTop = R.id.rvSurah
                    bottomMargin = 18
                }
                tvJuz.updatePadding(top = 8)
                tvJuz.background = ContextCompat.getDrawable(root.context, R.drawable.bg_blue_extra_light)
                tvJuz.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                juzSurah.verses?.let {
                    rvSurah.apply {
                        layoutManager = LinearLayoutManager(rvSurah.context)
                        adapter = VerseListAdapter(it, versePreference, object : VerseListAdapter.OnClickListener {
                            override fun onItemClicked(item: Verse) {
                                onItemClickListener.onItemClicked(LastReadVerse(ayah = item.number.inSurah, surah = juzSurah.name))
                            }

                            override fun onPlayButtonClicked(item: Verse, buttonView: View) {
                                onItemClickListener.onPlayButtonClicked(item)
                            }
                        })
                    }
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(lastRead: LastReadVerse)
        fun onPlayButtonClicked(item: Verse)
    }
}