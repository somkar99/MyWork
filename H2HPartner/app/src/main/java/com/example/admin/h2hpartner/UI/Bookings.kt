package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.example.admin.h2hpartner.R

/**
 * Created by apple on 20/02/18.
 */

class Bookings : Activity() {
    lateinit var activity: Activity
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.booking)
        setTitle("Bookings")
    }
}