package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.admin.h2hpartner.*
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.home.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import kotlin.math.round

class Home : BaseActivity() {

    override lateinit var context: Context
    override lateinit var activity: Activity
    lateinit var aladdress: ArrayList<String>
    lateinit var data: JSONObject
    var addjson: JSONArray? = null
    var profilestatus: String? = null
    var statusjson: JSONArray? = null
    var kycstatus: String? = null
    var workstatus: String? = null
    var refstatus: String? = null
    var personalstatus: String? = null
    var businesstatus: String? = null
    var pricetatus: String? = null
    var rejectionreason: String? = null
    var lsKey: String? = null
    var lsMessage = ""
    var oncreateflag: Boolean = false
    var approved: Boolean = false
    var rejected: Boolean = false
    lateinit var pd: TransperantProgressDialog
    var firsttime: Boolean = false
     var profilecomplition:Int=0
    var PROFILECOMPLETIONPERCENTAGE:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        activity = this
        context = this
        pd = TransperantProgressDialog(context)





    }

    override fun onResume() {
        super.onResume()
        profilecomplition=0
        getData()
        initlayout()
        if(firsttime==false){
            pd.show()
            firsttime=true
        }
        tvWelcomeMessage.setText(prefs.fullname)
        tvPrimarybusiness.setText(prefs.pri_business)
        updatecompletionstatus()
        checkStatus()
        if(prefs.profilestatus.equals(StaticRefs.WAITINGFORAPPROVAL)||prefs.profilestatus.equals(StaticRefs.INCOMPLETE)|| prefs.profilestatus.equals(StaticRefs.REJECTED)){
            ivHome.isEnabled=false
            tvHome.isEnabled=false
        }

    }

    fun initlayout() {
        btnSubmit.setOnClickListener {
            lsMessage = ""
            var lbProceedAhead = true;

            if (prefs.profilestatus.equals(StaticRefs.INCOMPLETE)) {

                if (prefs.personalinfostatus.equals(StaticRefs.INCOMPLETE)) {

                    genMessage(getString(R.string.Personal_Information))
                }
                if (prefs.businessinfostatus.equals(StaticRefs.INCOMPLETE)) {

                    genMessage(getString(R.string.Business_Information))
                }
                if (prefs.kycinfostatus.equals(StaticRefs.INCOMPLETE)) {

                    genMessage(getString(R.string.Kyc_Information))
                }
                if (prefs.referenceinfostatus.equals(StaticRefs.INCOMPLETE)) {

                    genMessage(getString(R.string.Reference_Information))
                }
                if (prefs.pricinginfostatus.equals(StaticRefs.INCOMPLETE)) {

                    genMessage(getString(R.string.Pricing_Information))
                }

                if (lsMessage.length > 0) {

                    TastyToast.makeText(context, getString(R.string.Please_Enter) + lsMessage, Toast.LENGTH_LONG, TastyToast.WARNING).show()
                }


                if (prefs.personalinfostatus.equals(StaticRefs.COMPLETE) && prefs.businessinfostatus.equals(StaticRefs.COMPLETE) && prefs.kycinfostatus.equals(StaticRefs.COMPLETE) && prefs.referenceinfostatus.equals(StaticRefs.COMPLETE) && prefs.pricinginfostatus.equals(StaticRefs.COMPLETE)) {

                    updatestatus()
                }
            }


        }

        linearHomePersonal.setOnClickListener {

            val intent = Intent(this, Personalinfo::class.java)
            intent.putExtra("address", addjson.toString())
            startActivity(intent)


        }

        linearHomeBusiness.setOnClickListener {
            val intent = Intent(this, BusinessInfo::class.java)
            startActivity(intent)

        }

        linearKycDetails.setOnClickListener {
            val intent = Intent(this, KycInfo::class.java)
            startActivity(intent)

        }

        linearHomeworkdocuments.setOnClickListener {

            val intent = Intent(this, Uploadworkimages::class.java)
            startActivity(intent)

        }
        linearHomeReferences.setOnClickListener {

            val intent = Intent(this, ReferencesInfo::class.java)
            startActivity(intent)


        }
        linearHomePricing.setOnClickListener {

            val intent = Intent(this, Pricing::class.java)
            startActivity(intent)

        }
    }

    fun getData() {
        Fuel.post(StaticRefs.VENDORDETAILSURL, listOf((StaticRefs.VENDORID to prefs.vendorid), StaticRefs.TOKEN to prefs.token))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                   // parseJson(result.get().content)
                    result.fold({ d ->

                        parseJson(result.get().content)
                    }, { err ->
                        pd.dismiss()
                        /*  val intent = Intent(this, ServerDown::class.java)
                          startActivity(intent)
                          finish()*/
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        //do something with error
                    })


                }
    }

    fun calculatePercentage(completed:Int) {

        val per = round(completed / 0.06)
        PROFILECOMPLETIONPERCENTAGE=per.toInt()
      //  PROFILECOMPLETIONPERCENTAGE=
     /*   val bbc=PROFILECOMPLETIONPERCENTAGE


            val formatter = NumberFormat.getNumberInstance()
            formatter.setMinimumFractionDigits(2)
            formatter.setMaximumFractionDigits(2)
            PROFILECOMPLETIONPERCENTAGE = formatter.format(bbc).toDouble()
*/


    }

    fun parseJson(response: String) {
        var jsonob: JSONObject? = null
        // data = JSONObject(response).getJSONObject("data")
        val json = JSONObject(response)
        if (response.contains(StaticRefs.DATA) && json.getString(StaticRefs.DATA) != null && !json.getString(StaticRefs.DATA).equals("")) {

            val data1 = json.getString(StaticRefs.DATA)
            val jsondata = JSONObject(data1)
            if (jsondata.length() > 0) {


                val data = json.getJSONObject(StaticRefs.DATA)


                val firstname: String = data.getString(StaticRefs.FIRSTNAME)

                val lastname: String = data.getString(StaticRefs.LASTNAME)
                val mobilenumber: String = data.getString(StaticRefs.VENDORMOBILENO)
                val pribusiness: String = data.getString(StaticRefs.VENDORPRIMARYBUSINESS)
                val businessid: String = data.getString(StaticRefs.BUSINESSID)

                prefs.serviceid = businessid
                //  prefs.serviceid="2"
                prefs.fullname = firstname + " " + lastname
                prefs.mobile_no = mobilenumber
                prefs.pri_business = pribusiness
                prefs.username = data.getString(StaticRefs.USERNAME)
                prefs.gender = data.getString(StaticRefs.GENDER)
                prefs.categroy = data.getString(StaticRefs.CATEGORY)
                prefs.alternateno = data.getString(StaticRefs.ALTMOBILENO)
                prefs.landline = data.getString(StaticRefs.LANDLINE)

                System.out.println("fullname" + prefs.fullname)
                // App.prefs?.dashboard(tvHeaderName, tvHeaderNumber, tvHeaderPribusiness)

                tvWelcomeMessage.setText(prefs.fullname)
                tvPrimarybusiness.setText(prefs.pri_business)
                oncreateflag = true

            } else {
                pd.dismiss()
                TastyToast.makeText(context, getString(R.string.No_Data_Found), TastyToast.LENGTH_LONG, TastyToast.WARNING).show()

            }

        } else {
            pd.dismiss()
            TastyToast.makeText(context, getString(R.string.No_Data_Found), TastyToast.LENGTH_LONG, TastyToast.WARNING).show()

        }

    }

    fun genMessage(msg: String) {
        if (lsMessage.length > 0) {
            lsMessage = lsMessage + ", " + msg;
        } else {
            lsMessage = lsMessage + " " + msg;
        }
    }

/*
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
                        TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        //do something with error
                    })
                   // saveProfilestatus(result.get().content)


                }

    }
*/

/*
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
                        rejectionreason = data.getString(StaticRefs.PROFILEREJECTIONREASON)
                        btnSubmit.visibility = View.VISIBLE
                    } else if (profilestatus.equals(StaticRefs.INCOMPLETE)) {
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

                    }

                }
            }


        } else {
            TastyToast.makeText(context, "no Status ", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show()

        }


    }
*/

    fun updatestatus() {
        Fuel.post(StaticRefs.VENDORPROFILEUPDATE, listOf((StaticRefs.VENDORID to prefs.vendorid), StaticRefs.TOKEN to prefs.token))
                .responseJson()
                { request,
                  response,
                  result ->
                    //getStatus(result.get().content)
                    result.fold({ d ->

                        getStatus(result.get().content)
                    }, { err ->
                        pd.dismiss()
                        /*  val intent = Intent(this, ServerDown::class.java)
                          startActivity(intent)
                          finish()*/
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        //do something with error
                    })

                }

    }

    private fun getStatus(response: String) {

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


            } else {
                TastyToast.makeText(context, message, Toast.LENGTH_LONG, TastyToast.WARNING).show()
                btnSubmit.visibility = View.GONE
            }


        }
    }

    override fun onBackPressed() {
        if (lsKey.equals(StaticRefs.YES) && !lsKey.equals(null)) {
           super.onBackPressed()
        }
        else {

            val popup1 = android.support.v7.app.AlertDialog.Builder(this)
            popup1.setTitle(getString(R.string.Closing_Application_Heading))
                    .setMessage(getString(R.string.Are_You_Sure_Want_To_Exit))
                    .setPositiveButton(getString(R.string.YES), DialogInterface.OnClickListener {

                        dialog, which ->
                        // finish()
                        super.onBackPressed()
                    })
                    .setNegativeButton(getString(R.string.NO), null)
                    .show()

        }
    }

    fun updatecompletionstatus() {

            pd.dismiss()

        if (prefs.profilestatus.equals(StaticRefs.WAITINGFORAPPROVAL)||prefs.profilestatus.equals(StaticRefs.APPROVED)) {
            hideFooter(false)
            linearHomePersonal.setBackgroundResource(R.drawable.personaldone)
            linearHomeBusiness.setBackgroundResource(R.drawable.businessdone)
            linearKycDetails.setBackgroundResource(R.drawable.kycdone)
            if (!(prefs.workinfostatus.equals(StaticRefs.INCOMPLETE)|| !prefs.workinfostatus.equals("null"))|| !prefs.workinfostatus.equals("")) {
                linearHomeworkdocuments.setBackgroundResource(R.drawable.workdone)
                profilecomplition=6
            }
            else{
                profilecomplition=5
            }
            linearHomeReferences.setBackgroundResource(R.drawable.referencesdone)
            linearHomePricing.setBackgroundResource(R.drawable.pricingdone)
            calculatePercentage(profilecomplition)
            TastyToast.makeText(context, getString(R.string.Profile_Completion) + PROFILECOMPLETIONPERCENTAGE, Toast.LENGTH_LONG, TastyToast.SUCCESS).show()

            /* if (prefs.profilestatus.equals(StaticRefs.APPROVED)) {
                 if (approved != true) {

                     TastyToast.makeText(context, "Your Profile has been sent for Approval", Toast.LENGTH_LONG, TastyToast.SUCCESS).show()
                     approved = true
                 }

                 btnSubmit.visibility = View.GONE
                 hideFooter(false)


             }*/ if (prefs.profilestatus.contains(StaticRefs.WAITINGFORAPPROVAL)) {
                if (oncreateflag != true) {

                    TastyToast.makeText(context, getString(R.string.Your_Profile_Sent_For_Approval), Toast.LENGTH_LONG, TastyToast.SUCCESS).show()
                    oncreateflag = true
                }

                    btnSubmit.visibility = View.GONE


            }

        } else if (prefs.profilestatus.contains(StaticRefs.REJECTED)) {
            if (rejected != true) {

                TastyToast.makeText(context, getString(R.string.Profile_Rejected_Reason) + rejectionreason, Toast.LENGTH_LONG, TastyToast.SUCCESS).show()
                rejected = true
            }
        } else {

            if (prefs.personalinfostatus.equals(StaticRefs.COMPLETE)) {
                profilecomplition=profilecomplition+1

                linearHomePersonal.setBackgroundResource(R.drawable.personaldone)
            }
            if (prefs.businessinfostatus.equals(StaticRefs.COMPLETE)) {
                profilecomplition=profilecomplition+1
                linearHomeBusiness.setBackgroundResource(R.drawable.businessdone)

            }
            if (prefs.kycinfostatus.equals(StaticRefs.COMPLETE)) {
                profilecomplition=profilecomplition+1
                linearKycDetails.setBackgroundResource(R.drawable.kycdone)

            }
            if (prefs.workinfostatus.equals(StaticRefs.COMPLETE)) {
                profilecomplition=profilecomplition+1
                linearHomeworkdocuments.setBackgroundResource(R.drawable.workdone)

            }
            if (prefs.referenceinfostatus.equals(StaticRefs.COMPLETE)) {
                profilecomplition=profilecomplition+1
                linearHomeReferences.setBackgroundResource(R.drawable.referencesdone)

            }
            if (prefs.pricinginfostatus.equals(StaticRefs.COMPLETE)) {
                profilecomplition=profilecomplition+1
                linearHomePricing.setBackgroundResource(R.drawable.pricingdone)

            }
            calculatePercentage(profilecomplition)
            TastyToast.makeText(context, getString(R.string.Profile_Completion)+ PROFILECOMPLETIONPERCENTAGE, Toast.LENGTH_LONG, TastyToast.SUCCESS).show()

        }


    }

    fun checkStatus() {
        lsKey = getIntent().getStringExtra(StaticRefs.FROMSETTING)

        if (lsKey.equals(StaticRefs.YES) && !lsKey.equals(null)) {
          if(prefs.profilestatus.contains(StaticRefs.APPROVED)){
              tvMessage.setText(getString(R.string.Your_Profile_Is_Approved)+getString(R.string.Profile_Completion)+PROFILECOMPLETIONPERCENTAGE+getString(R.string.Persentage_Sign)).toString()
          }else if(prefs.profilestatus.contains(StaticRefs.WAITINGFORAPPROVAL)){
              tvMessage.setText(getString(R.string.Your_Profile_Sent_For_Approval)+","+getString(R.string.Profile_Completion)+PROFILECOMPLETIONPERCENTAGE+getString(R.string.Persentage_Sign)).toString()

          }

            btnSubmit.visibility = View.GONE

            hideFooter(false)
            updatecompletionstatus()
        }
        else{
            if(prefs.profilestatus.contains(StaticRefs.INCOMPLETE)){
                tvMessage.setText(getString(R.string.Few_Minutes_To_Complete_Profile)+"\n"+getString(R.string.Profile_Completion)+PROFILECOMPLETIONPERCENTAGE+getString(R.string.Persentage_Sign))
            }else if(prefs.profilestatus.contains(StaticRefs.WAITINGFORAPPROVAL)){
                tvMessage.setText(getString(R.string.Your_Profile_Sent_For_Approval)+","+getString(R.string.Profile_Completion)+PROFILECOMPLETIONPERCENTAGE+getString(R.string.Persentage_Sign))

            }

        }

    }


}

