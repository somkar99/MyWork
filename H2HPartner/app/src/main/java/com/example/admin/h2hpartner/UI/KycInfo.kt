package com.example.admin.h2hpartner.UI

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.admin.h2hpartner.App
import com.example.admin.h2hpartner.R
import kotlinx.android.synthetic.main.kycdetails.*
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log

import java.io.File
//import android.support.test.espresso.core.internal.deps.guava.io.ByteStreams.toByteArray
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.GoMobeil.H2H.Extensions.getBitmapFromUri
import com.GoMobeil.H2H.Extensions.toast


import com.beust.klaxon.JsonObject
import com.example.admin.h2hpartner.Adapter.KycAdapter
import com.example.admin.h2hpartner.BaseActivity
import com.example.admin.h2hpartner.Models.UploadImageModel
import com.example.admin.h2hpartner.Services.*
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast

import org.json.JSONArray
import org.json.JSONObject


/**
 * Created by Admin on 06-02-18.
 */
class KycInfo : BaseActivity() {

    companion object {
        var TAG: String? = "KycInfo"
        lateinit var uploadkyc: MutableList<UploadImageModel>
        var lbAddressChanged: Boolean? = false
        var lbDocumentsChanged: Boolean? = false
        var ivBackForProfile: String? = null
        var IMAGETYPE = "K"

    }

    override lateinit var context: Context
    override lateinit var activity: Activity

    lateinit var rcvList: RecyclerView
    lateinit var rcvAdapter: RecyclerView.Adapter<KycAdapter.ViewHolder>
    lateinit var layoutManager: RecyclerView.LayoutManager
    var adapter: KycAdapter? = null
    var UPLOAD_DOCUMENT: Int? = 200
    var EDIT_DOCUMENT: Int? = 300


    lateinit var kycimagearray: JSONArray
    var lsFinalBase64: String? = null
    var lsTitle: String? = ""
    var lsImage: String? = ""
    var liSrNo: Int? = null
    lateinit var pd: TransperantProgressDialog


    var lsAdharcardno: String = ""
    var lsMessage = ""
    var base = ""
    private val PICKFILE_RESULT_CODE = 50
    val TAG = "KycDocs"
    lateinit var bmp: Bitmap
    var lsImagetitle: String? = null


    lateinit var imageslist: List<UploadImageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kycdetails)
        hideFooter(true)
        setTitle(getString(R.string.Kyc_Details))
        activity = this
        context = this
        CustomServices.hideKB(activity)

        App.prefs?.dashboard(tvKycName, tvKycNumber, tvKycPribusiness)
        initlayout()
        getkycimages()
    }

    fun initlayout() {
        pd = TransperantProgressDialog(context)
        KycInfo.uploadkyc = mutableListOf()
        rcvList = findViewById(R.id.rcvkycimages)
        layoutManager = LinearLayoutManager(this);
        rcvList.layoutManager = layoutManager

        /*ivAddImg.setOnClickListener {
            val intent = Intent(this, SelectKycImage::class.java)
            startActivityForResult(intent, UPLOAD_DOCUMENT!!)
        }*/

        ivAddImg.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SelectKycImage::class.java)
            startActivityForResult(intent, UPLOAD_DOCUMENT!!)
        })

        cbKycsave.setOnClickListener {

            getData()
        }

    }

    fun getData() {

        var lbProceedAhead = true;
        val lsadharcard = etAdharcardno.text.toString().trim()


        if (!((lsadharcard != null && lsadharcard.length > 0))) {
            genMessage(getString(R.string.Adhar_Card))
            lbProceedAhead = false;
        } else {
            lsAdharcardno = lsadharcard
        }

        if (lbProceedAhead) {

            if (!StaticRefs.validateadhar(lsAdharcardno)) {
                etAdharcardno.setError(getString(R.string.Enter_Valid_Adhar_Number))
                lbProceedAhead = false
                Toast.makeText(context, getString(R.string.Enter_Valid_Adhar_Number), Toast.LENGTH_LONG).show()

            }
        }

        if (!(lbProceedAhead) && lsMessage.length > 0) {
            TastyToast.makeText(context, getString(R.string.Please_Enter_Valid) + lsMessage, Toast.LENGTH_LONG, TastyToast.WARNING).show()

        } else {
            // saveData()
            saveKycDetails()
        }

    }

    fun saveData() {

        TastyToast.makeText(context, getString(R.string.All_Data_Valided_Successfully), Toast.LENGTH_LONG, TastyToast.WARNING).show()

    }

    fun genMessage(msg: String) {
        if (lsMessage.length > 0) {
            lsMessage = lsMessage + ", " + msg;
        } else {
            lsMessage = lsMessage + " " + msg;
        }
    }

    fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted")
                return true
            } else {

                Log.v(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted")
            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPLOAD_DOCUMENT && resultCode == RESULT_OK) {

            KycInfo.lbDocumentsChanged = true
            KycInfo.ivBackForProfile = "backForProfile"

            var jsonData: String? = null
            jsonData = data!!.getStringExtra("DOCS_LIST")

            val json = JSONObject(jsonData)
            val response1 = json.getString(StaticRefs.DATA)

            val json2 = JSONObject(response1)
            lsTitle = json2.optString(UploadImageModel.IMAGENAME)
            lsImage = json2.optString(UploadImageModel.IMAGE)
            //  liSrNo = json2.optInt(UploadImageModel.IMAGESERNO)

            var lsImageRealPath = Uri.fromFile(File(StaticRefs.lsImageDirectory + "/" + lsImage))
            var bmp = getBitmapFromUri(lsImageRealPath)

            var convertase64 = ConvertToBase64(this, context, bmp!!)
            convertase64!!.execute()
            setAdapter()
        } else if (requestCode == EDIT_DOCUMENT && resultCode == RESULT_OK) {
            KycInfo.lbDocumentsChanged = true
            KycInfo.ivBackForProfile = "backForProfile"

            var status = data!!.getStringExtra("STATUS")

            if (status.equals("EDIT")) {
                var pos = data!!.getIntExtra("Position", 0)

                KycInfo.uploadkyc.removeAt(pos)

                var jsonData: String? = null
                jsonData = data!!.getStringExtra("DOCS_LIST")

                val json = JSONObject(jsonData)
                val response1 = json.getString(StaticRefs.DATA)

                val json2 = JSONObject(response1)
                lsTitle = json2.optString(UploadImageModel.IMAGENAME)
                lsImage = json2.optString(UploadImageModel.IMAGE)
                liSrNo = json2.optInt(UploadImageModel.IMAGESERNO)

                var lsImageRealPath = Uri.fromFile(File(StaticRefs.lsImageDirectory + "/" + lsImage))
                var bmp = getBitmapFromUri(lsImageRealPath)

                var convertase64 = ConvertToBase64(this, context, bmp!!)
                convertase64!!.execute()
                setAdapter()
            } else if (status.equals("DELETE")) {
                var pos = data!!.getIntExtra("Position", 0)
                KycInfo.uploadkyc.removeAt(pos)
                adapter!!.notifyDataSetChanged()
            }
        } else {

            println("hello")
        }


    }

    fun ReturnThreadResult(encoded: String?) {
        lsFinalBase64 = StaticRefs.BASE64LINK + encoded.toString().trim()

        var json1 = JsonObject();
        json1.put(UploadImageModel.IMAGENAME, lsTitle);
        json1.put(UploadImageModel.IMAGE, lsFinalBase64)
        //  json1.put(UploadImageModel.IMAGESERNO, liSrNo)
        val model = UploadImageModel(json1)
        KycInfo.uploadkyc.add(model)
        // adapter.notifyItemInserted(docsModel.size - 1)
        adapter!!.notifyDataSetChanged()
    }

    fun setAdapter() {

        adapter = KycAdapter(KycInfo.uploadkyc);
        rcvAdapter = adapter!!
        rcvList.adapter = rcvAdapter;
        rcvAdapter.notifyDataSetChanged();

        adapter!!.setItemClickIterface(object : KycAdapter.itemClickIterface {
            override fun itemClick(position: Int, lsValue: String) {

                if (lsValue.equals("Show")) {

                    var intent = Intent(applicationContext, SelectKycImage::class.java)
                    intent.putExtra("Position", position + 1)
                    startActivityForResult(intent, EDIT_DOCUMENT!!)

                } else if (lsValue.equals("Delete")) {
                    var lsTitle = KycInfo.uploadkyc.get(position).image_name
                    showDeletePopup(position, lsTitle!!, getString(R.string.Confirmation), getString(R.string.Are_You_Want_Delete_Documents))
                }
            }

        })

    }

    private fun showPopup(lsHeader: String, lsMessage: String) {
        val dialog = CustomDialog(activity, context)
        CustomServices.hideSoftKeyboard(this)
        dialog.setCancel(false)
        dialog.setOutsideTouchable(false)
        dialog.setTitle(lsHeader)
        dialog.setMessage("$lsMessage")
        dialog.showDialog()
        dialog.setDialogButtonClickListener(object : CustomDialog.DialogButtonClick {
            override fun DialogButtonClicked(view: View) {
                when (view.id) {
                    R.id.cbOK -> {
                        cbKycsave.performClick()
                    }

                    R.id.cbCancel -> {
                        dialog.dismiss()
                        finish()
                    }
                }
            }
        })
    }

    private fun showDeletePopup(pos: Int, title: String, lsHeader: String, lsMessage: String) {
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
                        lbDocumentsChanged = true
                        ivBackForProfile = "backForProfile"

                        uploadkyc.removeAt(pos)
                        dialog.dismiss()
                        adapter!!.notifyDataSetChanged()
                    }

                    R.id.cbCancel -> dialog.dismiss()
                }
            }
        })
    }

    fun saveKycDetails() {

        kycimagearray = JSONArray()

        if (uploadkyc.size > 0) {


            for (i in 0..uploadkyc.size - 1) {
                var kycimageobj = JSONObject()
                kycimageobj.put(StaticRefs.IMAGEVENDERID, prefs.vendorid)
                kycimageobj.put(StaticRefs.IMAGETYPE, IMAGETYPE)
                kycimageobj.put(StaticRefs.IMAGENAME, uploadkyc[i].image_name)
                kycimageobj.put(StaticRefs.IMAGE, uploadkyc[i].image)
                kycimageobj.put(StaticRefs.IMAGECREATEDBY, prefs.fullname)
                kycimageobj.put(StaticRefs.IMAGEISACTIVE, "Y")

                kycimagearray.put(kycimageobj)
            }

            pd.show()
            Fuel.post(StaticRefs.VENDORUPLOADIMAGES, listOf(StaticRefs.TOKEN to prefs.token,
                    (StaticRefs.AADHARNO to lsAdharcardno),
                    (StaticRefs.DOCS to kycimagearray.toString())))
                    .timeoutRead(StaticRefs.TIMEOUTREAD)
                    .responseJson()
                    { request,
                      response,
                      result ->
                        // parseuploadimageresponse(result.get().content)
                        result.fold({ d ->

                            parseuploadimageresponse(result.get().content)

                        }, { err ->
                            pd.dismiss()
                            TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        })
                    }
        } else {

            TastyToast.makeText(context, getString(R.string.Please_Upload_Atlest_One_Image), TastyToast.LENGTH_LONG, TastyToast.ERROR)

        }


    }

    private fun parseuploadimageresponse(response: String) {
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
                pd.dismiss()

                if (prefs.profilestatus.equals(StaticRefs.INCOMPLETE)) {
                    prefs.kycinfostatus = StaticRefs.COMPLETE
                }
                finish()
                TastyToast.makeText(context, message, Toast.LENGTH_LONG, TastyToast.WARNING).show()
            }

        }
    }

    fun getkycimages() {
        pd.show()
        Fuel.post(StaticRefs.VENDORUPLOADIMAGESSHOW, listOf((StaticRefs.IMAGEVENDERID to prefs.vendorid),
                (StaticRefs.IMAGETYPE to IMAGETYPE),
                StaticRefs.TOKEN to prefs.token))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->

                    //showkycimages(result.get().content)
                    result.fold({ d ->

                        showkycimages(result.get().content)

                    }, { err ->
                        pd.dismiss()
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })


                }

    }

    private fun showkycimages(response: String) {
        pd.dismiss()
        val json = JSONObject(response)
        if (response.contains(StaticRefs.AADHARNO) && json.getString(StaticRefs.AADHARNO) != null && !json.getString(StaticRefs.AADHARNO).equals("")) {
            lsAdharcardno = json.getString(StaticRefs.AADHARNO)
        } else {
            TastyToast.makeText(context, getString(R.string.Adhar_Card_Details_Not_Stored), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show()

        }
        if (response.contains(StaticRefs.DATA) && json.getString(StaticRefs.DATA) != null && !json.getString(StaticRefs.DATA).equals("")) {

            val data = json.getString(StaticRefs.DATA)
            val jsondata = JSONArray(data)
            if (jsondata.length() > 0) {

                val refjsonarr = json.getJSONArray(StaticRefs.DATA)
                if (refjsonarr.length() > 0) {

                    var json1: JSONObject? = null
                    for (i in 0..refjsonarr.length() - 1) {
                        json1 = refjsonarr.getJSONObject(i)
                        val refjson: JsonObject
                        refjson = JsonObject()

                        refjson.put(UploadImageModel.IMAGEVENDERID, json1.getString(StaticRefs.IMAGEVENDERID))
                        refjson.put(UploadImageModel.IMAGESERNO, json1.getString(StaticRefs.IMAGESERNO))
                        refjson.put(UploadImageModel.IMAGETYPE, json1.getString(StaticRefs.IMAGETYPE))
                        refjson.put(UploadImageModel.IMAGENAME, json1.getString(StaticRefs.IMAGENAME))
                        refjson.put(UploadImageModel.IMAGE, json1.getString(StaticRefs.IMAGE))
                        refjson.put(UploadImageModel.IMAGEISACTIVE, json1.getString(StaticRefs.IMAGEISACTIVE))
                        val model = UploadImageModel(refjson)
                        KycInfo.uploadkyc.add(model)
                    }
                    setAdapter()
                    etAdharcardno.setText(lsAdharcardno)
                }
            } else {
                TastyToast.makeText(context, getString(R.string.No_Data_Found), Toast.LENGTH_SHORT, TastyToast.WARNING).show()
            }
        } else {
            pd.dismiss()
        }
    }


}






