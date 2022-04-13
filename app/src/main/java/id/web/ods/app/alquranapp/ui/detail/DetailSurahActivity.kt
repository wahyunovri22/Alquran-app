package id.web.ods.app.alquranapp.ui.detail

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.view.View
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import id.web.ods.app.alquranapp.R
import id.web.ods.app.alquranapp.databinding.ActivityDetailSurahBinding
import id.web.ods.app.alquranapp.response.ResultData
import id.web.ods.app.alquranapp.utils.Config
import id.web.ods.app.alquranapp.utils.Loading

class DetailSurahActivity : AppCompatActivity() {
    companion object {
        const val DATA = "data"
    }

    lateinit var binding:ActivityDetailSurahBinding
    lateinit var data:ResultData
    lateinit var viewModel: DetailSurahViewModel
    lateinit var loading: Loading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSurahBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.ungu)))
        main()
    }

   private fun main(){
       viewModel = DetailSurahViewModel(this)
       loading = Loading(this)
       data = intent.getParcelableExtra<ResultData>(DATA) as ResultData
       supportActionBar?.title = data.nama
       binding.tvNamaSurahArab.text = data.asma
       binding.tvPembuka.text = data.arti
       binding.tvJumlah.text = "${data.type} - ${data.ayat} ayat"
       binding.tvKeterangan.text = Html.fromHtml(data.keterangan)

       viewModel.clicked.observe(this){
           if (viewModel.clicked.value == true ){
               binding.tvKeterangan.visibility = View.VISIBLE
           }else{
               binding.tvKeterangan.visibility = View.GONE
           }
       }

       getData()

       webView()

       binding.imgShow.setOnClickListener {
           viewModel.clicked.value = viewModel.clicked.value == false
       }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun webView(){
        binding.wb.settings.javaScriptEnabled = true
        binding.wb.loadUrl(data.audio?:"-")

        binding.wb.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                super.onReceivedError(view, errorCode, description, failingUrl)
            }
        }

        binding.wb.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val request = DownloadManager.Request(Uri.parse(url))
            request.setMimeType(mimetype)

            val cookies = CookieManager.getInstance().getCookie(url)
            request.addRequestHeader("cookie", cookies)
            request.addRequestHeader("User-Agent", userAgent)
            request.setDescription("Downloading File...")
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype))
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                    url, contentDisposition, mimetype
                )
            )
            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
            dm!!.enqueue(request)
            Toast.makeText(this, "Downloading File", Toast.LENGTH_LONG).show()
        }
    }

    private fun getData(){
        viewModel.getAyat(data.nomor.toString())
        viewModel.responseSurah.observe(this){
            if (it.status == Config.SUKSES){
                viewModel.listSurah.value = it.data
                setData()
            }
        }
    }

    private fun setData(){
        val adapter = AdapterAyat()
        with(binding.rvAyat){
            viewModel.listSurah.value?.let {
                adapter.setData(it)
                val linearLayoutManager = LinearLayoutManager(this@DetailSurahActivity)
                layoutManager = linearLayoutManager
                this.adapter = adapter
            }
        }
    }

    fun onLoading(){
        loading.onLoading()
    }

    fun onDismiss(){
        loading.onDismiss()
    }

    fun error(message: String) {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        pDialog.setTitleText("Terjadi Kesalahan")
            .setContentText(message)
            .setConfirmText("Reload")
            .setConfirmClickListener {
                getData()
            }
        pDialog.show()
    }

    override fun onPause() {
        super.onPause()
        binding.wb.onPause()
        binding.wb.pauseTimers()
    }

    override fun onResume() {
        super.onResume()
        binding.wb.resumeTimers()
        binding.wb.onResume()
    }

    override fun onDestroy() {
        binding.wb.destroy()
        super.onDestroy()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}