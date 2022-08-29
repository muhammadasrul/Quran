package com.acun.quran.ui.quran.juz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acun.quran.R
import com.acun.quran.data.remote.response.juz_list.Juz
import com.acun.quran.databinding.ItemJuzListBinding

class JuzListAdapter(
    private val juzList: List<Juz>,
    private val onItemClickListener: OnItemClickListener
): RecyclerView.Adapter<JuzListAdapter.JuzListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JuzListViewHolder {
        val binding = ItemJuzListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JuzListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JuzListViewHolder, position: Int) {
        holder.bind(juzList[position])
    }

    override fun getItemCount(): Int = juzList.size

    inner class JuzListViewHolder(private val binding: ItemJuzListBinding): RecyclerView.ViewHolder(binding.root) {
        private val ctx = binding.root.context
        fun bind(item: Juz) {
            with(binding) {
                tvJuz.text = ctx.getString(R.string.juz, item.juz)
                tvNumberOfVerses.text = ctx.getString(R.string.juz_number_of_verses, item.totalVerses)
                rvSurah.layoutManager = LinearLayoutManager(ctx)
                rvSurah.adapter = JuzSurahListAdapter(item, object : JuzSurahListAdapter.OnItemClickListener {
                    override fun onItemClicked(item: Juz, pos: Int) {
                        onItemClickListener.onItemClicked(item, pos)
                    }
                })
                root.setOnClickListener {
                    onItemClickListener.onItemClicked(item, 0)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(item: Juz, pos: Int)
    }
}