package com.example.admin.h2hpartner.UI

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.RegisterForPushNotificationsAsync
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.login.*
import me.pushy.sdk.Pushy
import org.json.JSONArray
import org.json.JSONObject

class Login : AppCompatActivity() {

    lateinit var activity: Activity;
    lateinit var context: Context
    var lsUserid: String? = null
    var lsPassword: String? = null
    var lsMessage = ""
    lateinit var pd: TransperantProgressDialog
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
    var oncreateflag: Boolean = false
    var approved: Boolean = false
    var rejected: Boolean = false
    var firsttime: Boolean = false
    var devicetoken:String=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      /*  Pushy.listen(this)

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request both READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE so that the
            // Pushy SDK will be able to persist the device token in the external storage
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }*/
        setContentView(R.layout.login)
        activity = this;
        context = this
       /* val registerPushy = RegisterForPushNotificationsAsync()
        registerPushy.setContext(context)
        registerPushy.execute()
        Pushy.listen(this)*/

        pd = TransperantProgressDialog(context)
        devicetoken= prefs.devicetoken

        initlayout()

    }
    fun initlayout(){
        Toast.makeText(context, "Pushy device token at login:"+ devicetoken, Toast.LENGTH_LONG).show()
        cbLogin.setOnClickListener {
            if (amIConnected() == true) {
                callLogin()
            } else {
                TastyToast.makeText(this, getString(R.string.Internet_Not_Available), TastyToast.LENGTH_LONG, TastyToast.ERROR)
            }

        }
        ivChk.setOnClickListener {
            ivUnChk.visibility = View.VISIBLE
            ivChk.visibility = View.GONE
        }
        ivUnChk.setOnClickListener {
            ivUnChk.visibility = View.GONE
            ivChk.visibility = View.VISIBLE

        }

        tvSignup.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
        tvForgotPassword.setOnClickListener {
            /* val intent = Intent(this, ChangePassword::class.java)
             intent.putExtra(StaticRefs.CHANGEPASSWORD, "forgot_password")
             startActivity(intent)
             finish()*/
        }
    }

    private fun amIConnected(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun genMessage(msg: String) {
        if (lsMessage.length > 0) {
            lsMessage = lsMessage + ", " + msg;
        } else {
            lsMessage = lsMessage + " " + msg;
        }
    }

    fun callLogin() {
        lsUserid = etUserId.text.toString()
        lsPassword = etPassword.text.toString()

        var lbProceedAhead = true;
        if (!((lsUserid != null && lsUserid!!.length > 0))) {
            genMessage(getString(R.string.Username))
            lbProceedAhead = false;
        }


        if (!((lsPassword != null && lsPassword!!.length > 0))) {
            genMessage(getString(R.string.Password))
            lbProceedAhead = false;
        }
        if (lbProceedAhead) {

            if (!StaticRefs.isValidPassword(lsPassword!!)) {
                etPassword.setError(getString(R.string.Enter_Valid_Password))
                lbProceedAhead = false
                TastyToast.makeText(context, getString(R.string.Enter_Valid_Password), Toast.LENGTH_LONG, TastyToast.WARNING).show()
            }
        }


        if (!(lbProceedAhead) && lsMessage.length > 0) {
            if (lsMessage.length > 20) {
                TastyToast.makeText(context, getString(R.string.Please_Enter_All_Details), Toast.LENGTH_LONG, TastyToast.WARNING).show()
            } else {
                TastyToast.makeText(context, getString(R.string.Please_Enter_Valid) + lsMessage, Toast.LENGTH_LONG, TastyToast.WARNING).show()
            }

        } else {

            var params: Map<String, String>
            pd.show()
            Fuel.post(StaticRefs.LoginURL, listOf(StaticRefs.USERID to lsUserid,StaticRefs.VENDORDEVICEID to prefs.devicetoken, (StaticRefs.PASSWORD to lsPassword)))
                    .responseJson()
                    { request,
                      response,
                      result ->
                       // parseResponse(result.get().content)
                        result.fold({ d ->

                            parseResponse(result.get().content)
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

    }

    fun parseResponse(response: String) {

     val json=JSONObject(response)

        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {
            var message = "";
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {
                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                 pd.dismiss()
                TastyToast.makeText(this, message, Toast.LENGTH_LONG, TastyToast.WARNING).show();
            } else {
                val data = JSONObject(response).getJSONObject(StaticRefs.DATA)

                if (!(data.equals(null) || data.equals("null"))) {
                    prefs.logout()
                    prefs.token = data.getString(StaticRefs.TOKEN)
                    prefs.vendorid = data.getString(StaticRefs.VENDORID)
                    println("Token is " + data.getString(StaticRefs.TOKEN));
                    println("vendor id is " + data.getString(StaticRefs.VENDORID));
                    TastyToast.makeText(this, message, Toast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                    remember_me()

                    getData()

                }
            }
        }
        else{
            TastyToast.makeText(this, "Login Again", Toast.LENGTH_LONG, TastyToast.WARNING).show();
        }
    }

    fun remember_me() {
        if (ivUnChk.visibility == View.VISIBLE) {
            prefs.remember_me = false
        } else {
            prefs.remember_me = true
            prefs.loginid = lsUserid!!
            prefs.password = lsPassword!!


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
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        //do something with error
                    })


                }
    }

    fun parseJson(response: String) {
        pd.dismiss()
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
                val registrationDate=data.getString(StaticRefs.CREATEDAT)
                val referralCode=data.getString(StaticRefs.VENDORREFRRALCODE)
                prefs.referralcode=referralCode
                prefs.registrationdate=StaticRefs.getDate(registrationDate)

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
                prefs.profileimage=data.getString(StaticRefs.VENDORIMAGE)

                System.out.println("fullname" + prefs.fullname)
                // App.prefs?.dashboard(tvHeaderName, tvHeaderNumber, tvHeaderPribusiness)




            } else {
                TastyToast.makeText(context, getString(R.string.No_Data_Found), TastyToast.LENGTH_LONG, TastyToast.WARNING).show()

            }

        } else {
            TastyToast.makeText(context,  getString(R.string.No_Data_Found), TastyToast.LENGTH_LONG, TastyToast.WARNING).show()

        }
        getProfileStatus()
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
                        pd.dismiss()
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
                       // createddate = data.getString(StaticRefs.PRICINGINFOSTATUS)
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
                    else if (profilestatus.equals(StaticRefs.WAITINGFORAPPROVAL)){

                        pd.dismiss()
                        workstatus = data.getString(StaticRefs.WORKIMAGESINFOSTATUS)
                        prefs.workinfostatus = workstatus
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                        finish()

                    }
                    else if(profilestatus.equals(StaticRefs.APPROVED)){
                        pd.dismiss()
                        workstatus = data.getString(StaticRefs.WORKIMAGESINFOSTATUS)
                        prefs.workinfostatus = workstatus
                        val intent = Intent(this, ApprovedVendorHome::class.java)
                        startActivity(intent)
                        finish()

                    }



                }
            }



        } else
        { pd.dismiss()
            TastyToast.makeText(context, getString(R.string.No_Status), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show()

        }



    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}


