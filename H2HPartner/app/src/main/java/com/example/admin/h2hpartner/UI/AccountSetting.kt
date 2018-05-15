package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import com.example.admin.h2hpartner.BaseActivity
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.account_setting.*
import kotlinx.android.synthetic.main.base_layout.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.system.exitProcess

class AccountSetting : BaseActivity() {

    override lateinit var context: Context
    override lateinit var activity: Activity
    lateinit var language: String
    var text: String = ""
    lateinit var pd: TransperantProgressDialog
    var languageToLoad ="en"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_setting)
        setTitle(getString(R.string.Account_Settings))
        hideFooter(true)
        context = this
        activity = this

        pd = TransperantProgressDialog(context)
        ivNotifications.visibility = View.INVISIBLE


        getData()

        ivbackPressed.setOnClickListener {

            if (rbEnglish.isChecked) {
                text = "EN"
                languageToLoad = "en"

            } else if (rbHindi.isChecked) {
                text = "HI"
                languageToLoad ="hi"

            } else if (rbMarathi.isChecked) {
                text = "MA"
                languageToLoad ="mr"
            }
            if (language.equals(text)) {
                TastyToast.makeText(context, " Default language is same as default", TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
            }
            else
            {
                saveData()
            }
        }

    }


    override fun onBackPressed() {


        val popup1 = android.support.v7.app.AlertDialog.Builder(this)
        popup1.setTitle(R.string.Language_setting_title)
                .setMessage(R.string.Language_setting_message)
                .setPositiveButton(getString(R.string.YES), DialogInterface.OnClickListener {

                    dialog, which ->
                    // finish()
                    if (rbEnglish.isChecked) {
                        text = "EN"
                        languageToLoad = "en"

                    } else if (rbHindi.isChecked) {
                        text = "HI"
                        languageToLoad ="hi"

                    } else if (rbMarathi.isChecked) {
                        text = "MA"
                        languageToLoad ="mr"
                    }
                    if (language.equals(text)) {
                        TastyToast.makeText(context, getString(R.string.Default_Language_is_same), TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    }
                    else
                    {

                        saveData()
                    }


                })
               // .setNegativeButton("No", null)
                .show()




    }

    fun saveData() {
        if (rbEnglish.isChecked) {
            text = "EN"
            languageToLoad = "en"

        } else if (rbHindi.isChecked) {
            text = "HI"
            languageToLoad ="hi"

        } else if (rbMarathi.isChecked) {
            text = "MA"
            languageToLoad ="mr"
        }

       // TastyToast.makeText(context, text, TastyToast.LENGTH_LONG, TastyToast.SUCCESS)


            Fuel.post(StaticRefs.VENDOREDIT, listOf(StaticRefs.VENDORID to prefs.vendorid, (StaticRefs.VENDORUPDATEDBY to prefs.mobile_no),
                    (StaticRefs.LANGUAGEPREFERENCE to text)))
                    .responseJson() { request,
                                      response,
                                      result ->
                        result.fold({ d ->
                            parseJsonSave(result.get().content)
                        }, { err ->
                            TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        })
                    }

      /*  val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)*/
    }

    fun parseJsonSave(response: String) {

        val json = JSONObject(response)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {
                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                TastyToast.makeText(this, message, Toast.LENGTH_LONG, TastyToast.ERROR).show()
            } else if (json.getString(StaticRefs.STATUS).equals(StaticRefs.SUCCESS)) {

                prefs.language = text
                val locale = Locale(prefs.language)
                Locale.setDefault(locale)
                val config = Configuration()
                config.locale = locale
                resources.updateConfiguration(config, resources.displayMetrics)
                finishAffinity ();
             // System.exit(0)
                Runtime.getRuntime().exit(0)
               /* System.runFinalizersOnExit(true)
                android.os.Process.killProcess(android.os.Process.myPid());*/
              /*  val i = baseContext.packageManager
                        .getLaunchIntentForPackage(baseContext.packageName)
                i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                startActivity(i)

               *//* val intent = Intent(context,Splash::class.java)
                startActivity(intent)
                finish()
                TastyToast.makeText(context, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()*/

                        //.setNegativeButton("No", null)

            }

        }
    }
     fun show(){

     }

    fun showdata(language: String) {

        if (language.equals("EN")) rbEnglish.isChecked = true
        else if (language.equals("HI")) rbHindi.isChecked = true
        else if (language.equals("MA")) rbMarathi.isChecked = true
        else if (language.equals("")) rbEnglish.isChecked = true

    }

    fun getData() {
        pd.show()
        Fuel.post(StaticRefs.VENDORDETAILSURL, listOf((StaticRefs.VENDORID to prefs.vendorid), StaticRefs.TOKEN to prefs.token))
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->
                        //do something with data

                        parseProfile(result.get().content)

                    }, { err ->
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        //do something with error
                    })
                }
    }

    fun parseProfile(response: String) {
        pd.dismiss()
        var jsonob: JSONObject? = null

        if (response.contains(StaticRefs.DATA) && !StaticRefs.USERNAME.equals(null)) {
            val json = JSONObject(response)
            val data = json.getJSONObject(StaticRefs.DATA)
            language = data.getString(StaticRefs.LANGUAGEPREFERENCE)

            showdata(language)

        } else {
            pd.dismiss()
            TastyToast.makeText(context, getString(R.string.No_Data_Found), TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
        }
    }

}



