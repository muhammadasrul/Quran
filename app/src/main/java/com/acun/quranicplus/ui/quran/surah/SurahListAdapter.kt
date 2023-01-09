package com.acun.quranicplus.ui.quran.surah

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.acun.quranicplus.R
import com.acun.quranicplus.data.remote.response.surah_list.Surah
import com.acun.quranicplus.databinding.ItemSurahListBinding

class SurahListAdapter(
    private val surahList: List<Surah>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<SurahListAdapter.SurahListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahListViewHolder {
        val binding = ItemSurahListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SurahListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SurahListViewHolder, position: Int) {
        holder.bind(surahList[position])
    }

    override fun getItemCount(): Int = surahList.size

    inner class SurahListViewHolder(private val binding: ItemSurahListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Surah) {
            with(binding) {
                tvSurahName.text = item.name.transliteration.en
                tvSurahNameArab.text = item.name.short
                tvSurahNumber.text = item.number.toString()
                tvNumberOfVerses.text = binding.root.context.getString(R.string.number_of_verses, item.numberOfVerses)
                root.setOnClickListener {
                    onItemClickListener.onItemClicked(item)
                }

                if (adapterPosition == surahList.lastIndex) {
                    root.updatePadding(bottom = 162)
                } else root.updatePadding(bottom = 0)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(item: Surah)
    }
}