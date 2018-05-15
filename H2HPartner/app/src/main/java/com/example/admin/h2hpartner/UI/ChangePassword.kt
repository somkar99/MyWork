package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.GoMobeil.H2H.Extensions.OnItemClickListener
import com.GoMobeil.H2H.Extensions.addOnItemClickListener
import com.GoMobeil.H2H.Extensions.toast
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.admin.h2hpartner.Adapter.ServiceListAdapter
import com.example.admin.h2hpartner.BaseActivity
import com.example.admin.h2hpartner.Models.ServiceModel
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.changepassword.*
import kotlinx.android.synthetic.main.register.*
import org.json.JSONObject


class ChangePassword : BaseActivity() {

    companion object {
        var TAG: String? = "ChangePassword"
    }

    override lateinit var context: Context
    override lateinit var activity: Activity
    lateinit var lsOldPassword: String
    lateinit var lsNewPassword: String
    lateinit var lsCNewPassword: String
    var lsLogin = prefs.LOGINID;
    var lsKey: String? = null
    var otp: String? = null
    var confirmotp: String? = null
    lateinit var pd: TransperantProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.changepassword)
        setTitle(getString(R.string.Change_Password))
        hideFooter(true)
        context = this
        activity = this

        pd = TransperantProgressDialog(context)
        lsKey = getIntent().getStringExtra(StaticRefs.CHANGEPASSWORD)
        initLayout()

        if (lsKey.equals("forgot_password") && !lsKey.equals(null)) {
            showgetotp()
        } else {
            showchangepassword()
        }


    }

    fun initLayout() {

        cbUpdatePassword.setOnClickListener(View.OnClickListener {
            lsOldPassword = etOldPassword.text.toString()
            lsNewPassword = etNewPassword.text.toString()
            lsCNewPassword = etCNewpassword.text.toString()

            if (lsNewPassword == lsCNewPassword) {
                updatepassword()
            } else {
                TastyToast.makeText(context, getString(R.string.Confirm_Pasword_Not_Equal_To_NewPassword), Toast.LENGTH_LONG, TastyToast.ERROR)
            }


        })

        cbGetotp.setOnClickListener {

            getotp()


        }
        cbFupdatePassword.setOnClickListener {
            //toast("Password has been Changed.... ")

        }
        cbSubmitotp.setOnClickListener {

            checkotp()
        }

    }

    fun updatepassword() {

        Fuel.post(StaticRefs.CHANGEPASSOWRDURL, listOf(StaticRefs.TOKEN to prefs.token,
                (StaticRefs.new_sp_password to lsNewPassword),
                (StaticRefs.old_sp_password to lsOldPassword),
                (StaticRefs.VENDORID to prefs.vendorid))).timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->
                        pd.show()
                        parseData(result.get().content)
                    }, { err ->
                        TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        //do something with error
                    })
                }
    }

    fun parseData(response: String) {

        val json = JSONObject(response)
        // val data = JSONObject(response).getJSONObject(StaticRefs.DATA)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {

                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                pd.dismiss()
                TastyToast.makeText(this, message, Toast.LENGTH_LONG, TastyToast.ERROR).show()
            } else {

                etCNewpassword.setText("")
                etOldPassword.setText("")
                etNewPassword.setText("")
                pd.dismiss()
                val intent = Intent(this, Login::class.java)
                prefs.logout()
                startActivity(intent)
                finish()
                TastyToast.makeText(context, message, Toast.LENGTH_LONG, TastyToast.SUCCESS).show()

            }

        }
    }

    fun getotp() {

        var mobileno = etMobileno.text.toString()
        otp = mobileno.substring(6)
        showenterotp()

    }

    fun checkotp() {
        confirmotp = etotp.text.toString().trim()

        if (otp.equals(confirmotp)) {
            showforgetpassword()
        }
    }


    fun showgetotp() {

        llGetOtp.visibility = View.VISIBLE
        llForgetPassword.visibility = View.GONE
        llChangePassword.visibility = View.GONE
        llOtp.visibility = View.GONE
    }

    fun showenterotp() {
        llGetOtp.visibility = View.GONE
        llOtp.visibility = View.VISIBLE
        llForgetPassword.visibility = View.GONE
        llChangePassword.visibility = View.GONE
    }

    fun showforgetpassword() {
        llGetOtp.visibility = View.GONE
        llOtp.visibility = View.GONE
        llForgetPassword.visibility = View.VISIBLE
        llChangePassword.visibility = View.GONE

    }

    fun showchangepassword() {

        llGetOtp.visibility = View.GONE
        llForgetPassword.visibility = View.GONE
        llChangePassword.visibility = View.VISIBLE
        llOtp.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}