package com.acun.quran.ui.quran.juz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acun.quran.data.remote.response.juz_list.Juz
import com.acun.quran.data.remote.response.juz_list.JuzSurah
import com.acun.quran.databinding.ItemSurahListBinding

class JuzSurahListAdapter(
    private val juz: Juz,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<JuzSurahListAdapter.SurahListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahListViewHolder {
        val binding =
            ItemSurahListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SurahListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SurahListViewHolder, position: Int) {
        holder.bind(juz.surah[position])
    }

    override fun getItemCount(): Int = juz.surah.size

    inner class SurahListViewHolder(private val binding: ItemSurahListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: JuzSurah) {
            with(binding) {
                tvSurahName.text = item.name
                tvSurahNameArab.text = item.name_arab
                tvSurahNumber.text = item.no.toString()
                tvNumberOfVerses.text = "${item.start} - ${item.end}"
                root.setOnClickListener {
                    onItemClickListener.onItemClicked(juz, adapterPosition)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(item: Juz, pos: Int)
    }
}