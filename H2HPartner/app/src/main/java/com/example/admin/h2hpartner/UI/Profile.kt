/*
package com.GoMobeil.Perks.UI

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.GoMobeil.Perks.Adapters.DocsAdapter
import com.GoMobeil.Perks.Models.DocsModel
import com.GoMobeil.Perks.Models.ProfileModel
import com.GoMobeil.Perks.Services.CustomServices
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import kotlinx.android.synthetic.main.profile.*
import kotlinx.android.synthetic.main.profile1.*
import org.json.JSONObject
import android.content.Intent
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.GoMobeil.Perks.*
import kotlinx.android.synthetic.main.profile2.*
import org.json.JSONArray
import android.net.Uri
import android.util.DisplayMetrics
import android.view.WindowManager
import com.GoMobeil.Perks.Extensions.*
import com.GoMobeil.Perks.Services.ConvertToBase64
import com.GoMobeil.Perks.Services.TransperantProgressDialog
import java.io.File
import com.GoMobeil.Perks.Services.CustomDialog
import com.example.admin.h2hpartner.R
import com.sdsmdg.tastytoast.TastyToast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


*
 * Created by ADMIN on 12-02-2018.


class Profile : BaseActivity() {
    companion object {
        var TAG: String? = "Profile"
        lateinit var docsModel: MutableList<DocsModel>
        var lbAddressChanged: Boolean? = false
        var lbDocumentsChanged: Boolean? = false
        var ivBackForProfile: String? = null
    }

    override lateinit var context: Context
    override lateinit var activity: Activity
    lateinit var profileModel: List<ProfileModel>
    var liCurrentPos = 0;
lateinit var alDocs: ArrayList<DocsModel>

    lateinit var adapter: DocsAdapter
    lateinit var rcvAdapter: RecyclerView.Adapter<DocsAdapter.ViewHolder>
    lateinit var layoutManager: RecyclerView.LayoutManager
    var lsAddLine1: String? = null
    var lsAddLine2: String? = null
    var lsAddCity: String? = null
    var lsAddState: String? = null
    var lsPincode: String? = null
    var lsMobile: String? = null
    var lsAltMobile: String? = null
    var EDIT_PROFILE: Int? = 100
    var UPLOAD_DOCUMENT: Int? = 200
    var EDIT_DOCUMENT: Int? = 300
    var jsonAddress: JSONObject? = JSONObject()
    var jsonDocsArr: JSONArray? = JSONArray()
    var jsonAddArr: JSONArray? = JSONArray()
    var alImages: ArrayList<String>? = ArrayList()
    var lsFinalBase64: String? = null
    var lsTitle: String? = ""
    var lsImage: String? = ""
    var liSrNo: Int? = null
    lateinit var pd: TransperantProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.profile)
        setTitle("Profile")

        context = this
        activity = this

        initLayout()
    }

    fun initLayout(): Unit {

        pd = TransperantProgressDialog(context)

        getProfile()

        llEditProfile.setBackgroundColor(context.resources.getColor(R.color.red_500))
        llServiceUsage.setBackgroundColor(context.resources.getColor(R.color.white))
        llMembership.setBackgroundColor(context.resources.getColor(R.color.white))

        editProf.visibility = View.VISIBLE
        membership.visibility = View.GONE
        serviceUsage.visibility = View.GONE

        CustomServices.hideKB(this)
        llEditProfile.setOnClickListener(this)
        llMembership.setOnClickListener(this)
        llServiceUsage.setOnClickListener(this)
        cvEditProfileAddress.setOnClickListener(this)
        cbUpdateProfile.setOnClickListener(this)
        llAddDocs.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)

        when (v!!.id) {
            R.id.llEditProfile -> {
                llEditProfile.setBackgroundColor(context.resources.getColor(R.color.red_500))
                llServiceUsage.setBackgroundColor(context.resources.getColor(R.color.white))
                llMembership.setBackgroundColor(context.resources.getColor(R.color.white))

                editProf.visibility = View.VISIBLE
                membership.visibility = View.GONE
                serviceUsage.visibility = View.GONE
                cbUpdateProfile.visibility = View.VISIBLE
            }

            R.id.llMembership -> {
                llMembership.setBackgroundColor(context.resources.getColor(R.color.red_500))
                llServiceUsage.setBackgroundColor(context.resources.getColor(R.color.white))
                llEditProfile.setBackgroundColor(context.resources.getColor(R.color.white))

                membership.visibility = View.VISIBLE
                editProf.visibility = View.GONE
                serviceUsage.visibility = View.GONE
                cbUpdateProfile.visibility = View.GONE

                showMemberShip()
            }

            R.id.llServiceUsage -> {
                llServiceUsage.setBackgroundColor(context.resources.getColor(R.color.red_500))
                llMembership.setBackgroundColor(context.resources.getColor(R.color.white))
                llEditProfile.setBackgroundColor(context.resources.getColor(R.color.white))

                serviceUsage.visibility = View.VISIBLE
                membership.visibility = View.GONE
                editProf.visibility = View.GONE
                cbUpdateProfile.visibility = View.GONE
                //showPieChart()
            }

            R.id.cvEditProfileAddress -> {
               // ivBackForProfile = null
                val b = Bundle()
                val intent = Intent(this, EditProfile::class.java)
                b.putString(StaticRefs.ADDLINE1, lsAddLine1)
                b.putString(StaticRefs.ADDLINE2, lsAddLine2)
                b.putString(StaticRefs.ADDCITY, lsAddCity)
                b.putString(StaticRefs.ADDSTATE, lsAddState)
                b.putString(StaticRefs.ADDPINCODE, lsPincode)
                b.putString(StaticRefs.MOBILE, lsMobile)
                b.putString(StaticRefs.ALTMOBILE, lsAltMobile)
                intent.putExtra(StaticRefs.DATA, b)
                startActivityForResult(intent, EDIT_PROFILE!!)
            }

            R.id.cbUpdateProfile -> {
                TastyToast.makeText(context, "Updated successfully", 30, TastyToast.SUCCESS).show();
                updateProfile()
            }

            R.id.llAddDocs -> {
               // ivBackForProfile = null
                val intent = Intent(this, UploadDocument::class.java)
                startActivityForResult(intent, UPLOAD_DOCUMENT!!)
            }
        }
    }

    override
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_PROFILE && resultCode == RESULT_OK) {

            lbAddressChanged = true
            ivBackForProfile = "backForProfile"

            lsAddLine1 = data!!.getStringExtra(StaticRefs.ADDLINE1)
            lsAddLine2 = data!!.getStringExtra(StaticRefs.ADDLINE2)
            lsAddCity = data!!.getStringExtra(StaticRefs.ADDCITY)
            lsAddState = data!!.getStringExtra(StaticRefs.ADDSTATE)
            lsPincode = data!!.getStringExtra(StaticRefs.ADDPINCODE)
            lsMobile = data!!.getStringExtra(StaticRefs.MOBILE)
            lsAltMobile = data!!.getStringExtra(StaticRefs.ALTMOBILE)

            tvRegisteredAdd.setText("$lsAddLine1 , $lsAddLine2 , $lsAddCity , $lsAddState , $lsPincode ")
            tvMobileNo.setText("$lsMobile")
            tvAlternateMob.setText("$lsAltMobile")

        } else if (requestCode == UPLOAD_DOCUMENT && resultCode == RESULT_OK) {

            lbDocumentsChanged = true
            ivBackForProfile = "backForProfile"

            var jsonData: String? = null
            jsonData = data!!.getStringExtra("DOCS_LIST")

            val json = JSONObject(jsonData)
            val response1 = json.getString(StaticRefs.DATA)

            val json2 = JSONObject(response1)
            lsTitle = json2.optString(DocsModel.CD_TITLE)
            lsImage = json2.optString(DocsModel.CD_IMAGE)
            liSrNo = json2.optInt(DocsModel.CD_SLNO)

            var lsImageRealPath = Uri.fromFile(File(StaticRefs.lsImageDirectory + "/" + lsImage))
            var bmp = getBitmapFromUri(lsImageRealPath)

            var convertase64 = ConvertToBase64(this, context, bmp!!)
            convertase64!!.execute()
        } else if (requestCode == EDIT_DOCUMENT && resultCode == RESULT_OK) {
            lbDocumentsChanged = true
            ivBackForProfile = "backForProfile"

            var status = data!!.getStringExtra("STATUS")

            if (status.equals("EDIT")) {
                var pos = data!!.getIntExtra("Position", 0)

                docsModel.removeAt(pos)

                var jsonData: String? = null
                jsonData = data!!.getStringExtra("DOCS_LIST")

                val json = JSONObject(jsonData)
                val response1 = json.getString(StaticRefs.DATA)

                val json2 = JSONObject(response1)
                lsTitle = json2.optString(DocsModel.CD_TITLE)
                lsImage = json2.optString(DocsModel.CD_IMAGE)
                liSrNo = json2.optInt(DocsModel.CD_SLNO)

                var lsImageRealPath = Uri.fromFile(File(StaticRefs.lsImageDirectory + "/" + lsImage))
                var bmp = getBitmapFromUri(lsImageRealPath)

                var convertase64 = ConvertToBase64(this, context, bmp!!)
                convertase64!!.execute()
            } else if (status.equals("DELETE")) {
                var pos = data!!.getIntExtra("Position", 0)
                docsModel.removeAt(pos)
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun getProfile() {
        pd.show()
        Fuel.post(StaticRefs.URLProfile, listOf((StaticRefs.ID to prefs.custid), (StaticRefs.TOKEN to prefs.token)))
                //Fuel.post(StaticRefs.URLLogin,listOf("email" to "anirudhaMahadik1@gmail.com",("password" to "alvin1000")))
                .responseJson()
                { request,
                  response,
                  result ->
                    parseProfile(result.get().content)
                }
    }

    fun parseProfile(response: String) {
        val json = JSONObject(response)

        val response1 = json.getString(StaticRefs.DATA)

        val parser = Parser()
        val stringBuilder = StringBuilder(response1)
        val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
        profileModel = model.map { ProfileModel(it) }.filterNotNull();

        try {
            var jsonArray = JSONArray(response1)
            var jsonAs: String? = null
            for (i in 0..jsonArray.length() - 1) {
                var jsonObj = jsonArray.getJSONObject(i)
                jsonAs = jsonObj.getString("customer_documents")
            }

            var jsonOb = JSONObject()
            jsonOb.put(StaticRefs.DATA, jsonAs)
            parseCustDocs(jsonOb.toString())
        } catch (e: Exception) {
        }
        pd.dismiss()
        showProfile()
    }

    fun parseCustDocs(response: String) {
        val json = JSONObject(response)
        val response1 = json.getString(StaticRefs.DATA)

        val parser = Parser()
        val stringBuilder = StringBuilder(response1)
        val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
        docsModel = model.map { DocsModel(it) }.filterNotNull() as MutableList<DocsModel>

        adapter = DocsAdapter(docsModel!!);
        rcvAdapter = adapter!!
        layoutManager = LinearLayoutManager(this);
        rcvDocs.adapter = rcvAdapter;
        rcvDocs.layoutManager = layoutManager
        rcvAdapter.notifyDataSetChanged();


        adapter.setItemClickIterface(object : DocsAdapter.itemClickIterface {
            override fun itemClick(position: Int, lsValue: String) {

                if (lsValue.equals(StaticRefs.SHOW_DOC)) {

                    var intent = Intent(applicationContext, UploadDocument::class.java)
                    intent.putExtra("Position", position + 1)
                    startActivityForResult(intent, EDIT_DOCUMENT!!)

                } else if (lsValue.equals(StaticRefs.DELETE_DOC)) {
                    var lsTitle = docsModel.get(position).cd_title
                    showDeletePopup(position, lsTitle!!, "Confirmation", "Are you sure , you want to delete document of")
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
                        .performClick()
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

                        docsModel.removeAt(pos)
                        dialog.dismiss()
                        adapter.notifyDataSetChanged()
                    }

                    R.id.cbCancel -> dialog.dismiss()
                }
            }
        })
    }


    fun showDocument(image: String, lstitle: String) {

        val layoutInflater = LayoutInflater.from(this)
        val dialogview = layoutInflater.inflate(R.layout.showimage, null)
        val ivImage = dialogview.findViewById<ImageView>(R.id.ivImage)
        val tvImage = dialogview.findViewById<TextView>(R.id.tvImage)

        val popup1 = android.support.v7.app.AlertDialog.Builder(this)
        popup1.setView(dialogview)
        var alertdialog = popup1.create()


        var displayMetrics = DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        var displayWidth = displayMetrics.widthPixels;
        var displayHeight = displayMetrics.heightPixels;
        var layoutParams = WindowManager.LayoutParams();
        layoutParams.copyFrom(alertdialog.getWindow().getAttributes());
        var dialogWindowWidth = (displayWidth * .8f).toInt();
        var dialogWindowHeight = (displayHeight * .6f).toInt();

        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;

        alertdialog.getWindow().setAttributes(layoutParams);

        alertdialog.show()

        ivImage.loadBase64Image(image);
        tvImage.setText(lstitle)

    }

    fun showMemberShip() {
        if ((profileModel[liCurrentPos].alCustMemPlan).size > 0) {

            tvPendMemSub.visibility = View.GONE
            llMemberTaken.visibility = View.VISIBLE

            tvPlan.setText(profileModel[liCurrentPos].alCustMemPlan[liCurrentPos].alMembershipPlan[liCurrentPos].mp_title)
            tvAnnualCost.setText("Annual Cost - Rs. " + profileModel[liCurrentPos].alCustMemPlan[liCurrentPos].alMembershipPlan[liCurrentPos].mp_fees + " /-")

            var lsStartDate = profileModel[liCurrentPos].alCustMemPlan[liCurrentPos].cm_startdate
            var lsEndDate = profileModel[liCurrentPos].alCustMemPlan[liCurrentPos].cm_enddate

            val sdf2 = SimpleDateFormat("yyyy-MM-dd")

            var startDate: Date? = null
            var endDate: Date? = null
            try {
                startDate = sdf2.parse(lsStartDate)
                endDate = sdf2.parse(lsEndDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val sDate = sdf.format(startDate)
            val eDate = sdf.format(endDate)

            tvValidFrom.setText(sDate)
            tvValidTill.setText(eDate)
        } else {
            llMemberTaken.visibility = View.GONE
            tvPendMemSub.visibility = View.VISIBLE
        }
    }

    fun showProfile() {

        if ((profileModel[liCurrentPos].alAddres).size > 0) {
            lsAddLine1 = profileModel[liCurrentPos].alAddres[liCurrentPos].add_line1
            lsAddLine2 = profileModel[liCurrentPos].alAddres[liCurrentPos].add_line2
            lsAddCity = profileModel[liCurrentPos].alAddres[liCurrentPos].add_city
            lsAddState = profileModel[liCurrentPos].alAddres[liCurrentPos].add_state
            lsPincode = profileModel[liCurrentPos].alAddres[liCurrentPos].add_pincode
        }

        if (lsAddLine1.equals(null)) {
            lsAddLine1 = ""
        }

        if (lsAddLine2.equals(null)) {
            lsAddLine2 = ""
        }

        if (lsAddCity.equals(null)) {
            lsAddCity = ""
        }

        if (lsAddState.equals(null)) {
            lsAddState = ""
        }

        if (lsPincode.equals(null)) {
            lsPincode = ""
        }

        tvRegisteredAdd.setText(lsAddLine1 + "," +
                lsAddLine2 + "," +
                lsAddCity + "," +
                lsAddState + "," + lsPincode)

        lsMobile = profileModel[liCurrentPos].cust_mobile
        lsAltMobile = profileModel[liCurrentPos].cust_altMob

        if (lsMobile.equals(null) || lsMobile.equals("")) {
            tvMobileNo.setText("-")
        } else {
            tvMobileNo.setText(lsMobile)
        }

        if (lsAltMobile.equals(null) || lsAltMobile.equals("")) {
            tvAlternateMob.setText("-")
        } else {
            tvAlternateMob.setText(lsAltMobile)
        }
    }

    fun updateProfile() {

        lbDocumentsChanged = false
        lbAddressChanged = false
        ivBackForProfile = null

        jsonAddress!!.put(StaticRefs.ADDLINE1, lsAddLine1)
        jsonAddress!!.put(StaticRefs.ADDLINE2, lsAddLine2)
        jsonAddress!!.put(StaticRefs.ADDCITY, lsAddCity)
        jsonAddress!!.put(StaticRefs.ADDSTATE, lsAddState)
        jsonAddress!!.put(StaticRefs.ADDPINCODE, lsPincode)

        jsonAddArr!!.put(jsonAddress)

        try {
            for (i in 0..docsModel!!.size - 1) {
                var json = JSONObject()
                json!!.put(StaticRefs.TITLE, docsModel!![i].cd_title)
                json!!.put(StaticRefs.IMAGE, docsModel!![i].cd_image)
                json!!.put(StaticRefs.SLNO, docsModel!![i].cd_slno)
                jsonDocsArr!!.put(json)

 if(docsModel!![i].cd_slno!! == 0)
                 {
                     alImages!!.add(docsModel!![i].cd_image!!)
                 }

            }

            // uploadOKImage(this!!.alImages!!,0,0)
        } catch (e: Exception) {
            println(e)
        }


        Fuel.post(StaticRefs.URLUpdateCustomer, listOf(
                (StaticRefs.TOKEN to prefs.token),
                (StaticRefs.ID to prefs.custid),
                (StaticRefs.ADDRESS to jsonAddArr.toString()),
                (StaticRefs.DOCS to jsonDocsArr.toString()),
                (StaticRefs.MOBILE to lsMobile),
                (StaticRefs.ALTMOBILE to lsAltMobile)))
                .responseJson()
                { request,
                  response,
                  result ->
                    parseResponse(result.get().content)
                }
    }

    fun parseResponse(response: String) {
        val json = JSONObject(response)

        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {
            var message = "";
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {
                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                //NGS CustomToast
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            } else {
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun ReturnThreadResult(encoded: String?) {
        lsFinalBase64 = "data:image/jpeg;base64," + encoded

        var json1 = JsonObject();
        json1.put(DocsModel.CD_TITLE, lsTitle);
        json1.put(DocsModel.CD_IMAGE, lsFinalBase64)
        json1.put(DocsModel.CD_SLNO, liSrNo)
        val model = DocsModel(json1)
        docsModel.add(model)
        // adapter.notifyItemInserted(docsModel.size - 1)
        adapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (lbAddressChanged == true || lbDocumentsChanged == true) {
            showPopup("Confirmation", "Changes have been made in Profile , Do you want to update it?")
        } else {
            super.onBackPressed()
        }
    }

    fun showPieChart() {
        piechart.setUsePercentValues(true)

        val yvalues = ArrayList<Entry>()
        yvalues.add(Entry(8f, 0))
        yvalues.add(Entry(15f, 1))
        yvalues.add(Entry(12f, 2))
        yvalues.add(Entry(25f, 3))
        yvalues.add(Entry(23f, 4))
        yvalues.add(Entry(17f, 5))

        val dataSet = PieDataSet(yvalues, "Election Results")

        val xVals = ArrayList<String>()
        xVals.add("January")
        xVals.add("February")
        xVals.add("March")
        xVals.add("April")
        xVals.add("May")
        xVals.add("June")

        val data = PieData(xVals, dataSet)

        data.setValueFormatter(PercentFormatter())

        piechart.setData(data);

        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        piechart.setDescription("This is Pie Chart");
        piechart.setDrawHoleEnabled(true);
        piechart.setHoleRadius(30f);
    }


}
*/
