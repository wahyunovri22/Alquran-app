package id.web.ods.app.alquranapp.ui.detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.web.ods.app.alquranapp.response.ResponseData
import id.web.ods.app.alquranapp.response.ResultData
import id.web.ods.app.alquranapp.retrofit.ApiRepository
import id.web.ods.app.alquranapp.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailSurahViewModel(var context: Context):ViewModel() {

    var clicked = MutableLiveData<Boolean>()
    var responseSurah = MutableLiveData<ResponseData>()
    var listSurah = MutableLiveData<ArrayList<ResultData>>()

    init {
        clicked.value = false
    }

    fun getAyat(no:String){
        listSurah.value?.clear()
        (context as DetailSurahActivity).onLoading()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val postRequest = ApiRepository().getAyat(no)
                val response = postRequest.await()
                (context as DetailSurahActivity).onDismiss()
                if (response.isSuccessful){
                    responseSurah.value = response.body()
                }
            } catch (e: Exception) {
                (context as DetailSurahActivity).onDismiss()
                (context as DetailSurahActivity).error(e.message?:"-")
                Log.d("ayat",e.message.toString())
            }
        }
    }
}