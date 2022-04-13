package id.web.ods.app.alquranapp.utils

import android.content.Context
import android.graphics.Color
import cn.pedant.SweetAlert.SweetAlertDialog

class Loading(var context: Context) {

    private lateinit var asd: SweetAlertDialog

    fun onLoading() {
        asd = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        asd.progressHelper.barColor = Color.parseColor("#A5DC86")
        asd.titleText = "Loading"
        asd.setCancelable(false)
        asd.show()
    }

    fun error(context: Context, title: String, pesan: String) {
        val pDialog = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
        pDialog.setTitleText(title)
            .setContentText(pesan)
            .setConfirmText("Oke")
            .setConfirmClickListener {
                pDialog.dismiss()
            }
        pDialog.show()
    }

    fun onDismiss() {
        if (asd.isShowing){
            asd.dismiss()
        }
    }
}