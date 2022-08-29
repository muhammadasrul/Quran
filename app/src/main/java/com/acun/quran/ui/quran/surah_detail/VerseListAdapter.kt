package com.acun.quran.ui.quran.surah_detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acun.quran.data.remote.response.surah.Verse
import com.acun.quran.databinding.ItemVerseListBinding

class VerseListAdapter(
    private val verseList: List<Verse>,
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
                tvArab.text = item.text.arab
                tvTranslation.text = item.text.transliteration.en
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