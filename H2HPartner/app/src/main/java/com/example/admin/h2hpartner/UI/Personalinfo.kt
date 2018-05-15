package com.example.admin.h2hpartner.UI

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.location.Geocoder
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

import com.example.admin.h2hpartner.App
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.CustomServices
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import kotlinx.android.synthetic.main.personal_info.*
import org.json.JSONObject
import android.widget.RadioButton
import com.GoMobeil.H2H.Extensions.getBitmapFromUri
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.Extensions.toast
import com.beust.klaxon.JsonObject
import com.example.admin.h2hpartner.BaseActivity
import com.example.admin.h2hpartner.Models.UploadImageModel
import com.example.admin.h2hpartner.R.layout.personal_info
import com.example.admin.h2hpartner.Services.CustomDialog
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.address_dialog.*
import kotlinx.android.synthetic.main.address_dialog.view.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.upload_document.*
import org.json.JSONArray
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


/**
 * Created by Admin on 05-02-18.
 */
class Personalinfo : BaseActivity() {

    companion object {

        val MALE = "Male"
        val FEMALE = "Female"
        val DIFFERENTLYABLED = "Differently abled"
        val WOMEN = "Women"
        val ELDERLY = "Elderly"
        val GENERAL = "General"
        val RECIDENTIALADDRESS = "Residential Address:"
    }

    override lateinit var context: Context
    override lateinit var activity: Activity

    lateinit var data: JSONObject
    lateinit var alertdialog:AlertDialog
    var lsBuilding: String = ""
    var lsStreet: String = ""
    var lsLandmark: String? = ""
    var lsCity: String = ""
    var lsprofileimagebas64: String = ""
    var lsState: String = ""
    var bmp: Bitmap? = null
    var fname: String? = ""
    var length: Long? = null
    var RESULT_LOAD_IMAGE: Int? = 100
    var PLACE_AUTOCOMPLETE_REQUEST_CODE = 1

    var lsPincode: String = ""
    var lsCompleteAddress: String = ""
    lateinit var cbSave: Button
    var lsUsername: String = ""
    var lsGender: String = ""
    var lsCategory: String = ""
    var lsAlternate: String = ""
    var lsLandline: String = ""
    var lsaddline2: String = ""
    var lsType: String = "R"
    var lsUpdatedby: String = prefs.fullname
    var lsMessage = ""
    var addjson: JSONArray? = null
    lateinit var pd: TransperantProgressDialog

  lateinit  var etBuilding:EditText
    lateinit  var etStreet:EditText
    lateinit var cbOK:Button
    lateinit var cbCancel:Button
    lateinit var cbChangeAddress:Button

    lateinit var viewGroup:ViewParent

    var adddata = ""
    var lattitude: Double? = 0.0
    var longitude: Double? = 0.0
    lateinit var coder: Geocoder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal_info)
        hideFooter(true)
        setTitle(getString(R.string.Personal_Details))


        activity = this
        context = this

        pd = TransperantProgressDialog(context)

        getProfileData()
        CustomServices.hideKB(activity)


        // val add=intent.getStringExtra("address")

        App.prefs?.dashboard(tvPersonalName, tvPersonalNumber, tvPersonalPriBusiness)

        initlayout()

    }

    @SuppressLint("ResourceType")
    fun initlayout() {



        cbSave = findViewById(R.id.cbSavepersonal)

        val item: List<CheckBox>
        tvResidentialAddress.setOnClickListener {

            getAddresswithpopup()

        }
        tvAddress.setOnClickListener {

            getAddresswithpopup()
        }

        cbSave.setOnClickListener {

            getData()
        }
        cbreset.setOnClickListener {
            clearData()

        }
        ivaddprofileimage.setOnClickListener {
            if (checkPermissionForReadExtertalStorage()) {
                val i = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, RESULT_LOAD_IMAGE!!)
            } else {
                requestPermissionForReadExtertalStorage()
            }


        }
        ivremoveprofileimage.setOnClickListener {
            bmp = null
            lsprofileimagebas64 = ""
            cvProfileImage.setImageResource(R.drawable.uploadprofile_icon)
        }
        cvProfileImage.setOnClickListener {
            if (checkPermissionForReadExtertalStorage()) {
                val i = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, RESULT_LOAD_IMAGE!!)
            } else {
                requestPermissionForReadExtertalStorage()

            }

        }

    }

    fun genMessage(msg: String) {
        if (lsMessage.length > 0) {
            lsMessage = lsMessage + ", " + msg;
        } else {
            lsMessage = lsMessage + " " + msg;
        }
    }

    fun saveData() {
        if (lsprofileimagebas64.length <= 0) {
            lsprofileimagebas64 = "null"
        }
        Fuel.post(StaticRefs.VENDOREDIT,listOf(StaticRefs.TOKEN to prefs.token, (StaticRefs.VENDORID to prefs.vendorid),
                (StaticRefs.ALTMOBILENO to lsAlternate),
                (StaticRefs.LANDLINE to lsLandline), (StaticRefs.CATEGORY to lsCategory),
                (StaticRefs.USERNAME to lsUsername), (StaticRefs.VENDORIMAGE to lsprofileimagebas64),
                (StaticRefs.GENDER to lsGender), (StaticRefs.ADDRESSLINE1 to lsBuilding),
                (StaticRefs.ADDRESSLINE2 to lsaddline2),(StaticRefs.ADDRESSLATITUDE to lattitude),
                (StaticRefs.ADDRESSLONGITUDE to longitude),(StaticRefs.VENDORUPDATEDBY to lsUpdatedby)))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson() { request,
                                  response,
                                  result ->
                    result.fold({ d ->
                        pd.show()
                        parseJsonSave(result.get().content)
                    }, { err ->
                        pd.dismiss()
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        //do something with error
                    })
                }
        prefs.username = lsUsername
        prefs.gender = lsGender
        prefs.categroy = lsCategory
        prefs.alternateno = lsAlternate
        prefs.landline = lsLandline
        prefs.profileimage = lsprofileimagebas64

    }

    fun parseJsonSave(response: String) {

        val json = JSONObject(response)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {
                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                val error = json.getString("errors")
                pd.dismiss()
                TastyToast.makeText(this, error, Toast.LENGTH_LONG, TastyToast.ERROR).show()
            } else if (json.getString(StaticRefs.STATUS).equals(StaticRefs.SUCCESS)) {
                pd.dismiss()
                if (prefs.profilestatus.equals(StaticRefs.INCOMPLETE)) {
                    prefs.personalinfostatus = StaticRefs.COMPLETE
                }
                finish()
                TastyToast.makeText(context, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
            }

        }
    }

    fun setData() {

        val gender: String
        val category: String
        if (!lsUsername.equals(null) && lsUsername.equals("null")) {
            etPersonalusername.setText("")
        } else {
            etPersonalusername.setText(lsUsername)
        }
        val lsgender = prefs.gender
        if (lsgender.equals("M")) {

            gender = MALE
            rgGender.check(rbMale.id)
        } else if (lsgender.equals("F")) {

            gender = FEMALE
            rgGender.check(rbFemale.id)
        }

        val lscategory = prefs.categroy
        if (lscategory.equals(GENERAL)) {

            rgServices.check(rbGeneral.id)
        } else if (lscategory.equals(WOMEN)) {

            rgServices.check(rbWomen.id)
        } else if (lscategory.equals(ELDERLY)) {

            rgServices.check(rbEdler.id)
        } else if (lscategory.equals(DIFFERENTLYABLED)) {
            rgServices.check(rbDiffrentlyAbled.id)
        }
        if (!lsAlternate.equals(null) && lsAlternate.equals("null")) {
            etAlternate.setText("")
        } else {
            etAlternate.setText(lsAlternate)
        }
        if (!lsLandline.equals(null) && lsLandline.equals("null")) {
            etLandline.setText("")
        } else {
            etLandline.setText(lsLandline)
        }
        cvaddress.visibility = View.VISIBLE
        if (lsCompleteAddress.equals(null) || lsCompleteAddress.equals("null") || lsCompleteAddress.equals("null") || lsCompleteAddress.startsWith(",")) {
            tvAddress.setText("Click Here To Add Address")
        } else {
            tvAddress.setText(lsCompleteAddress).toString().trim()
        }
        if (!(lsprofileimagebas64.equals(null) || lsprofileimagebas64.equals("null") || lsprofileimagebas64.equals("NULL") || lsprofileimagebas64.equals(""))) {
            cvProfileImage.loadBase64Image(lsprofileimagebas64)
        }
    }

    fun getAddresswithpopup() {


       val cal=LayoutInflater.from(context).inflate(R.layout.address_dialog,null)
        etBuilding = cal.findViewById<EditText>(R.id.etBuilding)
        etStreet = cal.findViewById<EditText>(R.id.etStreet)
        cbCancel = cal.findViewById<Button>(R.id.cbCancel)
         cbOK = cal.findViewById<Button>(R.id.cbOK)
        cbChangeAddress=cal.findViewById<Button>(R.id.cbChangeAddress)

        if (!(lsCompleteAddress.equals(null) || lsCompleteAddress.equals(""))) {

            etBuilding.setText(lsBuilding)

            etStreet.setText(lsaddline2)
        }

        val popup1 =AlertDialog.Builder(context)

        popup1.setView(cal)

        alertdialog = popup1.create()
        alertdialog.setCancelable(true)
        alertdialog.setCanceledOnTouchOutside(true)

        alertdialog.show()
        cbChangeAddress.setOnClickListener {
            autocomplete()
        }
      /*  cbOK.setOnClickListener {
            autocomplete()
        }*/

        cbOK.setOnClickListener() {

            lsBuilding = etBuilding.text.toString().trim()
            lsStreet = etStreet.text.toString().trim()
            var lbProceedAhead = true;


            if (!((lsBuilding != null && lsBuilding.length > 0))) {
                genMessage(getString(R.string.Building));
                lbProceedAhead = false;
            }

            if (!((lsStreet != null && lsStreet.length > 0))) {
                genMessage(getString(R.string.Street));
                lbProceedAhead = false;
            }



            if (!(lbProceedAhead) && lsMessage.length > 0) {
                if (lsMessage.length > 50) {
                    TastyToast.makeText(context, getString(R.string.Please_Enter_All_Details), TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
                } else {
                    TastyToast.makeText(context, getString(R.string.Please_Enter_Valid) + lsMessage, TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
                }

            } else {
                lsaddline2 = lsStreet
                lsCompleteAddress = lsBuilding + ", " + lsaddline2
                cvaddress.visibility = View.VISIBLE
                tvAddress.setText(lsCompleteAddress).toString().trim()
                alertdialog.dismiss()
                alertdialog.cancel()

            }
        }
        cbCancel.setOnClickListener(View.OnClickListener {

            alertdialog.dismiss()
            alertdialog.cancel()

        })

    }

    fun getData() {
        lsUsername = etPersonalusername.text.toString().trim()

        if (rgGender.checkedRadioButtonId == null || rgGender.checkedRadioButtonId <= 0) {

            TastyToast.makeText(context, "please select gender", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show()
        } else {
            var selectedidofgender = rgGender.checkedRadioButtonId
            var rbgender = findViewById<RadioButton>(selectedidofgender)
            lsGender = rbgender.text.toString().trim()
            if (lsGender.contains("Male")) {
                lsGender = "M"
            } else if (lsGender.contains("Female")) {
                lsGender = "F"
            }
        }

        if (rgServices.checkedRadioButtonId == null || rgServices.checkedRadioButtonId <= 0) {

            TastyToast.makeText(context, "please select Category", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show()
        } else {
            var selectedidofcategory = rgServices.checkedRadioButtonId
            var rbcategory = findViewById<RadioButton>(selectedidofcategory)
            lsCategory = rbcategory.text.toString().trim()
        }
        lsAlternate = etAlternate.text.toString().trim()
        lsLandline = etLandline.text.toString().trim()


        var lbisProceed = true
        if (!((lsUsername != null && lsUsername.length > 0))) {
            genMessage(" Username");
            lbisProceed = false;
        }

        if (!((lsGender != null && lsGender.length > 0))) {
            genMessage(" Gender ");
            lbisProceed = false;
        }


        if (!((lsCategory != null && lsCategory.length > 0))) {
            genMessage(" Category ");
            lbisProceed = false;
        }


       /* if (!((lsAlternate != null && lsAlternate.length > 0))) {
            genMessage(" Alternate number")
            lbisProceed = false;
        }



        if (!((lsLandline != null && lsLandline.length > 0))) {
            genMessage(" Landline number")
            lbisProceed = false;
        }*/

        if (!((lsCompleteAddress != null && lsCompleteAddress.length > 0))) {
            genMessage(" Complete Residential Address")
            lbisProceed = false;
        }


        if (lbisProceed) {

            if (!StaticRefs.isValidContact(lsAlternate)) {
                etAlternate.setError("enter a valid alternate number")
                lbisProceed = false
                TastyToast.makeText(context, "enter a valid alternate  number", TastyToast.LENGTH_LONG, TastyToast.WARNING).show()

            }
        }

        if (!(lbisProceed) && lsMessage.length > 0) {
            if (lsMessage.length > 70) {
                TastyToast.makeText(context, "Please enter all details", TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
            } else {
                TastyToast.makeText(context, "Please enter valid " + lsMessage, TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
            }

        } else {
            saveData()
        }


    }

    fun getProfileData() {
        pd.show()
        Fuel.post(StaticRefs.VENDORDETAILSURL, listOf((StaticRefs.VENDORID to prefs.vendorid), StaticRefs.TOKEN to prefs.token))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                    // parseProfile(result.get().content)
                    result.fold({ d ->

                        parseProfile(result.get().content)
                    }, { err ->
                        pd.dismiss()
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        //do something with error
                    })


                }
    }

    fun parseProfile(response: String) {

        pd.dismiss()
        var jsonob: JSONObject? = null
        // data = JSONObject(response).getJSONObject("data")
        val json = JSONObject(response)
        if (response.contains(StaticRefs.DATA) && json.getString(StaticRefs.DATA) != null && !json.getString(StaticRefs.DATA).equals("")) {

            val data = json.getJSONObject(StaticRefs.DATA)
            val data1 = json.getString(StaticRefs.DATA)
            val jsondata = JSONObject(data1)
            if (jsondata.length() > 0) {


                addjson = data.getJSONArray(StaticRefs.RESIDENTIALADD)

                adddata = addjson.toString()
                val firstname: String = data.getString(StaticRefs.FIRSTNAME)
                val lastname: String = data.getString(StaticRefs.LASTNAME)
                val mobilenumber: String = data.getString(StaticRefs.VENDORMOBILENO)
                val pribusiness: String = data.getString(StaticRefs.VENDORPRIMARYBUSINESS)

                if (response.contains(StaticRefs.VENDORIMAGE)) {
                    val image = data.getString(StaticRefs.VENDORIMAGE)

                    if (!(image.equals(null) || image.equals("null") || image.equals("NULL"))) {
                        lsprofileimagebas64 = data.getString(StaticRefs.VENDORIMAGE)
                    }
                }

                prefs.fullname = firstname + " " + lastname
                prefs.mobile_no = mobilenumber
                prefs.pri_business = pribusiness
                lsUsername = data.getString(StaticRefs.USERNAME)
                prefs.gender = data.getString(StaticRefs.GENDER)
                prefs.categroy = data.getString(StaticRefs.CATEGORY)
                lsAlternate = data.getString(StaticRefs.ALTMOBILENO)
                lsLandline = data.getString(StaticRefs.LANDLINE)
                System.out.println("fullname" + prefs.fullname)

                val add = adddata
                val jsonadd = JSONArray(add)
                var json1: JSONObject? = null

                for (i in 0..jsonadd.length() - 1) {

                    json1 = jsonadd.getJSONObject(i)

                    lsBuilding = json1!!.getString(StaticRefs.ADDRESSLINE1)
                    lsaddline2 = json1!!.getString(StaticRefs.ADDRESSLINE2)
                    lattitude=json1!!.getString(StaticRefs.ADDRESSLATITUDE).toDouble()
                    longitude=json1!!.getString(StaticRefs.ADDRESSLONGITUDE).toDouble()

                }
                lsCompleteAddress = lsBuilding + ", " + lsaddline2

                setData()

            } else {
                TastyToast.makeText(context, getString(R.string.No_Data_Found)+ lsMessage, TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
            }

        } else {
            pd.dismiss()
            TastyToast.makeText(context, getString(R.string.No_Data_Found) + lsMessage, TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
        }
    }

    fun clearData() {
        etPersonalusername.setText("")
        rgGender.clearCheck()
        rgServices.clearCheck()
        etAlternate.setText("")
        etLandline.setText("")
        tvResidentialAddress.setText(RECIDENTIALADDRESS)
        cvaddress.visibility = View.GONE
        tvAddress.setText("")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {

            val selectedImage = data.data

            length = getImageSize(selectedImage)

            if (length!! <= 1000) {
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
                cvProfileImage.setImageBitmap(bmp)
                val imagestring = CustomServices.ConverttoBase64(bmp!!)
                FinalBase64(imagestring)

            } else {
                TastyToast.makeText(context, getString(R.string.File_Upto_5GB), 30, TastyToast.ERROR).show();

            }
        }
        else if(requestCode==PLACE_AUTOCOMPLETE_REQUEST_CODE){
            if(resultCode== RESULT_OK){
                var place= PlaceAutocomplete.getPlace(context,data)
                var address=place.address


                lattitude=place.latLng.latitude
               longitude=place.latLng.longitude
                val geocoder= Geocoder(context, Locale.ENGLISH)


                etStreet.setText(address)


            }else if(requestCode== PlaceAutocomplete.RESULT_ERROR){
                var Status= PlaceAutocomplete.getStatus(context,data)
                TastyToast.makeText(context,Status.toString(),Toast.LENGTH_SHORT,TastyToast.ERROR)

            }
        }
    }

    fun FinalBase64(encoded: String?) {
        lsprofileimagebas64 =StaticRefs.BASE64LINK+encoded.toString().trim()


        // adapter.notifyItemInserted(docsModel.size - 1)
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

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun autocomplete(){
        try {
            val typeFilter = AutocompleteFilter.Builder()
                    .setCountry("IN")
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                    .build()
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter).build(this)
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        }catch (e: GooglePlayServicesRepairableException){

        }catch (e: GooglePlayServicesNotAvailableException){

        }

    }
}

