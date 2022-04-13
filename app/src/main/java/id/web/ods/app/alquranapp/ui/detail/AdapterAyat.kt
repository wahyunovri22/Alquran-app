package id.web.ods.app.alquranapp.ui.detail

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.web.ods.app.alquranapp.databinding.RowAyatBinding
import id.web.ods.app.alquranapp.response.ResultData

class AdapterAyat: RecyclerView.Adapter<AdapterAyat.Holder>() {

    lateinit var listData:ArrayList<ResultData>

    fun setData(listData:ArrayList<ResultData>){
        this.listData = listData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
       val binding = RowAyatBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class Holder(private val binding:RowAyatBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data:ResultData){
            binding.tvNo.text = data.nomor
            binding.tvNamaAyat.text = data.ar
            binding.tvNamaAyatIndo.text = Html.fromHtml(data.tr)
            binding.tvKeterangan.text = data.id
        }
    }
}