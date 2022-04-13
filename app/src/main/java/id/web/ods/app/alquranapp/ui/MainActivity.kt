package id.web.ods.app.alquranapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import id.web.ods.app.alquranapp.R
import id.web.ods.app.alquranapp.databinding.ActivityMainBinding
import id.web.ods.app.alquranapp.response.ResultData
import id.web.ods.app.alquranapp.ui.detail.DetailSurahActivity
import id.web.ods.app.alquranapp.ui.home.HomeViewModel
import id.web.ods.app.alquranapp.ui.home.SurahAdapter
import id.web.ods.app.alquranapp.utils.Config.SUKSES
import id.web.ods.app.alquranapp.utils.Loading

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    lateinit var viewModel: HomeViewModel
    lateinit var loading: Loading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        main()
    }

    private fun main(){
        viewModelStore.clear()
        loading = Loading(this)
        viewModel = HomeViewModel(this)
        permission()

        getData()

    }

    private fun getData(){
        viewModel.getDataSurah()
        viewModel.responseSurah.observe(this){
            if (it.status == SUKSES){
                viewModel.listSurah.value = it.data
                showData()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showData (){
        val adapter = SurahAdapter()
        with(binding.rvSurah){
            viewModel.listSurah.value?.let {
                adapter.setData(it,this@MainActivity)
                val linearLayoutManager = LinearLayoutManager(this@MainActivity)
                layoutManager = linearLayoutManager
                this.adapter = adapter
            }

            binding.edtSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    adapter.filter.filter(s.toString())
                }
            })

            binding.edtSearch.setOnTouchListener(View.OnTouchListener { _, event ->
                val drawableRight = 2

                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= binding.edtSearch.right - binding.edtSearch.compoundDrawables[drawableRight].getBounds().width()) {
                        binding.edtSearch.setText("")
                        binding.edtSearch.hint = resources.getString(R.string.cari)
                        return@OnTouchListener true
                    }
                }
                false
            })
        }
    }

    fun goToDetail(data:ResultData){
        val i = Intent(this,DetailSurahActivity::class.java)
        i.putExtra(DetailSurahActivity.DATA,data)
        startActivity(i)
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

    private fun permission(){
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) { /* ... */
                }
            }).check()
    }
}