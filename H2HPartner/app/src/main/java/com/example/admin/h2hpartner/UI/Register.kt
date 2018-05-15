package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.GoMobeil.H2H.Extensions.OnItemClickListener
import com.GoMobeil.H2H.Extensions.addOnItemClickListener
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.admin.h2hpartner.Adapter.ServiceListAdapter
import com.example.admin.h2hpartner.App
import com.example.admin.h2hpartner.Models.ServiceModel
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.register.*
import org.json.JSONArray
import org.json.JSONObject

class Register : Activity() {

    lateinit var context: Context
    lateinit var activity: Activity

    var lsMessage = ""
    var lsFName = ""
    var lsLName = ""
    var lsFullname = ""
    var lsEmail = ""
    var lsMobile = ""
    var lsPrimary = ""
    var lsPassword = ""
    var lsConfPassword = ""
    var lsReferralCode=""
    var lsDeviceid=""

    var service_id: Int = 0
    var service_name = ""
    var profilestatus: String? = null
    var statusjson: JSONArray? = null
    var kycstatus: String? = null
    var workstatus: String? = null
    var refstatus: String? = null
    var personalstatus: String? = null
    var businesstatus: String? = null
    var pricetatus: String? = null
    var rejectionreason: String? = null
    lateinit var rcvServicelist: RecyclerView
    lateinit var rcvadapter: RecyclerView.Adapter<ServiceListAdapter.ViewHolder>
    var adapter: ServiceListAdapter? = null
    lateinit var servicelist: MutableList<ServiceModel>
    lateinit var layoutManager: RecyclerView.LayoutManager
    var serviceModel: MutableList<ServiceModel>? = mutableListOf()
    lateinit var pd: TransperantProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        activity = this
        context = this

        /* ivbackPressed.visibility = View.VISIBLE
         ivMenu.visibility = View.VISIBLE
         ivNotifications.visibility = View.INVISIBLE
 */
        pd = TransperantProgressDialog(context)

        App.prefs?.clearDashboard()
        initlayout()

    }

    fun initlayout() {
        servicelist = mutableListOf()
        rcvServicelist = findViewById(R.id.rcvServicelist)
        layoutManager = LinearLayoutManager(context)
        rcvServicelist.layoutManager = layoutManager

        cbRegister.setOnClickListener {

            saveDetails()
        }

        etPrimaryBusiness.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {

                val lsprimarybusiness = etPrimaryBusiness.text.toString().trim()
                if (lsprimarybusiness.length > 0) {
                    if (serviceModel!!.size > 0) {
                        serviceModel?.clear()
                        adapter!!.notifyDataSetChanged()
                    }
                    rcvServicelist.visibility = View.VISIBLE
                    getPrimaryBusiness(lsprimarybusiness)
                }else{
                    serviceModel?.clear()

                    rcvServicelist.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
               // val lsprimary = etPrimaryBusiness.text.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               // val lsprimary = etPrimaryBusiness.text.toString()
            }

        })
        etPrimaryBusiness.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                rcvServicelist.visibility = View.GONE
            }
        }


    }

    fun getPrimaryBusiness(primarybusiness: String) {


        Fuel.post(StaticRefs.SERVICELIST, listOf(StaticRefs.TOKEN to prefs.token,
                (StaticRefs.SERVICENAME to primarybusiness))).timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                   // parseSeviceList(result.get().content)
                    result.fold({ d ->

                        parseSeviceList(result.get().content)
                    }, { err ->
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        //do something with error
                    })
                }


    }

    fun parseSeviceList(response: String) {

        val json = JSONObject(response)

        if (response.contains(StaticRefs.DATA) && json.getString(StaticRefs.DATA) != null && !json.getString(StaticRefs.DATA).equals("null"))
        {
            val data1=json.getJSONArray(StaticRefs.DATA)
            if(data1.length()>0) {

                val data = JSONObject(response).getString(StaticRefs.DATA)
                val parser = Parser()
                val stringBuilder = StringBuilder(data)
                val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
                serviceModel = model.map { ServiceModel(it) }.filterNotNull() as MutableList<ServiceModel>
                adapter = ServiceListAdapter(this!!.serviceModel!!);
                rcvadapter = adapter!!
                rcvServicelist.adapter = rcvadapter;
                rcvadapter.notifyDataSetChanged();
                rcvServicelist.addOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClicked(view: View, position: Int) {
                        service_id = serviceModel!!.get(position).lisrv_id
                        service_name = serviceModel!!.get(position).lssrv_name
                        etPrimaryBusiness.setText(service_name)
                        rcvServicelist.visibility = View.GONE
                    }

                })
            }else{
                //TastyToast.makeText(context, "Data is Empty", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                Log.d("Tag","Data is Empty")
                TastyToast.makeText(context, getString(R.string.No_Service_Exist), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                rcvServicelist.visibility = View.GONE


            }

    }
        else{
           // TastyToast.makeText(context, "No Data", TastyToast.LENGTH_LONG, TastyToast.ERROR)
            TastyToast.makeText(context, getString(R.string.No_Service_Exist), TastyToast.LENGTH_LONG, TastyToast.ERROR)
            rcvServicelist.visibility = View.GONE

        }


    }

    fun saveDetails() {

        lsFName = etFirstName.text.toString()
        lsLName = etLastName.text.toString()
        lsEmail = etEmail.text.toString()
        lsMobile = etMobileNumber.text.toString()
        lsPrimary = etPrimaryBusiness.text.toString()
        lsPassword = etPassword.text.toString()
        lsConfPassword = etConfPassword.text.toString()

        var lbProceedAhead = true;


        if (!((lsConfPassword != null && lsConfPassword.length > 0))) {
            genMessage(getString(R.string.Confirm_Password));
            lbProceedAhead = false;
        }

        if (!((lsFName != null && lsFName.length > 0))) {
            genMessage(getString(R.string.First_Name));
            lbProceedAhead = false;
        }



        if (!((lsLName != null && lsLName.length > 0))) {
            genMessage(getString(R.string.Last_Name));
            lbProceedAhead = false;
        }




        if (!((lsEmail != null && lsEmail.length > 0))) {
            genMessage(getString(R.string.Email))
            lbProceedAhead = false;
        }




        if (!((lsMobile != null && lsMobile.length > 0))) {
            genMessage(getString(R.string.Mobile_No))
            lbProceedAhead = false;
        }




        if (!((lsConfPassword != null && lsConfPassword.length > 0))) {
            genMessage(getString(R.string.Password))
            lbProceedAhead = false;
        }



        if (!((lsPassword != null && lsPassword.length > 0))) {
            genMessage(getString(R.string.Password))
            lbProceedAhead = false;
        }

        if (lbProceedAhead) {

            if (!(lsEmail.contains(getString(R.string.At_sign)))) {

                lbProceedAhead = false
                genMessage(getString(R.string.Valid_Email_Address))

            }

            if (!StaticRefs.isValidEmail(lsEmail)) {
                etEmail.setError(getString(R.string.Enter_Valid_Email))
                lbProceedAhead = false
                TastyToast.makeText(context, getString(R.string.Enter_Valid_Email), TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                return
            }

            if (!StaticRefs.isValidContact(lsMobile)) {
                etMobileNumber.setError(getString(R.string.Enter_Valid_MobileNo))
                lbProceedAhead = false
                TastyToast.makeText(context, getString(R.string.Enter_Valid_MobileNo), TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                return
            }
            if (!StaticRefs.isValidUser(lsFName)) {

                etFirstName.setError(getString(R.string.Enter_Valid_First_Name))
                lbProceedAhead = false
                TastyToast.makeText(context, getString(R.string.Enter_Valid_First_Name), TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                return
            }
            if (!StaticRefs.isValidUser(lsLName)) {
                etLastName.setError(getString(R.string.Enter_Valid_Last_Name))
                lbProceedAhead = false
                TastyToast.makeText(context, getString(R.string.Enter_Valid_Last_Name), TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                return
            }
            if (!StaticRefs.isValidPassword(lsPassword)) {
                etPassword.setError(getString(R.string.Enter_Valid_Password))
                lbProceedAhead = false
                TastyToast.makeText(context, getString(R.string.Enter_Valid_Password), TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                return
            }

            if (!lsPassword.equals(lsConfPassword)) {
                etConfPassword.setError(getString(R.string.Password_Should_Match))
                lbProceedAhead = false
                TastyToast.makeText(context, getString(R.string.Password_Should_Match), TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                return
            }

        }


        if (!(lbProceedAhead) && lsMessage.length > 0) {
            if (lsMessage.length > 50) {
                TastyToast.makeText(context, getString(R.string.Please_Enter_All_Details), TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            } else {
                TastyToast.makeText(context, getString(R.string.Please_Enter_Valid) + lsMessage, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            }
            return
        }
        else if(service_id==0){
            TastyToast.makeText(context, getString(R.string.Invalid_Service), TastyToast.LENGTH_LONG, TastyToast.ERROR).show()

        }
        else {
            registration()
        }


    }

    fun genMessage(msg: String) {
        if (lsMessage.length > 0) {
            lsMessage = lsMessage + ", " + msg;
        } else {
            lsMessage = lsMessage + " " + msg;
        }
    }

    fun registration() {
        lsFName = etFirstName.text.toString()
        lsLName = etLastName.text.toString()
        lsFullname = lsFName + " " + lsLName
        lsEmail = etEmail.text.toString()
        lsMobile = etMobileNumber.text.toString()
        lsPrimary = etPrimaryBusiness.text.toString()
        lsPassword = etPassword.text.toString()
        lsConfPassword = etConfPassword.text.toString()
        val referralCode=etReferralCode.text.toString()
        if(!(referralCode.equals(null)||referralCode.equals("null")||referralCode.equals(""))){
            lsReferralCode=referralCode
        }
        else{
            lsReferralCode="Null"
        }


        var params: Map<String, String>

        Fuel.post(StaticRefs.REGISTERURL, listOf(StaticRefs.FIRSTNAME to lsFName,
                (StaticRefs.LASTNAME to lsLName), (StaticRefs.EMAIL to lsEmail),
                (StaticRefs.VENDORMOBILENO to lsMobile), (StaticRefs.VENDORPRIMARYBUSINESS to lsPrimary),
                (StaticRefs.REFERBY to lsReferralCode),(StaticRefs.VENDORDEVICEID to prefs.devicetoken ),
                (StaticRefs.PASSWORD to lsPassword), (StaticRefs.VENDORCREATEDBY to lsFullname)))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->
                        //do something with data
                        pd.show()
                        parseResponse(result.get().content)

                    }, { err ->
                        pd.dismiss()
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        //do something with error
                    })


                }


    }

    fun parseResponse(response: String) {

        val json = JSONObject(response)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {

                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {

                val error = json.getString("errors")

                TastyToast.makeText(this, error, Toast.LENGTH_LONG, TastyToast.ERROR).show()
                pd.dismiss()

            } else {
                val data = JSONObject(response).getJSONObject(StaticRefs.DATA)
                if (!(data.equals(null) || data.equals("null"))) {

                    prefs.logout()

                    prefs.serviceid = service_id.toString()
                    prefs.vendorid = data.getString(StaticRefs.VENDORID)
                    prefs.token = data.getString(StaticRefs.TOKEN)
                    prefs.fullname = lsFName + " " + lsLName
                    prefs.mobile_no = lsMobile
                    prefs.pri_business = lsPrimary

                  getProfileStatus()
                    TastyToast.makeText(context, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()

                }
            }
        }
    }

    fun getProfileStatus() {
        Fuel.post(StaticRefs.VENDORPROFILE, listOf((StaticRefs.VENDORID to prefs.vendorid), StaticRefs.TOKEN to prefs.token))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->

                        saveProfilestatus(result.get().content)
                    }, { err ->
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        //do something with error
                    })
                    // saveProfilestatus(result.get().content)


                }

    }

    fun saveProfilestatus(response: String) {

        var jsonob: JSONObject? = null
        // data = JSONObject(response).getJSONObject("data")
        val json = JSONObject(response)

        if (response.contains(StaticRefs.PROFILESTATUS) && json.getString(StaticRefs.PROFILESTATUS) != null) {

            val profilest = json.getString(StaticRefs.PROFILESTATUS)

            profilestatus = json.getString(StaticRefs.PROFILESTATUS)

            prefs.profilestatus = profilestatus
            if (response.contains(StaticRefs.DATA) && json.getString(StaticRefs.DATA) != null && !json.getString(StaticRefs.DATA).equals("")) {


                val data1 = json.getString(StaticRefs.DATA)
                val jsondata = JSONObject(data1)
                if (jsondata.length() > 0) {


                    val data = json.getJSONObject(StaticRefs.DATA)

                    if (profilestatus!!.contains(StaticRefs.REJECTED) && data.has(StaticRefs.PROFILEREJECTIONREASON)) {
                        pd.dismiss()
                        rejectionreason = data.getString(StaticRefs.PROFILEREJECTIONREASON)
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else if (profilestatus.equals(StaticRefs.INCOMPLETE)) {
                        personalstatus = data.getString(StaticRefs.PERSONALINFOSTATUS)
                        businesstatus = data.getString(StaticRefs.BUSINESSINFOSTATUS)
                        kycstatus = data.getString(StaticRefs.KYCINFOSTATUS)
                        workstatus = data.getString(StaticRefs.WORKIMAGESINFOSTATUS)
                        refstatus = data.getString(StaticRefs.REFERENCESINFOSTATUS)
                        pricetatus = data.getString(StaticRefs.PRICINGINFOSTATUS)
                        prefs.personalinfostatus = personalstatus
                        prefs.businessinfostatus = businesstatus
                        prefs.kycinfostatus = kycstatus
                        prefs.workinfostatus = workstatus
                        prefs.referenceinfostatus = refstatus
                        prefs.pricinginfostatus = pricetatus
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                        finish()

                    }



                }
            }

            if (profilestatus.equals(StaticRefs.WAITINGFORAPPROVAL)){
                pd.dismiss()
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish()

            }
            else if(profilestatus.equals(StaticRefs.APPROVED)){
                pd.dismiss()
                val intent = Intent(this, ApprovedVendorHome::class.java)
                startActivity(intent)
                finish()

            }

        } else {
            TastyToast.makeText(context, getString(R.string.No_Status), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show()

        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}

