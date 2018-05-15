package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.admin.h2hpartner.BaseActivity
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.prefs
import kotlinx.android.synthetic.main.base_layout.*
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.setting.*


class Setting : BaseActivity() {
    override lateinit var context: Context
    override lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)
        setTitle("Setting")

        ivNotifications.visibility = View.INVISIBLE

        initlayout()

        ivHome.isEnabled = false
        tvHome.isEnabled = false

    }

    fun initlayout() {
        tvName.setText(prefs.fullname)
        tvMobile.setText(prefs.mobile_no)
        tvReferralCode.setText(prefs.referralcode)

        llProfile.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra(StaticRefs.FROMSETTING, "yes")
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
            finish()
        }

        llChangePassword.setOnClickListener {
            val intent = Intent(this, ChangePassword::class.java)
            intent.putExtra(StaticRefs.CHANGEPASSWORD, "Change_Password")
            startActivity(intent)
        }

        llSettings.setOnClickListener {
            val intent = Intent(this, AccountSetting::class.java)
            startActivity(intent)
        }
        ivbackPressed.setOnClickListener {
          onBackPressed()

        }
    }

    override fun onBackPressed() {
     super.onBackPressed()
    }
}

