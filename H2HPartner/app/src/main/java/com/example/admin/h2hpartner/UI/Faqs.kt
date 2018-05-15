package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.admin.h2hpartner.Adapter.FaqsAdapter
import com.example.admin.h2hpartner.Adapter.NewLeads_Adapter
import com.example.admin.h2hpartner.BaseActivity
import com.example.admin.h2hpartner.Models.FaqsModel
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.base_layout.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by admin on 06-04-2018.
 */
class Faqs : BaseActivity() {
    override lateinit var context: Context
    override lateinit var activity: Activity
    lateinit var pd: TransperantProgressDialog
    lateinit var llm: LinearLayoutManager
    lateinit var rcvAdapter: RecyclerView.Adapter<FaqsAdapter.ViewHolder>
    var adapter: FaqsAdapter? = null
    var rcvfaqs: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.faq)
        setTitle(getString(R.string.FAQS))
        context = this
        activity = this
        initlayout()
    }

    fun initlayout() {

        ivNotifications.visibility = View.INVISIBLE
        pd = TransperantProgressDialog(context)
        rcvfaqs = findViewById(R.id.rcvFAQ)
        llm = LinearLayoutManager(this);
        rcvfaqs!!.layoutManager = llm;
        getServiceFaqs()
    }

    fun getServiceFaqs() {
        pd.show()
        Fuel.post(StaticRefs.FAQSSHOW, listOf((StaticRefs.TOKEN to prefs.token), (StaticRefs.FAQSERV_ID to prefs.serviceid)))
                .responseJson()
                { request,
                  response,
                  result ->
                    parseFAQS1(result.get().content)
                }
    }

    fun parseFAQS1(response: String) {
        val json = JSONObject(response)
        if (response.contains(StaticRefs.DATA)) {

            val data = json.getString(StaticRefs.DATA)

            val jsondata = JSONArray(data)

            if (jsondata.length()>0){

                val parser = Parser()
                val stringBuilder = StringBuilder(data)
                val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
                val faqModel = model.map { FaqsModel(it) }.filterNotNull();
                rcvfaqs!!.visibility = View.VISIBLE
                adapter = FaqsAdapter(llm, faqModel)
                rcvAdapter = adapter!!
                rcvfaqs!!.adapter = rcvAdapter;
                rcvAdapter.notifyDataSetChanged();
                pd.dismiss()
            }
            else{
                pd.dismiss()
                TastyToast.makeText(context,getString(R.string.No_FAQ_Present),Toast.LENGTH_SHORT,TastyToast.INFO).show()
            }
            }


        }


    }