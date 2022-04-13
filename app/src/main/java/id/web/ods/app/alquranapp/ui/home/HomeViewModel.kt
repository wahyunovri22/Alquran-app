package id.web.ods.app.alquranapp.ui.home

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

class HomeViewModel(private var context: Context):ViewModel() {

    var responseSurah = MutableLiveData<ResponseData>()
    var listSurah = MutableLiveData<ArrayList<ResultData>>()

    fun getDataSurah(){
        listSurah.value?.clear()
        (context as MainActivity).onLoading()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val postRequest = ApiRepository().getAllSurah()
                val response = postRequest.await()
                (context as MainActivity).onDismiss()
                if (response.isSuccessful){
                    responseSurah.value = response.body()
                }
            } catch (e: Exception) {
                (context as MainActivity).onDismiss()
                (context as MainActivity).error(e.message?:"-")
                Log.d("surah",e.message.toString())
            }
        }
    }
}