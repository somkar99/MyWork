package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.admin.h2hpartner.R

class ServerDown : AppCompatActivity() {
    companion object {
        var TAG: String? = "Server Down"
    }
    lateinit var context: Context
    lateinit var activity: Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_down)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
