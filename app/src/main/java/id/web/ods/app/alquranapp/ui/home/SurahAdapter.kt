package id.web.ods.app.alquranapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import id.web.ods.app.alquranapp.databinding.RowSurahBinding
import id.web.ods.app.alquranapp.response.ResultData
import id.web.ods.app.alquranapp.ui.MainActivity
import kotlin.collections.ArrayList

class SurahAdapter : RecyclerView.Adapter<SurahAdapter.Holder>(), Filterable {

    lateinit var list: ArrayList<ResultData>
    lateinit var list2: ArrayList<ResultData>
    lateinit var context: Context

    fun setData(list: ArrayList<ResultData>, context: Context) {
        this.list = list
        this.list2 = list
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RowSurahBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = list[position]
        holder.bind(data,context)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class Holder(private val binding: RowSurahBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResultData,context: Context) {
            binding.tvNo.text = data.nomor
            binding.tvNamaSurah.text = data.nama
            binding.tvNamaSurahArab.text = data.asma
            binding.tvKota.text = "${data.type} - ${data.ayat} ayat"

            binding.root.setOnClickListener {
                (context as MainActivity).goToDetail(data)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val oReturn = FilterResults()
                val results2 = ArrayList<ResultData>()

                if (list2.size > 0) {
                    for (g in list2) {
                        if (g.nama.toString().lowercase().contains(charSequence.toString())){
                            results2.add(g)
                        }
                    }
                }
                oReturn.values = results2
                return oReturn
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                list = filterResults.values as ArrayList<ResultData>
                notifyDataSetChanged()

            }
        }
    }
}