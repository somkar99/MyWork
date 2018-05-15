package com.example.admin.h2hpartner.UI

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import com.GoMobeil.H2H.Extensions.getBitmapFromUri
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.example.admin.h2hpartner.BaseActivity
import com.example.admin.h2hpartner.Models.UploadImageModel
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.CustomDialog
import com.example.admin.h2hpartner.Services.CustomServices
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.prefs
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.upload_document.*

import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class SelectKycImage : Activity() {


    lateinit var activity: Activity
    lateinit var context: Context

    private val PICKFILE_RESULT_CODE = 50

    var RESULT_LOAD_IMAGE: Int? = 100
    var bmp: Bitmap? = null
    var resizedBmp: Bitmap? = null
    lateinit var jsonMain: JSONObject
    var fname: String? = ""
    var finalImageName: String? = null
    var length: Long? = null
    var lsBase64String: String? = ""

    var liExSlNo: Int? = null
    var position: Int? = 0
    lateinit var lsExImageName: String
    lateinit var lsExTitle: String

    var lsTitle: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_document)
        setTitle("Upload Document")
        activity = this
        context = this
        initlayout()
    }

    fun initlayout(): Unit {

        position = getIntent().getIntExtra("Position", 0)

        if (position == null || position == 0) {

            flDoc.visibility = View.GONE
            tvAddDoc.visibility = View.VISIBLE

            llEdit.visibility = View.GONE
            cbUpload.visibility = View.VISIBLE

        } else {
            lsExTitle = KycInfo.uploadkyc.get(position!! - 1).image_name!!
            lsExImageName = KycInfo.uploadkyc.get(position!! - 1).image!!

            tvAddDoc.visibility = View.GONE
            flDoc.visibility = View.VISIBLE

            cbUpload.visibility = View.GONE
            llEdit.visibility = View.VISIBLE

            etTitle.setText(lsExTitle)
            ivDocImage.loadBase64Image(lsExImageName);
            etTitle.isEnabled = false
            etTitle.clearFocus()
        }


        tvAddDoc.setOnClickListener(View.OnClickListener {

            if (checkPermissionForReadExtertalStorage()) {
                val i = Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, RESULT_LOAD_IMAGE!!)
            } else {
                requestPermissionForReadExtertalStorage()
                tvAddDoc.callOnClick()
            }
        })

        ivCancel.setOnClickListener(View.OnClickListener {
            flDoc.visibility = View.GONE
            tvAddDoc.visibility = View.VISIBLE
            bmp = null
            fname = ""
            lsBase64String = ""
        })

        cbEdit.setOnClickListener(View.OnClickListener {
            rename(fname!!)
            if (etTitle.text.toString().length > 0) {
                if (bmp != null) {
                    saveDoc()
                    var intent = Intent()
                    intent.putExtra("DOCS_LIST", jsonMain.toString())
                    intent.putExtra("STATUS", "EDIT")
                    intent.putExtra("Position", position!! - 1)
                    setResult(RESULT_OK, intent)
                    finish()

                } else {
                    ivCancel.performClick()
                    TastyToast.makeText(context, getString(R.string.Please_Attach_Related_doucment), 30, TastyToast.ERROR).show();
                    cbEdit.setText(getString(R.string.OK))
                }
            } else {
                etTitle.setError(getString(R.string.Title_Required))
            }
        })

        cbDelete.setOnClickListener(View.OnClickListener {

            showDeletePopup(lsExTitle, getString(R.string.Confirmation), getString(R.string.Are_You_Want_Delete_Documents))
        })

        cbUpload.setOnClickListener(View.OnClickListener {

            rename(fname!!)

            if (etTitle.text.toString().length > 0) {
                if (bmp != null) {

                    var lbResult: Boolean? = false

                    for (i in 0..KycInfo.uploadkyc.size - 1) {
                        var lsTempTitle = KycInfo.uploadkyc.get(i).image_name
                        if (lsTempTitle.equals(etTitle.text.toString())) {
                            lbResult = true
                        }
                    }

                    if (lbResult!!) {
                        TastyToast.makeText(context, getString(R.string.Cant_Have_Doc_With_Same_Name), 30, TastyToast.ERROR).show();
                    } else {
                        //  callSaveFile(bmp)
                        saveDoc()
                        var intent = Intent()
                        intent.putExtra("DOCS_LIST", jsonMain.toString())
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                } else {
                    TastyToast.makeText(context, getString(R.string.Please_Attach_Related_doucment), 30, TastyToast.ERROR).show();
                }
            } else {
                etTitle.setError(getString(R.string.Title_Required))
            }
        })

    }

    fun saveDoc() {
        var json = JSONObject()
        if (position == 0 || position == null) {
            json.put(UploadImageModel.IMAGESERNO, null)
            json.put(UploadImageModel.IMAGENAME, etTitle.text.toString())
            json.put(UploadImageModel.IMAGE, finalImageName)
        } else {
            json.put(UploadImageModel.IMAGESERNO, liExSlNo)
            json.put(UploadImageModel.IMAGENAME, lsExTitle)
            json.put(UploadImageModel.IMAGE, finalImageName)
        }
        jsonMain = JSONObject()
        jsonMain.put(StaticRefs.DATA, json)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {

            val selectedImage = data.data

            length = getImageSize(selectedImage)

            if (length!! <= 1000) {

                tvAddDoc.visibility = View.GONE
                flDoc.visibility = View.VISIBLE

                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                val cursor = contentResolver.query(selectedImage!!,
                        filePathColumn, null, null, null)
                cursor!!.moveToFirst()

                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val picturePath = cursor.getString(columnIndex)
                cursor.close()

                try {
                    bmp = getBitmapFromUri(selectedImage)
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

                compressImage(picturePath)
                ivDocImage.setImageBitmap(bmp)


            } else {
                TastyToast.makeText(context, getString(R.string.File_Upto_5GB), 30, TastyToast.ERROR).show();

            }
        }
    }

    fun getImageSize(uri: Uri): Long {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        cursor!!.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val filePath = cursor.getString(columnIndex)
        cursor.close()

        val file = File(filePath)
        val ls = file.length()
        val lengthInKb = ls / 5120
        // Toast.makeText(context, "My file length is ${lengthInKb.toString()}", Toast.LENGTH_SHORT).show()
        return lengthInKb
    }

    fun checkPermissionForReadExtertalStorage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        } else {
            return false
        }
    }

    @Throws(Exception::class)
    fun requestPermissionForReadExtertalStorage() {
        try {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    100)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }

    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }

    fun compressImage(imageUri: String): String {

        val filePath = getRealPathFromURI(imageUri)
        var scaledBitmap: Bitmap? = null

        val options = BitmapFactory.Options()

        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        val maxHeight = 812.0f
        val maxWidth = 612.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight


        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

        options.inJustDecodeBounds = false

        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()

        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))

        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)

            val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0)
            Log.d("EXIF", "Exif: " + orientation)
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90F)
                Log.d("EXIF", "Exif: " + orientation)
            } else if (orientation == 3) {
                matrix.postRotate(180F)
                Log.d("EXIF", "Exif: " + orientation)
            } else if (orientation == 8) {
                matrix.postRotate(270F)
                Log.d("EXIF", "Exif: " + orientation)
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap!!.width, scaledBitmap.height, matrix,
                    true)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        fname = getFilename()
        try {
            out = FileOutputStream(fname)

            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return fname as String
    }

    fun getFilename(): String {
        val file = File(StaticRefs.lsImageDirectory)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath + "/" + System.currentTimeMillis() + ".jpg"

    }

    fun getRealPathFromURI(contentURI: String): String {
        val contentUri = Uri.parse(contentURI)
        val cursor = contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            return contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return cursor.getString(index)
        }
    }

    fun rename(lsName: String) {
        val file = File(lsName)
        finalImageName = "@${(prefs.vendorid).toString()}_" + etTitle.text.toString() + ".jpg"
        val rename = File(StaticRefs.lsImageDirectory + "/" + finalImageName)
        file.renameTo(rename)
    }

    private fun showDeletePopup(title: String, lsHeader: String, lsMessage: String) {
        val dialog = CustomDialog(activity, context)
        CustomServices.hideSoftKeyboard(this)
        dialog.setCancel(false)
        dialog.setOutsideTouchable(false)
        dialog.setTitle(lsHeader)
        dialog.setMessage("$lsMessage $title")
        dialog.showDialog()
        dialog.setDialogButtonClickListener(object : CustomDialog.DialogButtonClick {
            override fun DialogButtonClicked(view: View) {
                when (view.id) {
                    R.id.cbOK -> {
                        var intent = Intent()
                        intent.putExtra("STATUS", "DELETE")
                        intent.putExtra("Position", position!! - 1)
                        setResult(RESULT_OK, intent)
                        finish()

                    }

                    R.id.cbCancel -> dialog.dismiss()
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
