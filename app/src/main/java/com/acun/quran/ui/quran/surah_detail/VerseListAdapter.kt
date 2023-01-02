package com.acun.quran.ui.quran.surah_detail

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.acun.quran.data.local.datastore.VersePreference
import com.acun.quran.data.remote.response.surah.Verse
import com.acun.quran.databinding.ItemVerseListBinding
import com.acun.quran.util.toGone
import com.acun.quran.util.toVisible
import com.acun.quran.util.setVisibility

class VerseListAdapter(
    private var verseList: List<Verse>,
    private var preference: VersePreference,
    private val onClickListener: OnClickListener
): RecyclerView.Adapter<VerseListAdapter.VerseListViewHolder>() {

    fun setVerseList(verseList: List<Verse>) {
        this.verseList = verseList
        notifyItemRangeChanged(0, this.verseList.size)
    }

    fun setPreference(preference: VersePreference) {
        this.preference = preference
    }

    var itemPosition = -1
    var isPlay = false

    fun setNewPosition(position: Int) {
        isPlay = if (itemPosition == position) !isPlay else true
        notifyItemChanged(itemPosition)
        itemPosition = position
        notifyItemChanged(itemPosition)
    }

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
                tvTransliteration.setVisibility(preference.transliteration)
                tvTranslation.setVisibility(preference.translation)

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

                if (item.headerName.isNotEmpty()) {
                    tvSurahName.toVisible()
                    tvSurahName.text = item.surahName
                } else tvSurahName.toGone()

                tvArab.text = item.text.arab
                tvTransliteration.text = item.text.transliteration.en
                tvTranslation.text = item.translation.en
                tvVerseNumber.text = item.number.inSurah.toString()
                if (adapterPosition == verseList.lastIndex) {
                    line.toGone()
                    root.updatePadding(bottom = 162)
                } else root.updatePadding(bottom = 0)
                tvArab.setOnClickListener {
                    onClickListener.onItemClicked(item)
                }

//                btnPlay.load(if (isPlay) R.drawable.ic_pause else R.drawable.ic_play)
//                btnPlay.setOnClickListener {
//                    isPlay = if (itemPosition == adapterPosition) !isPlay else true
//                    itemPosition = adapterPosition
//                    btnPlay.load(if (isPlay) R.drawable.ic_pause else R.drawable.ic_play)
//                    onClickListener.onPlayButtonClicked(item, adapterPosition)
//                }

                imageShare.setOnClickListener {
                    onClickListener.onShareButtonClicked(item)
                }
            }
        }
    }

    interface OnClickListener {
        fun onItemClicked(item: Verse)
        fun onPlayButtonClicked(item: Verse, position: Int)
        fun onShareButtonClicked(item: Verse)
    }
}