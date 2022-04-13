package id.web.ods.app.alquranapp.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ResponseData(
    @field:SerializedName("status")
    val status :String ? = null,

    @field:SerializedName("data")
    val data: ArrayList<ResultData> = arrayListOf()
)

@Parcelize
data class ResultData(

    @field:SerializedName("arti")
    val arti :String ? = null,

    @field:SerializedName("asma")
    val asma :String ? = null,

    @field:SerializedName("ayat")
    val ayat :String ? = null,

    @field:SerializedName("nama")
    val nama :String ? = null,

    @field:SerializedName("type")
    val type :String ? = null,

    @field:SerializedName("audio")
    val audio :String ? = null,

    @field:SerializedName("nomor")
    val nomor :String ? = null,

    @field:SerializedName("keterangan")
    val keterangan :String ? = null,

    @field:SerializedName("ar")
    val ar :String ? = null,

    @field:SerializedName("id")
    val id :String ? = null,

    @field:SerializedName("tr")
    val tr :String ? = null,

    var statusopen :Int = 0
):Parcelable