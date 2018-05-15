package com.example.admin.h2hpartner.Services

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask


import java.io.ByteArrayOutputStream
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Base64
import com.example.admin.h2hpartner.UI.KycInfo
import com.example.admin.h2hpartner.UI.Uploadworkimages

/**
 * Created by ADMIN on 28-02-2018.
 */

class ConvertToBase64(var parent: KycInfo, var context: Context, var bmp: Bitmap) : AsyncTask<String, Context, Bitmap>() {
    lateinit var pd: TransperantProgressDialog
    var encoded: String? = null

    override fun onPreExecute() {
        pd = TransperantProgressDialog(context)
        pd.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doInBackground(vararg params: String): Bitmap? {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArrayImage = baos.toByteArray()
        encoded = Base64.encodeToString(byteArrayImage, Base64.NO_WRAP)
        println("encoded base64 $encoded")
        return bmp
    }

    override fun onPostExecute(result: Bitmap) {
        pd.dismiss()
        parent.ReturnThreadResult(encoded);
    }

}