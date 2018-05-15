/*
package com.GoMobeil.H2H.UI

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.GoMobeil.H2H.Adapters.FaqsAdapter
import com.GoMobeil.H2H.Models.FaqsModel
import com.GoMobeil.H2H.R

*/
/**
 * Created by Admin on 24-03-18.
 *//*

class FAQPopup(internal var context: Context,internal var list :List<FaqsModel>,internal var title: String?) : PopupWindow() {
    lateinit var popView: View
    //lateinit var popW: PopupWindow
    public lateinit var ivBack: ImageView

    init {
        setupPopUp()
    }

    fun setupPopUp() {
        //popUp = LayoutInflater.from(context).inflate(R.layout.calci,null);
        popView = LayoutInflater.from(context).inflate(R.layout.faq_popup, null)
        var llm = LinearLayoutManager(context)
        var adapter = FaqsAdapter(llm,list)
        var tvTitle = popView.findViewById<TextView>(R.id.tvTitle)
        ivBack = popView.findViewById<ImageView>(R.id.ivBack)
        tvTitle.text = "FAQ for " +title;
        var rcvList = popView.findViewById<RecyclerView>(R.id.rcvFAQ)
        rcvList.adapter = adapter
        rcvList.layoutManager = llm




    }

}
*/
