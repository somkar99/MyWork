package com.example.admin.h2hpartner.UI

import android.accounts.AccountAuthenticatorResponse
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.admin.h2hpartner.Adapter.NewLeads_Adapter
import com.example.admin.h2hpartner.Adapter.ReviewAdpater
import com.example.admin.h2hpartner.BaseActivity
import com.example.admin.h2hpartner.Model.NewLeads_Model
import com.example.admin.h2hpartner.Models.ReviewModel
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Admin on 12-02-18.
 */

class CustomerFeedback : BaseActivity() {

    override lateinit var context: Context
    override lateinit var activity: Activity
    lateinit var pd: TransperantProgressDialog
    lateinit var llm: LinearLayoutManager
    lateinit var rcvAdapter: RecyclerView.Adapter<ReviewAdpater.ViewHolder>
    var adapter: ReviewAdpater? = null
    var rcvMyCases: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customerfeedback)
        setTitle(getString(R.string.Reviews))
        hideFooter(true)

        pd = TransperantProgressDialog(context)


        context = this
        activity = this

        initlayout()
    }

    fun initlayout() {
        rcvMyCases = findViewById(R.id.rcvFeedback)
        llm = LinearLayoutManager(this);
        rcvMyCases!!.layoutManager = llm;
        showData()


    }

    fun showData() {

        pd.show()
        Fuel.post(StaticRefs.FEEDBACK, listOf((StaticRefs.TRANSACTIONVENDORID to prefs.vendorid), (StaticRefs.TOKEN to prefs.token)))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->
                        parseJson(result.get().content)

                    }, { err ->
                        pd.dismiss()
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })
                }
    }

    fun parseJson(response: String){
        pd.dismiss()

        val json = JSONObject(response)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var lsmessage = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {

                lsmessage = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                pd.dismiss()
                val error=json.getString("error")
                TastyToast.makeText(this, error, TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
            } else {
                if (response.contains(StaticRefs.DATA)) {

                    val lsreviewdata = json.getString(StaticRefs.DATA)
                    if(!(lsreviewdata.equals(null) ||lsreviewdata.equals("null"))) {
                        val jsondata = JSONArray(lsreviewdata)
                        if (jsondata.length() > 0) {


                            val response = json.getString(StaticRefs.DATA)
                            val parser = Parser()
                            val stringBuilder = StringBuilder(response)
                            val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
                            val reviewModel = model.map { ReviewModel(it) }.filterNotNull();

                            rcvMyCases!!.visibility = View.VISIBLE
                            adapter = ReviewAdpater(reviewModel);
                            rcvAdapter = adapter!!
                            rcvMyCases!!.adapter = rcvAdapter;
                            rcvAdapter.notifyDataSetChanged();



                        } else {
                            /* rcvAdapter = adapter!!
                         rcvMyCases.adapter = rcvAdapter;
                         rcvAdapter.notifyDataSetChanged();*/
                            rcvMyCases!!.visibility = View.GONE

                            TastyToast.makeText(context, getString(R.string.No_Reviews), TastyToast.LENGTH_SHORT, TastyToast.INFO).show()

                        }
                    }
                    else{
                        rcvMyCases!!.visibility = View.GONE

                        TastyToast.makeText(context, getString(R.string.No_Reviews), TastyToast.LENGTH_SHORT, TastyToast.INFO).show()

                    }
                }
            }
        }
    }

}