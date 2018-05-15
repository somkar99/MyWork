package com.example.admin.h2hpartner.UI


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
import com.example.admin.h2hpartner.BaseActivity
import com.example.admin.h2hpartner.Model.NewLeads_Model
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.base_layout.*
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.new_leads.*

import kotlinx.android.synthetic.main.new_leads_row_1.*
import org.json.JSONObject

class NewLeads : BaseActivity() {

    override lateinit var context: Context
    override lateinit var activity: Activity
    lateinit var pd: TransperantProgressDialog
    lateinit var llm: LinearLayoutManager
    lateinit var rcvAdapter: RecyclerView.Adapter<NewLeads_Adapter.ViewHolder>
    var adapter: NewLeads_Adapter? = null
    var Key = ""

    companion object {
        var OPEN = "O"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_leads)
        setTitle(getString(R.string.New_Leads))

        ivNotifications.visibility = View.INVISIBLE

        context = this
        activity = this

        initlayout()
    }

    override fun onResume() {
        super.onResume()
        showData()

    }

    fun initlayout() {
        pd = TransperantProgressDialog(context)

        ivLeads.isEnabled = false
        tvLeads.isEnabled = false

        llm = LinearLayoutManager(this);
        rcvNewLeads.layoutManager = llm;

    }

    fun showData() {
        pd.show()

        Fuel.post(StaticRefs.NEWLEADS, listOf((StaticRefs.LEAD_SERVICEPROVIDERID to prefs.vendorid), (StaticRefs.LEADSTATUS to OPEN)))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->
                        parseJson(result.get().content)
                        pd.dismiss()
                    }, { err ->
                        pd.dismiss()
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })
                }

    }

    fun parseJson(data: String) {
        val json = JSONObject(data)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {

                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                pd.dismiss()
                TastyToast.makeText(this, getString(R.string.Parameters_Incorrect), TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
            } else {
                if (data.contains(StaticRefs.DATA)) {

                    val newleadsdata = json.getString(StaticRefs.DATA)
                    if (!(newleadsdata.equals(null) || newleadsdata.equals("null"))) {
                        val response = json.getString(StaticRefs.DATA)
                        val parser = Parser()
                        val stringBuilder = StringBuilder(response)
                        val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
                        val newleads_model = model.map { NewLeads_Model(it) }.filterNotNull();

                        adapter = NewLeads_Adapter(newleads_model);
                        rcvAdapter = adapter!!
                        rcvNewLeads.adapter = rcvAdapter;
                        rcvAdapter.notifyDataSetChanged();


                        adapter!!.setOnCardClickListener(object : NewLeads_Adapter.onItemClickListener {
                            override fun onItemClick(position: Int, view: View) {
                                if (view.id == ivCustomerCall.id) {
                                    val enableMobile = newleads_model.get(position).sr_sharemobileno
                                    if (prefs.profilestatus.equals(StaticRefs.APPROVED)) {
                                        if (enableMobile.equals("Y")) {
                                            var mobileno: String? = null
                                            mobileno = newleads_model.get(position).lsMobile
                                            if (mobileno != null && mobileno != "") {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobileno))
                                                startActivity(intent)
                                            } else {
                                                TastyToast.makeText(context, getString(R.string.Mobile_No_Not_Shared), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                                            }

                                        } else {
                                            TastyToast.makeText(context, getString(R.string.Customer_Has_Disabled_This_Option), TastyToast.LENGTH_LONG, TastyToast.WARNING)
                                        }
                                    } else {
                                        TastyToast.makeText(context, getString(R.string.Profile_Not_Completed_Waiting_Approval), TastyToast.LENGTH_LONG, TastyToast.WARNING)
                                    }

                                } else if (view.id == ivCustomerMessage.id) {

                                    val enableMessage = newleads_model.get(position).sr_enablemessaging
                                    if (prefs.profilestatus.equals(StaticRefs.APPROVED)) {

                                        if (enableMessage.equals("Y")) {
                                            var transactionid = newleads_model.get(position).txn_id
                                            var cust_id = newleads_model.get(position).cust_id
                                            val i = Intent(context, Message::class.java)
                                            i.putExtra("key", "Leads")
                                            i.putExtra(StaticRefs.CUST_ID, cust_id)
                                            i.putExtra(StaticRefs.CUSTOMERFIRSTNAME, newleads_model.get(position).lscustomerfirstname)
                                            i.putExtra(StaticRefs.TRANSACTIONID, transactionid.toString())

                                            startActivity(i)
                                        } else {
                                            TastyToast.makeText(context, getString(R.string.Customer_Has_Disabled_This_Option), TastyToast.LENGTH_LONG, TastyToast.WARNING)
                                        }
                                    } else {
                                        TastyToast.makeText(context, getString(R.string.Profile_Not_Completed_Waiting_Approval), TastyToast.LENGTH_LONG, TastyToast.WARNING)
                                    }

/* var mobileno: String? = null
mobileno = newleads_model.get(position).lsMobile

val sendIntent = Intent()
sendIntent.action = Intent.ACTION_SEND
sendIntent.putExtra(Intent.EXTRA_TEXT, "hi")
sendIntent.putExtra("jid", mobileno + "@s.whatsapp.net");
sendIntent.type = "text/plain"
sendIntent.`package` = "com.whatsapp"
if (sendIntent.resolveActivity(packageManager) != null) {
    startActivity(sendIntent)
}*/


                                } else if (view.id == llLeadsDetails.id) {
                                    var transactionid = newleads_model.get(position).txn_id
                                    var cust_id = newleads_model.get(position).cust_id
                                    val i = Intent(context, NewLeads_Detail::class.java)
                                    i.putExtra("key", "Leads")
                                    i.putExtra(StaticRefs.CUST_ID, cust_id)
                                    i.putExtra(StaticRefs.TRANSACTIONID, newleads_model.get(position).txn_id)
                                    i.putExtra("call", newleads_model.get(position).sr_sharemobileno)
                                    i.putExtra("message", newleads_model.get(position).sr_enablemessaging)

                                    startActivity(i)
                                }

                            }
                        })

                    } else {
                        rcvNewLeads.visibility = View.GONE
                        TastyToast.makeText(context, getString(R.string.No_New_Leads), TastyToast.LENGTH_SHORT, TastyToast.INFO).show()
                    }
                }
            }
        }
    }
}





