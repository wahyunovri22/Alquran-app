package id.web.ods.app.alquranapp.retrofit

class ApiRepository() {

    private var apiService: ApiService = ApiConfig.getApiService()

    fun getAllSurah() = apiService.getAllSurah()
    fun getAyat(no:String) = apiService.getAyat(no)
}