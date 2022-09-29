package com.acun.quran.ui.quran.surah_detail

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acun.quran.data.local.datastore.VersePreference
import com.acun.quran.data.remote.response.surah.Verse
import com.acun.quran.databinding.ItemVerseListBinding
import com.acun.quran.util.visibility

class VerseListAdapter(
    private val verseList: List<Verse>,
    private val preference: VersePreference,
    private val onItemClickListener: OnItemClickListener
): RecyclerView.Adapter<VerseListAdapter.VerseListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerseListViewHolder {
        val binding = ItemVerseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VerseListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VerseListViewHolder, position: Int) {
        holder.bind(verseList[position])
    }

    override fun getItemCount(): Int = verseList.size

    inner class VerseListViewHolder(private val binding: ItemVerseListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Verse) {
            with(binding) {
                tvTransliteration.visibility(preference.transliteration)
                tvTranslation.visibility(preference.translation)

                when(preference.textSizePos) {
                    0 -> {
                        tvArab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                        tvTransliteration.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                        tvTranslation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                    }
                    1 -> {
                        tvArab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
                        tvTransliteration.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                        tvTranslation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                    }
                    2 -> {
                        tvArab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
                        tvTransliteration.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                        tvTranslation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                    }
                }

                tvArab.text = item.text.arab
                tvTransliteration.text = item.text.transliteration.en
                tvTranslation.text = item.translation.en
                tvVerseNumber.text = item.number.inSurah.toString()
                if (adapterPosition == verseList.lastIndex) {
                    line.visibility = View.GONE
                }
                root.setOnClickListener {
                    onItemClickListener.onItemClicked(item)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(item: Verse)
    }
}