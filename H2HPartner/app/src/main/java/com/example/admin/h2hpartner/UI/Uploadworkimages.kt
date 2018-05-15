package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.GoMobeil.H2H.Extensions.getBitmapFromUri
import com.GoMobeil.H2H.Extensions.toast
import com.beust.klaxon.JsonObject
import com.example.admin.h2hpartner.Adapter.WorkAdapter
import com.example.admin.h2hpartner.App
import com.example.admin.h2hpartner.BaseActivity
import com.example.admin.h2hpartner.Models.AddReferencesModel
import com.example.admin.h2hpartner.Models.UploadImageModel
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.CustomDialog
import com.example.admin.h2hpartner.Services.CustomServices
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.uploadworkimages.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * Created by Admin on 06-02-18.
 */
class Uploadworkimages : BaseActivity() {

    companion object {
        var TAG: String? = "Work_Images"
        lateinit var uploadworkimages: MutableList<UploadImageModel>
        var lbAddressChanged: Boolean? = false
        var lbDocumentsChanged: Boolean? = false
        var ivBackForProfile: String? = null
        var IMAGETYPE = "W"

    }

    override lateinit var context: Context
    override lateinit var activity: Activity

    lateinit var workimagearray: JSONArray
    lateinit var rcvList: RecyclerView
    lateinit var rcvAdapter: RecyclerView.Adapter<WorkAdapter.ViewHolder>
    lateinit var layoutManager: RecyclerView.LayoutManager
    var adapter: WorkAdapter? = null
    var UPLOAD_DOCUMENT: Int? = 200
    var EDIT_DOCUMENT: Int? = 300

    var lsFinalBase64: String? = null
    var lsTitle: String? = ""
    var lsImage: String? = ""
    var liSrNo: Int? = null
    lateinit var pd: TransperantProgressDialog

    var lsMessage = ""
    var base = ""
    private val PICKFILE_RESULT_CODE = 50
    val TAG = "Workimages"

    lateinit var imageslist: List<UploadImageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.uploadworkimages)
        hideFooter(true)
        setTitle(getString(R.string.Work_Images))
        context = this
        activity = this
        CustomServices.hideKB(activity)
        pd = TransperantProgressDialog(context)
        App.prefs?.dashboard(tvUploadName, tvUploadMobileno, tvUploadPrimarybusiness)
        initlayout()
        getworkimages()
    }

    fun initlayout() {

        Uploadworkimages.uploadworkimages = mutableListOf()
        rcvList = findViewById(R.id.rcvWorkImages)
        layoutManager = LinearLayoutManager(this);
        rcvList.layoutManager = layoutManager

        ivAddImages.setOnClickListener {

            val intent = Intent(this, SelectWorkImages::class.java)
            startActivityForResult(intent, UPLOAD_DOCUMENT!!)
        }

        cbSaveImages.setOnClickListener {
            saveWorkImages()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UPLOAD_DOCUMENT && resultCode == RESULT_OK) {

            Uploadworkimages.lbDocumentsChanged = true
            Uploadworkimages.ivBackForProfile = "backForProfile"

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
            val encoded = CustomServices.ConverttoBase64(bmp)
            FinalBase64(encoded)

            /* var convertase64 = ConvertToBase64(this, context, bmp!!)
             convertase64!!.execute()*/
            setAdapter()
        } else if (requestCode == EDIT_DOCUMENT && resultCode == RESULT_OK) {
            Uploadworkimages.lbDocumentsChanged = true
            Uploadworkimages.ivBackForProfile = "backForProfile"

            var status = data!!.getStringExtra("STATUS")

            if (status.equals("EDIT")) {
                var pos = data!!.getIntExtra("Position", 0)

                Uploadworkimages.uploadworkimages.removeAt(pos)

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

                //   var convertase64 = ConvertToBase64(this, context, bmp!!)
                val encoded = CustomServices.ConverttoBase64(bmp)
                FinalBase64(encoded)
                //  convertase64!!.execute()
                setAdapter()
            } else if (status.equals("DELETE")) {
                var pos = data!!.getIntExtra("Position", 0)
                Uploadworkimages.uploadworkimages.removeAt(pos)
                rcvAdapter!!.notifyDataSetChanged()
            }
        }


    }

    fun setAdapter() {

        adapter = WorkAdapter(Uploadworkimages.uploadworkimages);
        rcvAdapter = adapter!!
        rcvList.adapter = rcvAdapter;
        rcvAdapter.notifyDataSetChanged();

        adapter!!.setItemClickIterface(object : WorkAdapter.itemClickIterface {
            override fun itemClick(position: Int, lsValue: String) {

                if (lsValue.equals("Show")) {

                    var intent = Intent(applicationContext, SelectWorkImages::class.java)
                    intent.putExtra("Position", position + 1)
                    startActivityForResult(intent, EDIT_DOCUMENT!!)

                } else if (lsValue.equals("Delete")) {
                    var lsTitle = Uploadworkimages.uploadworkimages.get(position).image_name
                    showDeletePopup(position, lsTitle!!, getString(R.string.Confirmation), getString(R.string.Are_You_Want_Delete_Documents))
                }
            }

        })

    }

    fun FinalBase64(encoded: String?) {
        lsFinalBase64 = StaticRefs.BASE64LINK + encoded.toString().trim()

        var json1 = JsonObject();
        json1.put(UploadImageModel.IMAGENAME, lsTitle);
        json1.put(UploadImageModel.IMAGE, lsFinalBase64)
        //   json1.put(UploadImageModel.IMAGESERNO, liSrNo)
        val model = UploadImageModel(json1)
        Uploadworkimages.uploadworkimages.add(model)
        // adapter.notifyItemInserted(docsModel.size - 1)
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
                        cbSaveImages.performClick()
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
                        Uploadworkimages.lbDocumentsChanged = true
                        Uploadworkimages.ivBackForProfile = "backForProfile"

                        Uploadworkimages.uploadworkimages.removeAt(pos)
                        dialog.dismiss()
                        adapter!!.notifyDataSetChanged()
                    }

                    R.id.cbCancel -> dialog.dismiss()
                }
            }
        })
    }

    fun saveWorkImages() {

        workimagearray = JSONArray()

        if (uploadworkimages.size > 0) {


            for (i in 0..Uploadworkimages.uploadworkimages.size - 1) {
                var workimageobj = JSONObject()
                workimageobj.put(StaticRefs.IMAGEVENDERID, prefs.vendorid)
                workimageobj.put(StaticRefs.IMAGETYPE, Uploadworkimages.IMAGETYPE)
                workimageobj.put(StaticRefs.IMAGENAME, Uploadworkimages.uploadworkimages[i].image_name)
                workimageobj.put(StaticRefs.IMAGE, Uploadworkimages.uploadworkimages[i].image)
                workimageobj.put(StaticRefs.IMAGECREATEDBY, prefs.fullname)
                workimageobj.put(StaticRefs.IMAGEISACTIVE, "Y")


                workimagearray.put(workimageobj)
            }

            pd.show()
            Fuel.post(StaticRefs.VENDORUPLOADIMAGES, listOf(StaticRefs.TOKEN to prefs.token,
                    (StaticRefs.DOCS to workimagearray.toString())))
                    .timeoutRead(StaticRefs.TIMEOUTREAD)
                    .responseJson()
                    { request,
                      response,
                      result ->
                        // parseUploadImageResponse(result.get().content)
                        result.fold({ d ->

                            parseUploadImageResponse(result.get().content)

                        }, { err ->
                            pd.dismiss()
                            TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        })
                    }


        } else {

            TastyToast.makeText(this, "Please Upload atlest One Image", Toast.LENGTH_LONG, TastyToast.WARNING).show()


        }


    }

    private fun parseUploadImageResponse(response: String) {
        pd.dismiss()
        val json = JSONObject(response)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {

                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                pd.dismiss()

                val error = json.getString("errors")
                TastyToast.makeText(this, error, Toast.LENGTH_LONG, TastyToast.ERROR).show()
            } else if (json.getString(StaticRefs.STATUS).equals(StaticRefs.SUCCESS)) {

                if (prefs.profilestatus.equals(StaticRefs.INCOMPLETE) || prefs.workinfostatus.equals("") || prefs.workinfostatus.equals(StaticRefs.INCOMPLETE) ) {
                    prefs.workinfostatus = StaticRefs.COMPLETE
                }
                finish()

            }

        }
    }

    fun getworkimages() {
        pd.show()
        Fuel.post(StaticRefs.VENDORUPLOADIMAGESSHOW, listOf((StaticRefs.IMAGEVENDERID to prefs.vendorid),
                (StaticRefs.IMAGETYPE to IMAGETYPE),
                StaticRefs.TOKEN to prefs.token))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                    //showworkimages(result.get().content)
                    result.fold({ d ->

                        showworkimages(result.get().content)

                    }, { err ->
                        pd.dismiss()
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })


                }

    }

    fun showworkimages(response: String) {
        pd.dismiss()
        val json = JSONObject(response)

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
                        Uploadworkimages.uploadworkimages.add(model)
                    }
                    setAdapter()
                } else {
                    TastyToast.makeText(context, getString(R.string.No_Image_Found), Toast.LENGTH_SHORT, TastyToast.INFO).show()

                }
            } else {
                TastyToast.makeText(context, getString(R.string.No_Data_Found), Toast.LENGTH_SHORT, TastyToast.INFO).show()

            }

        } else {

            TastyToast.makeText(context, getString(R.string.No_Data_Found), Toast.LENGTH_SHORT, TastyToast.INFO).show()


        }
    }


}