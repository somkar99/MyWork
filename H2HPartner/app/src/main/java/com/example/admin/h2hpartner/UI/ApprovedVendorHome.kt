package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.admin.h2hpartner.Adapter.ReviewAdpater
import com.example.admin.h2hpartner.BaseActivity
import com.example.admin.h2hpartner.Models.ReviewModel
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.home3.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.CacheResponse
import java.text.SimpleDateFormat
import kotlin.math.round

class ApprovedVendorHome : BaseActivity() {
    override lateinit var context: Context
    override lateinit var activity: Activity
    var profile = prefs.profileimage
    var registerDate = prefs.registrationdate
    lateinit var pd: TransperantProgressDialog
    var lsLeadsCompleted: String? = ""
    var lsLeadsAccepted: String? = ""
    var lsLeadsRejected: String? = ""
    var lsAverageRating: String? = ""
    var lsProfileCompleted: String? = ""
    var profilecomplition:Int=0
    var PROFILECOMPLETIONPERCENTAGE:Int=0
    var profilestatus: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home3)

        ivHome.isEnabled=false
        tvHome.isEnabled=false

        context = this
        activity = this
        pd = TransperantProgressDialog(context)

        initlayout()
    }

    override fun onResume() {
        super.onResume()
        initlayout()
    }

    fun initlayout() {

        if (!(prefs.workinfostatus.equals(StaticRefs.INCOMPLETE)|| !prefs.workinfostatus.equals("null"))|| !prefs.workinfostatus.equals("")) {

            profilecomplition=6
        }
        else{
            profilecomplition=5
        }
        calculatePercentage(profilecomplition)
        tvProfileCompleted.setText(PROFILECOMPLETIONPERCENTAGE.toString()+"%")


        if (!(profile.equals(null) || profile.equals("null") || profile.equals("NULL"))) {
            ivprofileimage.loadBase64Image(prefs.profileimage)
        }

        tvProviderServiceName.setText(prefs.pri_business)
        tvName.setText(prefs.fullname)
        tvregisterDate.setText(registerDate)
        getAnalytics()

        llMessage.setOnClickListener {
            val intent = Intent(this, Message_Conv::class.java)
            startActivity(intent)
          // TastyToast.makeText(context," feature will be implemented shortly..",Toast.LENGTH_SHORT,TastyToast.INFO).show()
        }

        llFeedack.setOnClickListener {
            val intent = Intent(this, CustomerFeedback::class.java)
            startActivity(intent)
        }


        llReferrals.setOnClickListener {
            val intent = Intent(this, Referrals::class.java)
            startActivity(intent)
           // TastyToast.makeText(context," feature will be implemented shortly..",Toast.LENGTH_SHORT,TastyToast.INFO).show()

        }
    }
    fun calculatePercentage(completed:Int) {

        val per = round(completed / 0.06)
        PROFILECOMPLETIONPERCENTAGE=per.toInt()
    }
    fun getAnalytics() {
        pd.show()
        Fuel.post(StaticRefs.HOMEANALYTICS, listOf((StaticRefs.LEAD_SERVICEPROVIDERID to prefs.vendorid), (StaticRefs.TOKEN to prefs.token)))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->
                        parseJson(result.get().content)

                    }, { err ->
                        pd.dismiss()
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })
                }
    }

    fun parseJson(response: String) {



        val json = JSONObject(response)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var lsmessage = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {

                lsmessage = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                val error = json.getString("error")
                pd.dismiss()
                TastyToast.makeText(this, error, TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
            } else {

                            lsLeadsAccepted = json.getString(StaticRefs.ACCEPTED)
                            lsLeadsCompleted = json.getString(StaticRefs.COMPLETED)
                            lsLeadsRejected = json.getString(StaticRefs.REJECTED)
                            lsAverageRating=json.getString(StaticRefs.AVERAGERATING)
                            setData()


            }
        }

    }
    fun setData(){
        tvLeadsAcceptedNo.setText(lsLeadsAccepted)
        tvLeadsRejectedNo.setText(lsLeadsRejected)
        tvLeadsCompletedNo.setText(lsLeadsCompleted)
        if(lsAverageRating.equals(null)||lsAverageRating.equals("null")){
            tvAverageRatingNo.setText("0")

        }else {
            tvAverageRatingNo.setText(lsAverageRating)
        }
        pd.dismiss()
    }

    override fun onBackPressed() {
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
