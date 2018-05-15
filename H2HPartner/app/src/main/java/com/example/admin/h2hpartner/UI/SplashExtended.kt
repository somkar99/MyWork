package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.splash_extended.*
import kotlinx.android.synthetic.main.splash_extended0.*
import kotlinx.android.synthetic.main.splash_extended1.*
import kotlinx.android.synthetic.main.splash_extended2.*
import org.json.JSONArray
import org.json.JSONObject

class SplashExtended : Activity() {
    lateinit var context: Context
    lateinit var activity: Activity
    var rememberme: Boolean? = false
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.splash_extended)
        context = this
        activity = this
        rememberme = prefs.remember_me

        pd = TransperantProgressDialog(context)


        run1()
        getstarted1()

        splash1.visibility = View.VISIBLE
    }

    fun getstarted() {


        GetStarted1.setOnClickListener {
            splash1.visibility = View.GONE
            splash2.visibility = View.VISIBLE
            splash3.visibility = View.GONE

        }
        GetStarted2.setOnClickListener {
            splash1.visibility = View.GONE
            splash2.visibility = View.GONE
            splash3.visibility = View.VISIBLE


        }
        GetStarted3.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()

        }
    }


    fun run1() {
        val handler: Handler? = Handler()
        handler!!.postDelayed({

            splash1.visibility = View.VISIBLE
            splash2.visibility = View.GONE
            splash3.visibility = View.GONE

        }, 3000)


        handler!!.postDelayed({

            splash1.visibility = View.GONE
            splash2.visibility = View.VISIBLE
            splash3.visibility = View.GONE

        }, 6000)

        handler!!.postDelayed({

            splash1.visibility = View.GONE
            splash2.visibility = View.GONE
            splash3.visibility = View.VISIBLE

        }, 9000)

    }

    fun getstarted1() {


        GetStarted1.setOnClickListener {
            pd.show()
            rememberMe()

        }
        GetStarted2.setOnClickListener {
            pd.show()
            rememberMe()

        }
        GetStarted3.setOnClickListener {
            pd.show()
            rememberMe()

        }

    }

    fun rememberMe() {

        rememberme = prefs.remember_me
        if (rememberme == false) {
            val intent = Intent(context, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            var lsUsername = prefs.loginid
            var lsPassword = prefs.password
            callLogin(lsUsername, lsPassword)
        }

    }

    fun callLogin(userid: String, password: String) {

        var params: Map<String, String>

        Fuel.post(StaticRefs.LoginURL, listOf(StaticRefs.USERID to userid,StaticRefs.VENDORDEVICEID to prefs.devicetoken, (StaticRefs.PASSWORD to password)))
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->
                        //do something with data

                        parseResponse(result.get().content, userid, password)

                    }, { err ->
                        pd.dismiss()
                        val intent = Intent(this, ServerDown::class.java)
                        startActivity(intent)
                        finish()
                        TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        //do something with error
                    })
                }
    }

    fun parseResponse(response: String, userid: String, password: String) {
        val json = JSONObject(response)

        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {
            var message = "";
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {
                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                pd.dismiss()
                val intent = Intent(context, Login::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            } else {
                val data = JSONObject(response).getJSONObject(StaticRefs.DATA)

                prefs.token = data.getString(StaticRefs.TOKEN)
                prefs.vendorid = data.getString(StaticRefs.VENDORID)
                getProfileStatus()
                pd.dismiss()


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
                        pd.dismiss()
                        TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
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
                  else  if (profilestatus.equals(StaticRefs.WAITINGFORAPPROVAL)){
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
                    else{

                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                        finish()
                    }



                }
            }




        } else {
            TastyToast.makeText(context, "no Status ", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show()

        }




    }
}


