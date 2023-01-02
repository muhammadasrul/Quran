package com.acun.quran.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout.LayoutParams
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.acun.quran.R
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

    inner class PrayerTimeViewHolder(private val binding: ItemPrayerBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(prayer: Prayer) {
            with(binding) {
                if (adapterPosition == 0) root.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    marginStart = 40
                }

                tvPrayerTime.text = prayer.time
                    .replace("[()]".toRegex(), "")
                    .replace(" ", "\n")
                tvPrayerName.text = prayer.name
                if (prayer.isNowPrayer) {
                    prayerTimeCardView.strokeWidth = 4
                    prayerTimeCardView.strokeColor = ContextCompat.getColor(root.context, R.color.primary_blue)
                }
            }
        }
    }
}