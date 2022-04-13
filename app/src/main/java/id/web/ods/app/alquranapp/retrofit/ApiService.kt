package id.web.ods.app.alquranapp.retrofit

import id.web.ods.app.alquranapp.response.ResponseData
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("surah")
    fun getAllSurah():Deferred<Response<ResponseData>>

    @GET("surah/{nomor}")
    fun getAyat(@Path("nomor")no:String):Deferred<Response<ResponseData>>
}