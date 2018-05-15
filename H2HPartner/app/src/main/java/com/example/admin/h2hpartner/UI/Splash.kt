package com.example.admin.h2hpartner.UI


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.RegisterForPushNotificationsAsync
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.UI.Home
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import me.pushy.sdk.Pushy
import org.json.JSONObject
import java.util.*

class Splash : Activity() {
    companion object {
        var TAG: String? = "Splash"
    }

    lateinit var context: Context
    lateinit var activity: Activity
    var flag: String? = "false" // To check weather App Runs first time
    var rememberme: Boolean? = false
    lateinit var llGetStarted: LinearLayout
    var languageToLoad ="en"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Pushy.listen(this)

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request both READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE so that the
            // Pushy SDK will be able to persist the device token in the external storage
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        languageToLoad = prefs.language
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
        setContentView(R.layout.splash)
        context = this
        activity = this

        val registerPushy = RegisterForPushNotificationsAsync()
        registerPushy.setContext(context)
        registerPushy.execute()
        Pushy.listen(this)




        val handler: Handler? = Handler()

        val ivLogoText = findViewById<ImageView>(R.id.ivLogoText)
        val ivLogoSlogan = findViewById<TextView>(R.id.ivLogoSlogan)
        val tvGetStarted = findViewById<TextView>(R.id.tvGetStarted)
        llGetStarted = findViewById<LinearLayout>(R.id.llGetStarted)
        val an = AnimationUtils.loadAnimation(baseContext, R.anim.left_to_right)
        val an2 = AnimationUtils.loadAnimation(baseContext, R.anim.left_to_right)
        llGetStarted.visibility = View.INVISIBLE

        //  flag = prefs.firsttime_flag
        rememberme = prefs.remember_me

        ivLogoSlogan.visibility = View.GONE

        ivLogoText.startAnimation(an)
        an.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {

                handler!!.postDelayed(Runnable {
                    ivLogoSlogan.visibility = View.VISIBLE
                    ivLogoSlogan.startAnimation(an2)
                }, 500)

                an2.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationEnd(animation: Animation?) {
                        handler!!.postDelayed(Runnable {

                            llGetStarted.visibility = View.VISIBLE
                            llGetStarted.setOnClickListener() {
                                prefs.firsttime_flag = "true"
                                val intent = Intent(context, SplashExtended::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }, 1000)
                    }

                    override fun onAnimationStart(animation: Animation?) {

                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }
                })
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })




    }

    fun rememberMe() {
        // if (flag.equals("true")) {

        if (rememberme == false) {
            val intent = Intent(context, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            var lsUsername = prefs.loginid
            var lsPassword = prefs.password
            callLogin(lsUsername, lsPassword)
        }

        //   } else {
        llGetStarted.visibility = View.VISIBLE
        llGetStarted.setOnClickListener() {
            prefs.firsttime_flag = "true"
            val intent = Intent(context, SplashExtended::class.java)
            startActivity(intent)
            finish()
        }
        // }
        //finish()


    }

    fun callLogin(userid: String, password: String) {

        var params: Map<String, String>

        Fuel.post(StaticRefs.LoginURL, listOf(StaticRefs.USERID to userid,StaticRefs.VENDORDEVICEID to prefs.devicetoken, (StaticRefs.PASSWORD to password)))
                .responseJson()
                { request,
                  response,
                  result ->
                    parseResponse(result.get().content, userid, password)
                }
    }

    fun parseResponse(response: String, userid: String, password: String) {
        val json = JSONObject(response)
        val data = JSONObject(response).getJSONObject(StaticRefs.DATA)

        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {
            var message = "";
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {
                message = json.getString(StaticRefs.MESSAGE)
                val intent = Intent(context, Login::class.java)
                startActivity(intent)
                finish()

            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            } else {
                prefs.token = data.getString(StaticRefs.TOKEN)
                prefs.vendorid = data.getString(StaticRefs.VENDORID)
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}



