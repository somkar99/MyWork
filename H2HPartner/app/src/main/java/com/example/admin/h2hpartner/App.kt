package com.example.admin.h2hpartner

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.widget.TextView
import com.example.admin.h2hpartner.Services.StaticRefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import org.acra.ACRA
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes
import java.security.Signature


val prefs: Prefs by lazy {
    App.prefs!!
}

@ReportsCrashes(
        mailTo = "somkar99@gmail.com",
        mode = ReportingInteractionMode.DIALOG,
        resToastText = R.string.crash_toast_text,
        resDialogText = R.string.crash_dialog_text,
        resDialogIcon = android.R.drawable.ic_dialog_info,
        resDialogTitle = R.string.crash_dialog_title,
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt,
        resDialogOkToast = R.string.crash_dialog_ok_toast)


class App : Application() {
    companion object {
        var prefs: Prefs? = null
    }


    override fun onCreate() {
        prefs = Prefs(applicationContext)
        FuelManager.instance.basePath = StaticRefs.BASEURL
        super.onCreate()
      //  ACRA.init(this)
    }

}


class Prefs(context: Context) {
    val PREFS_FILENAME = "prefs_h2h"
    val PREFS_DEVICEID="prefs_deviceid"


    val TOKEN = StaticRefs.TOKEN

    val VENDORID = StaticRefs.VENDORID
    var FIRSTNAME = StaticRefs.FIRSTNAME
    val LASTNAME = StaticRefs.LASTNAME
    val FULLNAME = StaticRefs.FULLNAME
    val PRIMARY_BUSINESS = StaticRefs.VENDORPRIMARYBUSINESS
    val MOBILE_NO = StaticRefs.VENDORMOBILENO
    val USERNAME = StaticRefs.USERNAME
    val GENDER = StaticRefs.GENDER
    val CATEGORY = StaticRefs.CATEGORY
    val ALTERNATENO = StaticRefs.ALTMOBILENO
    val LANDLINE = StaticRefs.LANDLINE
    val SERVICEID = StaticRefs.SERVICEID
    val PROFILEIMAGE=StaticRefs.VENDORIMAGE
    val LOGINID = StaticRefs.LOGINID
    val PASSWORD = StaticRefs.PASSWORD
    val LANGUAGE = StaticRefs.LANGUAGE
    val FIRSTTIME_FLAG = StaticRefs.FIRSTTIME_FLAG
    val REMEMBER_ME = StaticRefs.REMEMBER_ME
    val PROFILESTATUS = StaticRefs.PROFILESTATUS
    val PERSONALINFOSTATUS = StaticRefs.PERSONALINFOSTATUS
    val BUSINESSINFOSTATUS = StaticRefs.BUSINESSINFOSTATUS
    val KYCINFOSTATUS = StaticRefs.KYCINFOSTATUS
    val WORKINFOSTATUS = StaticRefs.WORKIMAGESINFOSTATUS
    val REFERENCEINFOSTATUS = StaticRefs.REFERENCESINFOSTATUS
    val PRICINGINFOSTATUS = StaticRefs.PRICINGINFOSTATUS
    val REGISTRATIONDATE=StaticRefs.VENDORCREATEDBY
    val DEVICETOKEN=StaticRefs.DEVICETOKEN
    val VENDORREFRRALCODE=StaticRefs.VENDORREFRRALCODE


    val ADDRESSLINE1 = StaticRefs.ADDRESSLINE1
    val ADDRESSLINE2 = StaticRefs.ADDRESSLINE2
    val CITY = StaticRefs.CITY
    val STATE = StaticRefs.STATE
    val PINCODE = StaticRefs.PINCODE


    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);
    val  pref1:SharedPreferences=context.getSharedPreferences(PREFS_DEVICEID,0);

    var token: String
        get() = prefs.getString(TOKEN, "");
        set(value) = prefs.edit().putString(TOKEN, value).apply()


    var vendorid: String
        get() = prefs.getString(VENDORID, "")
        set(value) = prefs.edit().putString(VENDORID, value).apply()

    var first_name: String
        get() = prefs.getString(FIRSTNAME, "")
        set(value) = prefs.edit().putString(FIRSTNAME, value).apply()

    var last_name: String
        get() = prefs.getString(LASTNAME, "")
        set(value) = prefs.edit().putString(LASTNAME, value).apply()

    var fullname: String
        get() = prefs.getString(FULLNAME, "")
        set(value) = prefs.edit().putString(FULLNAME, value).apply()

    var pri_business: String
        get() = prefs.getString(PRIMARY_BUSINESS, "")
        set(value) = prefs.edit().putString(PRIMARY_BUSINESS, value).apply()

    var mobile_no: String
        get() = prefs.getString(MOBILE_NO, "")
        set(value) = prefs.edit().putString(MOBILE_NO, value).apply()

    var username: String
        get() = prefs.getString(USERNAME, "")
        set(value) = prefs.edit().putString(USERNAME, value).apply()

    var gender: String
        get() = prefs.getString(GENDER, "")
        set(value) = prefs.edit().putString(GENDER, value).apply()

    var categroy: String
        get() = prefs.getString(CATEGORY, "")
        set(value) = prefs.edit().putString(CATEGORY, value).apply()

    var alternateno: String
        get() = prefs.getString(ALTERNATENO, "")
        set(value) = prefs.edit().putString(ALTERNATENO, value).apply()

    var landline
        get() = prefs.getString(LANDLINE, "")
        set(value) = prefs.edit().putString(LANDLINE, value).apply()

    var profilestatus
        get() = prefs.getString(PROFILESTATUS, "")
        set(value) = prefs.edit().putString(PROFILESTATUS, value).apply()
    var personalinfostatus
        get() = prefs.getString(PERSONALINFOSTATUS, "")
        set(value) = prefs.edit().putString(PERSONALINFOSTATUS, value).apply()
    var businessinfostatus
        get() = prefs.getString(BUSINESSINFOSTATUS, "")
        set(value) = prefs.edit().putString(BUSINESSINFOSTATUS, value).apply()
    var kycinfostatus
        get() = prefs.getString(KYCINFOSTATUS, "")
        set(value) = prefs.edit().putString(KYCINFOSTATUS, value).apply()
    var workinfostatus
        get() = prefs.getString(WORKINFOSTATUS, "")
        set(value) = prefs.edit().putString(WORKINFOSTATUS, value).apply()
    var referenceinfostatus
        get() = prefs.getString(REFERENCEINFOSTATUS, "")
        set(value) = prefs.edit().putString(REFERENCEINFOSTATUS, value).apply()
    var pricinginfostatus
        get() = prefs.getString(PRICINGINFOSTATUS, "")
        set(value) = prefs.edit().putString(PRICINGINFOSTATUS, value).apply()
    var serviceid
        get() = prefs.getString(SERVICEID, "")
        set(value) = prefs.edit().putString(SERVICEID, value).apply()
    var profileimage
        get() = prefs.getString(PROFILEIMAGE, "")
        set(value) = prefs.edit().putString(PROFILEIMAGE, value).apply()
    var registrationdate
        get() = prefs.getString(REGISTRATIONDATE, "")
        set(value) = prefs.edit().putString(REGISTRATIONDATE, value).apply()
    var devicetoken
        get() = pref1.getString(DEVICETOKEN, "")
        set(value) = pref1.edit().putString(DEVICETOKEN, value).apply()
    var referralcode
        get() = prefs.getString(VENDORREFRRALCODE, "")
        set(value) = prefs.edit().putString(VENDORREFRRALCODE, value).apply()




    fun clearDashboard() {

        prefs.edit().remove(FULLNAME).commit()
        prefs.edit().remove(PRIMARY_BUSINESS).commit()
        prefs.edit().remove(MOBILE_NO).commit()

    }


    fun dashboard(tvname: TextView, tvmobileno: TextView, tvprimarybusiness: TextView) {
        tvname.setText(fullname)
        tvmobileno.setText(mobile_no)
        tvprimarybusiness.setText(pri_business)
    }

    var firsttime_flag: String
        get() = prefs.getString(FIRSTTIME_FLAG, "");
        set(value) = prefs.edit().putString(FIRSTTIME_FLAG, value).apply()

    var remember_me: Boolean
        get() = prefs.getBoolean(REMEMBER_ME, false);
        set(value) = prefs.edit().putBoolean(REMEMBER_ME, value).apply()

    var loginid: String
        get() = prefs.getString(LOGINID, "")
        set(value) = prefs.edit().putString(LOGINID, value).apply()

    var password: String
        get() = prefs.getString(PASSWORD, "")
        set(value) = prefs.edit().putString(PASSWORD, value).apply()

    var language: String
        get() = prefs.getString(LANGUAGE, "")
        set(value) = prefs.edit().putString(LANGUAGE, value).apply()

    fun logout() {


        prefs.edit().clear().commit()
    }


}
