package com.example.admin.h2hpartner.UI

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.location.Address
import android.location.Geocoder
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.admin.h2hpartner.Adapter.PincodeAdapter
import com.example.admin.h2hpartner.App
import com.example.admin.h2hpartner.Models.PinCodeModel

import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
//import com.example.admin.h2hpartner.Extensions.CalendarView

import kotlinx.android.synthetic.main.business.*
import kotlinx.android.synthetic.main.personal_info.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import com.GoMobeil.H2H.Extensions.getBitmapFromUri
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.Extensions.toast
import com.example.admin.h2hpartner.BaseActivity
import com.example.admin.h2hpartner.Services.CustomServices
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.address_dialog.*
import kotlinx.android.synthetic.main.address_dialog.view.*
import kotlinx.android.synthetic.main.register.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


/**
 * Created by Admin on 06-02-18.
 */
class BusinessInfo : BaseActivity() {
    companion object {
        var BUSINESSADDRESS = "Business Address:"
    }

    override lateinit var context: Context
    override lateinit var activity: Activity
    var PLACE_AUTOCOMPLETE_REQUEST_CODE = 1
    //Local Strings
    lateinit var lsName: String
    lateinit var lsMobileno: String
    lateinit var lsPrimarybusiness: String
    lateinit var lsusp: String
    lateinit var lsweburl: String
    lateinit var lsBusinesscontactno: String
    lateinit var cbReset: Button
    lateinit var cbSave: Button
    lateinit var alertdialog: android.support.v7.app.AlertDialog
    var lsBuilding: String = ""
    var blanckstring: String = ""
    var lsStreet: String = ""
    var lsLandmark: String? = ""
    var lsCity: String = ""
    var lsState: String = ""
    var lsPincode: String = ""
    var lsCompleteAddress: String = ""
    var lsaddline2: String = ""
    var lsUpdatedby: String = prefs.fullname
    var lsMessage = ""
    var lslogoimagebas64: String = ""
    var bmp: Bitmap? = null
    var fname: String? = ""
    var length: Long? = null
    var RESULT_LOAD_IMAGE: Int? = 100
    var lsType: String = "W"
    var lsIntroduction: String = ""
    var lsYear: String = ""
    var lsMonth: String = ""
    var lsEstablishon: String = ""
    var lsBusninessname: String = ""
    var lsServiceareinkm: String = ""
    var lsAddress:String=""
    lateinit var data: JSONObject
    var addjson: JSONArray? = null

    lateinit  var etBuilding:EditText
    lateinit  var etStreet:EditText
    lateinit var cbOK:Button
    lateinit var cbCancel:Button
    lateinit var cbChangeAddress:Button

    var adddata = ""
    var lattitude: Double? = 0.0
    var longitude: Double? = 0.0
    lateinit var coder: Geocoder


    lateinit var rcvList: RecyclerView
    lateinit var rcvAdapter: RecyclerView.Adapter<PincodeAdapter.ViewHolder>
    lateinit var layoutManager: RecyclerView.LayoutManager
    var adapter: PincodeAdapter? = null
    var name:String=""


    lateinit var mlPincodelist: MutableList<PinCodeModel>
    lateinit var pd: TransperantProgressDialog
    lateinit  var date:Date
    var year:Int?=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.business)
        hideFooter(true)
        setTitle(getString(R.string.Business_Details))
        activity = this
        context = this
        CustomServices.hideKB(activity)
        pd = TransperantProgressDialog(context)

        coder = Geocoder(context)
        getBusinessData()

        cbReset = findViewById<Button>(R.id.cbReset)
        cbSave = findViewById(R.id.cbSave)

    year = Calendar.getInstance().get(Calendar.YEAR)


        App.prefs?.dashboard(tvBName, tvBNumber, tvBPriBusiness)

        initlayout()


    }

    fun initlayout() {

        mlPincodelist = mutableListOf()


        /* rcvList = findViewById(R.id.rcServiceareas)
         layoutManager = LinearLayoutManager(context)
         rcvList.layoutManager = layoutManager*/

        val seekBar = findViewById<View>(R.id.seekBar) as SeekBar
        seekBar.max = 200
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                val float: Float
                float = (progress / 10.0).toFloat()
                tvrangeinkm.setText(getString(R.string.Radius_is)+ float.toString() + getString(R.string.KM))
                lsServiceareinkm = float.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {


            }
        })

        cbReset.setOnClickListener() {

            clearData()
        }

        cbSave.setOnClickListener() {

            getData()
        }

        tvBusinessAddress.setOnClickListener {

            getAddresswithpopup()
        }
        tvBaddress.setOnClickListener {
            getAddresswithpopup()
        }
        ivaddlogoimage.setOnClickListener {
            if (checkPermissionForReadExtertalStorage()) {
                val i = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, RESULT_LOAD_IMAGE!!)
            } else {
                requestPermissionForReadExtertalStorage()

            }


        }
        etEmonth.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                lsMonth=etEmonth.text.toString()
                if (!((lsMonth != null && lsMonth.length > 0))) {
                    TastyToast.makeText(context, getString(R.string.Please_Enter_Month), Toast.LENGTH_SHORT, TastyToast.ERROR).show();


                }
                else{
                    if (!(lsMonth.length > 1)) {


                        // genMessage(" Month format - mm");
                        TastyToast.makeText(context, getString(R.string.Month_Format), Toast.LENGTH_SHORT, TastyToast.ERROR).show();

                    }
                    else {
                        if (lsMonth.length > 0) {
                            var month = lsMonth.toInt()
                            if (month > 12) {
                                etEmonth.setText("")
                                TastyToast.makeText(context, getString(R.string.Invalid_month), Toast.LENGTH_SHORT, TastyToast.ERROR).show();
                            }

                        }
                    }
                }



            }
        }
        etEyear.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){

                lsYear=etEyear.text.toString()

            if (!((lsYear != null && lsYear.length > 0))) {
                // genMessage(" Year ");
                TastyToast.makeText(context, getString(R.string.Please_Enter_Year), Toast.LENGTH_SHORT, TastyToast.ERROR).show();


            }else{
                if (!(lsYear.length == 4)) {

                    // genMessage(" year format - yyyy");
                    TastyToast.makeText(context, getString(R.string.Year_Format), Toast.LENGTH_SHORT, TastyToast.ERROR).show();

                }
                else{
                    if(lsYear.length>0)
                    {
                        var Eyear=lsYear.toInt()

                        if(Eyear>year!!){
                            etEyear.setText("")
                            TastyToast.makeText(context, getString(R.string.Invalid_Year), Toast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }

                    }
                }
            }



        }

        }






        ivremovelogo.setOnClickListener {
            bmp = null
            lslogoimagebas64 = ""
            cvLogoImage.setImageResource(R.drawable.uploadprofile_icon)
        }
        cvLogoImage.setOnClickListener {
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
                cvLogoImage.setImageBitmap(bmp)
                val imagestring = CustomServices.ConverttoBase64(bmp!!)
                FinalBase64(imagestring)

            } else {
                TastyToast.makeText(context, getString(R.string.File_Upto_5GB), 30, TastyToast.ERROR).show();

            }
        }
       else if(requestCode==PLACE_AUTOCOMPLETE_REQUEST_CODE){
            if(resultCode== RESULT_OK){
                var place=PlaceAutocomplete.getPlace(context,data)
                var address=place.address


              lattitude=place.latLng.latitude
                longitude=place.latLng.longitude
                val geocoder=Geocoder(context, Locale.ENGLISH)

                etStreet.setText(address)




            }else if(requestCode==PlaceAutocomplete.RESULT_ERROR){
                var Status=PlaceAutocomplete.getStatus(context,data)
                TastyToast.makeText(context,Status.toString(),Toast.LENGTH_SHORT,TastyToast.ERROR)

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

    fun getData() {

        lsBusninessname = etBusinessName.text.toString().trim()
        lsIntroduction = etBusinessintroduction.text.toString().trim()
        lsYear = etEyear.text.toString().trim()
        lsMonth = etEmonth.text.toString().trim()
        lsusp = etYccu.text.toString()
        lsweburl = etWeburl.text.toString()
        lsBusinesscontactno = etBusinesscontactno.text.toString()


        var lbisProceed = true

        if (!((lsServiceareinkm != null && lsServiceareinkm.length > 0))) {
            genMessage(getString(R.string.Service_Area));
            lbisProceed = false;
        }

        if (!((lsBusninessname != null && lsBusninessname.length > 0))) {
            genMessage(getString(R.string.Business_Name));
            lbisProceed = false;
        }

        if (!((lsIntroduction != null && lsIntroduction.length > 0))) {
            genMessage(getString(R.string.Introduction));
            lbisProceed = false;
        }

        if (!((lsYear != null && lsYear.length > 0))) {
            genMessage(getString(R.string.Year));
            lbisProceed = false;
        }

        if (!(lsYear.length == 4)) {

            genMessage(getString(R.string.Year_Format));
            lbisProceed = false;
        }


        if (!((lsMonth != null && lsMonth.length > 0))) {
            genMessage(getString(R.string.Month));
            lbisProceed = false;
        }
        if (!(lsMonth.length > 1)) {

            genMessage(getString(R.string.Month_Format));
            lbisProceed = false;

        }

        if (!((lsusp != null && lsusp.length > 0))) {
            genMessage(getString(R.string.USP))
            lbisProceed = false;
        }


     /*   if (!((lsweburl != null && lsweburl.length > 0))) {
            genMessage(" Web URL")
            lbisProceed = false;
        }*/

        if (!((lsBusinesscontactno != null && lsBusinesscontactno.length > 0))) {
            genMessage(getString(R.string.Complete_Business_Address))
            lbisProceed = false;
        }

        if (lbisProceed) {


            if (!StaticRefs.isValidContact(lsBusinesscontactno)) {
                etBusinesscontactno.setError(getString(R.string.Enter_Valid_Business_Number))
                lbisProceed = false
                TastyToast.makeText(context, getString(R.string.Enter_Valid_Business_Number), Toast.LENGTH_LONG, TastyToast.ERROR).show()

            }

        }


        if (!(lbisProceed) && lsMessage.length > 0) {
            if (lsMessage.length > 40) {
                TastyToast.makeText(context, getString(R.string.Please_Enter_All_Details), Toast.LENGTH_LONG, TastyToast.ERROR).show()
            } else {
                TastyToast.makeText(context, getString(R.string.Please_Enter_Valid ) + lsMessage, Toast.LENGTH_LONG, TastyToast.ERROR).show()
            }

        } else {
            lsEstablishon = lsYear + "-" + lsMonth + "-01"
         //   getCoordinatesFromAdd(lsCompleteAddress)

            saveData()
        }

    }

    fun clearData() {
        etBusinessintroduction.setText("")
        etBusinessName.setText("")
        etEmonth.setText("")
        etEyear.setText("")
        etYccu.setText("")
        etWeburl.setText("")
        etBusinesscontactno.setText("")
        tvBusinessAddress.setText(BUSINESSADDRESS)
        cvBaddress.visibility = View.GONE
        seekBar.setProgress(0)
    }

    fun saveData() {

      //  getCoordinatesFromAdd(lsCompleteAddress)
        if (lslogoimagebas64.length <= 0) {
            lslogoimagebas64 = "null"
        }
        TastyToast.makeText(context, getString(R.string.Data_Validate), Toast.LENGTH_SHORT, TastyToast.SUCCESS).show()
        pd.show()
        Fuel.post(StaticRefs.VENDORBUSINESSEDIT, listOf(StaticRefs.TOKEN to prefs.token, (StaticRefs.VENDORID to prefs.vendorid),
                (StaticRefs.SERVICEID to prefs.serviceid), (StaticRefs.PBBUSINESSNAME to lsBusninessname),
                (StaticRefs.PBLOGO to lslogoimagebas64),
                (StaticRefs.PBINTRODUCTION to lsIntroduction), (StaticRefs.PBESTABLISHON to lsEstablishon),
                (StaticRefs.PBUSP to lsusp), (StaticRefs.PBSERVICEAREA to lsServiceareinkm),
                (StaticRefs.PBWEBURL to lsweburl), (StaticRefs.PBBUSINESSCONTACTNO to lsBusinesscontactno),
                (StaticRefs.ADDRESSLINE1 to lsBuilding), (StaticRefs.ADDRESSLINE2 to lsaddline2),
                (StaticRefs.ADDRESSLATITUDE to lattitude), (StaticRefs.ADDRESSLONGITUDE to longitude),
                (StaticRefs.PBUPDATEDBY to lsUpdatedby)))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson() { request,
                                  response,
                                  result ->

                    result.fold({ d ->
                        parseJsonSave(result.get().content)

                    }, { err ->
                        pd.dismiss()
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })
                }


    }

    fun parseJsonSave(response: String) {
        pd.dismiss()
        val json = JSONObject(response)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {

                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {

                val error = json.getString("errors")
                TastyToast.makeText(this, error, Toast.LENGTH_LONG, TastyToast.ERROR).show()
            } else if (json.getString(StaticRefs.STATUS).equals(StaticRefs.SUCCESS)) {
                if (prefs.profilestatus.equals(StaticRefs.INCOMPLETE)) {
                    prefs.businessinfostatus = StaticRefs.COMPLETE
                }
                finish()

                TastyToast.makeText(context, message, Toast.LENGTH_LONG, TastyToast.SUCCESS).show()
            }

        }
    }

    fun getBusinessData() {
        pd.show()
        Fuel.post(StaticRefs.VENDORBUSINESSSHOW, listOf((StaticRefs.PBVENDORID to prefs.vendorid), StaticRefs.TOKEN to prefs.token))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->

                        parseBusinessdata(result.get().content)

                    }, { err ->
                        pd.dismiss()
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })

                }
    }

    fun parseBusinessdata(response: String) {
        pd.dismiss()
        var jsonob: JSONObject? = null
        // data = JSONObject(response).getJSONObject("data")
        val json = JSONObject(response)
        if (response.contains(StaticRefs.DATA) && json.getString(StaticRefs.DATA) != null && !json.getString(StaticRefs.DATA).equals("")) {
            val data1 = json.getString(StaticRefs.DATA)
            val jsondata = JSONObject(data1)
            if (jsondata.length() > 0) {


                val data = json.getJSONObject(StaticRefs.DATA)


                lsBusninessname = data.getString(StaticRefs.PBBUSINESSNAME)
                lsEstablishon = data.getString(StaticRefs.PBESTABLISHON)
                lsIntroduction = data.getString(StaticRefs.PBINTRODUCTION)
                lsusp = data.getString(StaticRefs.PBUSP)
                lsweburl = data.getString(StaticRefs.PBWEBURL)
                lsBusinesscontactno = data.getString(StaticRefs.PBBUSINESSCONTACTNO)
                if (response.contains(StaticRefs.PBSERVICEAREA) || !StaticRefs.PBSERVICEAREA.equals(null)) {

                    lsServiceareinkm = data.getString(StaticRefs.PBSERVICEAREA)
                } else {

                }
                if (response.contains(StaticRefs.PBLOGO)) {
                    val image = data.getString(StaticRefs.PBLOGO)
                    if (!(image.equals(null) || image.equals("null") || image.equals("NULL"))) {
                        lslogoimagebas64 = data.getString(StaticRefs.PBLOGO)
                    }
                }

                addjson = data.getJSONArray(StaticRefs.BUSINESSADD)

                adddata = addjson.toString()

                val add = adddata
                val jsonadd = JSONArray(add)
                var json1: JSONObject? = null

                for (i in 0..jsonadd.length() - 1) {

                    json1 = jsonadd.getJSONObject(i)

                    lsBuilding = json1!!.getString(StaticRefs.ADDRESSLINE1)
                    lsaddline2 = json1!!.getString(StaticRefs.ADDRESSLINE2)
                    longitude=json1!!.getString(StaticRefs.ADDRESSLONGITUDE).toDouble()
                    lattitude=json1!!.getString(StaticRefs.ADDRESSLATITUDE).toDouble()


                    val tokens2 = StringTokenizer(lsEstablishon, "-")
                    lsYear = tokens2.nextToken()
                    lsMonth = tokens2.nextToken()

                }
                lsCompleteAddress = lsBuilding + ", " + lsaddline2

                setData()
            } else {
                TastyToast.makeText(context, getString(R.string.No_Data_Found), TastyToast.LENGTH_SHORT, TastyToast.WARNING).show()

            }
        } else {
            pd.dismiss()
            TastyToast.makeText(context, getString(R.string.No_Data_Found), TastyToast.LENGTH_SHORT, TastyToast.WARNING).show()
        }
    }

    fun setData() {

        etBusinessName.setText(lsBusninessname)
        etBusinessintroduction.setText(lsIntroduction)
        etEyear.setText(lsYear)
        etEmonth.setText(lsMonth)
        etYccu.setText(lsusp)
        etWeburl.setText(lsweburl)
        etBusinesscontactno.setText(lsBusinesscontactno)
        cvBaddress.visibility = View.VISIBLE
        tvBaddress.setText(lsCompleteAddress)

        if (!(lsServiceareinkm.equals(null) || lsServiceareinkm.equals(""))) {
            val radius: Float
            radius = lsServiceareinkm.toFloat()
            val progress: Int
            progress = (radius * 10).toInt()
            seekBar.setProgress(progress)
            tvrangeinkm.setText(getString(R.string.Radius_is) + lsServiceareinkm +getString(R.string.KM))
        }
        if (!(lslogoimagebas64.equals(null) || lslogoimagebas64.equals("NULL") || lslogoimagebas64.equals("null") || lslogoimagebas64.equals(blanckstring))) {
            cvLogoImage.loadBase64Image(lslogoimagebas64)
        }


    }

    fun getAddresswithpopup() {
        val layoutInflater = LayoutInflater.from(this)
      var  cal=layoutInflater.inflate(R.layout.address_dialog,null)

        etBuilding = cal.findViewById<EditText>(R.id.etBuilding)
         etStreet = cal.findViewById<EditText>(R.id.etStreet)

         cbCancel = cal.findViewById<Button>(R.id.cbCancel)
       cbOK = cal.findViewById<Button>(R.id.cbOK)
        cbChangeAddress=cal.findViewById<Button>(R.id.cbChangeAddress)



        if (!(lsCompleteAddress.equals(null) || lsCompleteAddress.equals(""))) {

            etBuilding.setText(lsBuilding)

            etStreet.setText(lsaddline2)


        }


        val popup1 = android.support.v7.app.AlertDialog.Builder(this)
        popup1.setView(cal)
        popup1.setCancelable(true)
        alertdialog = popup1.create()
        alertdialog.show()

        cbChangeAddress.setOnClickListener {
            autocomplete()
        }
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
                    TastyToast.makeText(context, getString(R.string.Please_Enter_All_Details), Toast.LENGTH_LONG, TastyToast.WARNING).show()
                } else {
                    TastyToast.makeText(context, getString(R.string.Please_Enter_Valid )+ lsMessage, Toast.LENGTH_LONG, TastyToast.WARNING).show()
                }

            } else {
                lsaddline2 = lsStreet
                lsCompleteAddress = lsBuilding + ", " + lsaddline2

                cvBaddress.visibility = View.VISIBLE
                tvBaddress.setText(lsCompleteAddress)
                alertdialog.dismiss()

            }

        }

        cbCancel.setOnClickListener(View.OnClickListener {
            alertdialog.dismiss()

        })

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

    fun FinalBase64(encoded: String?) {
        lslogoimagebas64 = StaticRefs.BASE64LINK + encoded.toString().trim()

    }

    override fun onBackPressed() {

        super.onBackPressed()

    }

    fun getCoordinatesFromAdd(lsAddVal: String) {
        try {
            val address = coder.getFromLocationName(lsAddVal, 1);
            if (address == null) {
                toast("Address is empty")
            }
            val location = address!!.get(0);
            lattitude = location.getLatitude();
            longitude = location.getLongitude();

            toast("MyLat $lattitude , MyLong $longitude")
            Log.d("TAG", "latitude" + lattitude + "Longiude" + longitude)

        } catch (e: Exception) {
            println(e)
        }
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

