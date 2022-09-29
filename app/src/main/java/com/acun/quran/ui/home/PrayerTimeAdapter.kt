package com.acun.quran.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acun.quran.data.remote.response.prayer.model.Prayer
import com.acun.quran.databinding.ItemPrayerBinding

class PrayerTimeAdapter(private val prayerTimeList: List<Prayer>): RecyclerView.Adapter<PrayerTimeAdapter.PrayerTimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayerTimeViewHolder {
        val binding = ItemPrayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PrayerTimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PrayerTimeViewHolder, position: Int) {
        holder.bind(prayerTimeList[position])
    }

    override fun getItemCount(): Int = prayerTimeList.size

    class PrayerTimeViewHolder(private val binding: ItemPrayerBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(prayer: Prayer) {
            with(binding) {
                tvPrayerTime.text = prayer.time
                tvPrayerName.text = prayer.name
            }
        }
    }
}