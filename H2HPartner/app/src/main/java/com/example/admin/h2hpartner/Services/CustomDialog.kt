package com.example.admin.h2hpartner.Services


import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView

import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.CustomServices


class CustomDialog(internal var activity: Activity, internal var context: Context) : PopupWindow(), View.OnClickListener {
    internal var popW: PopupWindow? = null
    internal var popUp: View? = null
    private var mClickListener: DialogButtonClick? = null
    internal var cbOK: Button? = null
    lateinit var cbCancel: Button
    lateinit var tvTitle: TextView
    lateinit var tvMessage: TextView
    internal var lsTitle = ""
    internal var lsMessage = ""
    lateinit var blankView: View
    lateinit var blankViewOK: View
    internal var lbCancelVisible: Boolean? = null

    fun showDialog() {
        popUp = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null)


        tvMessage = popUp!!.findViewById<TextView>(R.id.tvMessage)
        tvTitle = popUp!!.findViewById<TextView>(R.id.tvTitle)

        tvTitle.text = lsTitle
        tvMessage.text = lsMessage

        cbCancel = popUp!!.findViewById<Button>(R.id.cbCancel)
        cbOK = popUp!!.findViewById<Button>(R.id.cbOK)
        blankView = popUp!!.findViewById<View>(R.id.blankView)
        blankViewOK = popUp!!.findViewById<View>(R.id.blankViewOK)

        cbOK!!.setOnClickListener(this)

        cbCancel.setOnClickListener(this)


        popW = PopupWindow(popUp, 0, 0, true)
        val b = CustomServices.getCoordinates(context, .80, .30)
        popW!!.width = b.getInt("WIDTH")
        popW!!.height = b.getInt("HEIGHT")
        popW!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popW!!.isOutsideTouchable = false

        popW!!.showAtLocation(activity.window.decorView, Gravity.CENTER, 0, 0)

        /*popUp!!.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slide_down))*/
    }


    fun setDialogButtonClickListener(listener: DialogButtonClick) {
        mClickListener = listener
    }

    interface DialogButtonClick {

        fun DialogButtonClicked(view: View)
    }

    override fun onClick(v: View) {
        mClickListener!!.DialogButtonClicked(v)
        if (popW != null) {
            popW!!.dismiss()

        } else {

            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(popUp)
            popUp = null
        }
    }


    fun setTitle(lsTitle: String) {
        this.lsTitle = lsTitle
    }

    fun setMessage(lsMessage: String) {
        this.lsMessage = lsMessage
    }

    fun setCancel(lbEnableCancel: Boolean) {
        lbCancelVisible = lbEnableCancel
    }

}