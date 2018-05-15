package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager

import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
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
import kotlinx.android.synthetic.main.mycases.*
import kotlinx.android.synthetic.main.new_leads.*
import kotlinx.android.synthetic.main.new_leads_row_1.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by apple on 04/04/18.
 */


class MyCases : BaseActivity() {
    companion object {
        var HIRED = "H"
        var ACCEPTED = "A"
        var REJECTED = "R"
        var ONGOING = "OG"
        var COMPLETED="C"
    }

    override lateinit var context: Context
    override lateinit var activity: Activity
    lateinit var pd: TransperantProgressDialog
    lateinit var llm: LinearLayoutManager
    lateinit var rcvAdapter: RecyclerView.Adapter<NewLeads_Adapter.ViewHolder>
    var adapter: NewLeads_Adapter? = null
    var rcvMyCases: RecyclerView? = null
    var lscasestatus: String = ONGOING


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mycases)
        setTitle(getString(R.string.My_Cases))
        ivNotifications.visibility=View.GONE
        ivFilter.visibility=View.VISIBLE

        context = this
        activity = this


    }

    override fun onResume() {
        super.onResume()
        initlayout()
        if (lscasestatus.equals(HIRED)) {
           setTitle(getString(R.string.Hired_Cases)).toString()

            /* vOngoing.setBackgroundColor(getResources().getColor(R.color.green))
             vAccecpted.setBackgroundColor(getResources().getColor(R.color.green))
             vRejected.setBackgroundColor(getResources().getColor(R.color.green))
             vHired.setBackgroundColor(getResources().getColor(R.color.white))
             vCompleted.setBackgroundColor(getResources().getColor(R.color.green))*/
            showData(lscasestatus)
        } else if (lscasestatus.equals(ACCEPTED)) {
            setTitle(getString(R.string.Accepted_Cases)).toString()

            /*vOngoing.setBackgroundColor(getResources().getColor(R.color.green))
            vAccecpted.setBackgroundColor(getResources().getColor(R.color.white))
            vRejected.setBackgroundColor(getResources().getColor(R.color.green))
            vHired.setBackgroundColor(getResources().getColor(R.color.green))
            vCompleted.setBackgroundColor(getResources().getColor(R.color.green))*/

            showData(lscasestatus)
        } else if (lscasestatus.equals(REJECTED)) {
            setTitle(getString(R.string.Rejected_Cases)).toString()

            /* vOngoing.setBackgroundColor(getResources().getColor(R.color.green))
             vAccecpted.setBackgroundColor(getResources().getColor(R.color.green))
             vRejected.setBackgroundColor(getResources().getColor(R.color.white))
             vHired.setBackgroundColor(getResources().getColor(R.color.green))
             vCompleted.setBackgroundColor(getResources().getColor(R.color.green))*/
            showData(lscasestatus)

        } else if (lscasestatus.equals(ONGOING)) {
            setTitle(getString(R.string.Ongoing_Cases)).toString()

            /* vOngoing.setBackgroundColor(getResources().getColor(R.color.white))
             vAccecpted.setBackgroundColor(getResources().getColor(R.color.green))
             vRejected.setBackgroundColor(getResources().getColor(R.color.green))
             vHired.setBackgroundColor(getResources().getColor(R.color.green))
             vCompleted.setBackgroundColor(getResources().getColor(R.color.green))*/
            showData(lscasestatus)

        }
        else if (lscasestatus.equals(COMPLETED)) {
            setTitle(getString(R.string.Completed_Cases)).toString()

            /* vCompleted.setBackgroundColor(getResources().getColor(R.color.white))
             vHired.setBackgroundColor(getResources().getColor(R.color.green))
             vAccecpted.setBackgroundColor(getResources().getColor(R.color.green))
             vRejected.setBackgroundColor(getResources().getColor(R.color.green))
             vHired.setBackgroundColor(getResources().getColor(R.color.green))*/
            showData(lscasestatus)

        }

    }

    fun initlayout() {
        pd = TransperantProgressDialog(context)
        rcvMyCases = findViewById(R.id.rcvMyCases)


        ivMyCases.isEnabled = false
        tvMyCases.isEnabled = false

      /*  vOngoing.setOnClickListener(this)
        tvOngoing.setOnClickListener(this)
        tvAccecpted.setOnClickListener(this)
        vAccecpted.setOnClickListener(this)
        tvRejected.setOnClickListener(this)
        vRejected.setOnClickListener(this)
        vHired.setOnClickListener(this)
        tvHired.setOnClickListener(this)
        vCompleted.setOnClickListener(this)
        tvCompleted.setOnClickListener(this)*/
        ivFilter.setOnClickListener (this)

        llm = LinearLayoutManager(this);
        rcvMyCases!!.layoutManager = llm;
    }

    fun showData(input: String) {

        pd.show()
        Fuel.post(StaticRefs.NEWLEADS, listOf((StaticRefs.LEAD_SERVICEPROVIDERID to prefs.vendorid), (StaticRefs.LEADSTATUS to input))).timeoutRead(StaticRefs.TIMEOUTREAD)
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

    fun parseJson(data: String) {
        pd.dismiss()

        val json = JSONObject(data)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var lsmessage = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {

                lsmessage = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                pd.dismiss()
                TastyToast.makeText(this, getString(R.string.Parameters_Incorrect), TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
            } else {
                if (data.contains(StaticRefs.DATA)) {

                    val lsnewleadsdata = json.getString(StaticRefs.DATA)
                    if(!(lsnewleadsdata.equals(null) ||lsnewleadsdata.equals("null"))) {
                        val jsondata = JSONArray(lsnewleadsdata)
                        if (jsondata.length() > 0) {

                            val response = json.getString(StaticRefs.DATA)
                            val parser = Parser()
                            val stringBuilder = StringBuilder(response)
                            val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
                            val newleads_model = model.map { NewLeads_Model(it) }.filterNotNull();

                            rcvMyCases!!.visibility = View.VISIBLE
                            adapter = NewLeads_Adapter(newleads_model);
                            rcvAdapter = adapter!!
                            rcvMyCases!!.adapter = rcvAdapter;
                            rcvAdapter.notifyDataSetChanged();


                            adapter!!.setOnCardClickListener(object : NewLeads_Adapter.onItemClickListener {
                                override fun onItemClick(position: Int, view: View) {
                                    if (view.id == ivCustomerCall.id) {
                                        var lsmobileno: String? = null
                                        lsmobileno = newleads_model.get(position).lsMobile
                                        val enableMobile = newleads_model.get(position).sr_sharemobileno
                                        if(enableMobile.equals("Y"))
                                        {
                                        if (lsmobileno != null && lsmobileno != "") {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + lsmobileno))
                                            startActivity(intent)
                                        } else {
                                            TastyToast.makeText(context, getString(R.string.Mobile_No_Not_Shared), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                                        }
                                    }
                                    else
                                    {
                                        TastyToast.makeText(context,getString(R.string.Customer_Has_Disabled_This_Option),TastyToast.LENGTH_LONG,TastyToast.WARNING)
                                    }
                                    } else if (view.id == ivCustomerMessage.id) {
                                        val enableMessage = newleads_model.get(position).sr_enablemessaging
                                        if (enableMessage.equals("Y")) {

                                        var transactionid = newleads_model.get(position).txn_id
                                        var cust_id = newleads_model.get(position).cust_id
                                        val i = Intent(context, Message::class.java)
                                        i.putExtra(StaticRefs.CUST_ID, cust_id)
                                        i.putExtra(StaticRefs.TRANSACTIONID, transactionid.toString())
                                        i.putExtra(StaticRefs.CUSTOMERFIRSTNAME, newleads_model.get(position).lscustomerfirstname)
                                        startActivity(i)

                                        }
                                        else {
                                            TastyToast.makeText(context, getString(R.string.Customer_Has_Disabled_This_Option), TastyToast.LENGTH_LONG, TastyToast.WARNING)
                                        }

                                      /*  var mobileno: String? = null
                                        mobileno = newleads_model.get(position).lsMobile

                                        val sendIntent = Intent()
                                        sendIntent.action = Intent.ACTION_SEND
                                        sendIntent.putExtra(Intent.EXTRA_TEXT, "hi")
                                        //sendIntent.putExtra("jid", mobileno + "@s.whatsapp.net");
                                        sendIntent.type = "text/plain"
                                        // sendIntent.`package` = "com.whatsapp"
                                        if (sendIntent.resolveActivity(packageManager) != null) {
                                            startActivity(sendIntent)
                                        }*/
                                    } else if (view.id == llLeadsDetails.id) {
                                        var transactionid = newleads_model.get(position).txn_id
                                        var cust_id = newleads_model.get(position).cust_id
                                        val i = Intent(context, NewLeads_Detail::class.java)
                                        i.putExtra("key", "Cases")
                                        i.putExtra("status",lscasestatus)
                                        i.putExtra(StaticRefs.CUST_ID, cust_id)
                                        i.putExtra(StaticRefs.TRANSACTIONID, newleads_model.get(position).txn_id)
                                        i.putExtra("call",newleads_model.get(position).sr_sharemobileno)
                                        i.putExtra("message",newleads_model.get(position).sr_enablemessaging)
                                        i.putExtra(StaticRefs.REJECTIONREASON,newleads_model.get(position).lsrejectionreason)
                                        startActivity(i)
                                    }

                                }
                            })

                        } else {
                            /* rcvAdapter = adapter!!
                         rcvMyCases.adapter = rcvAdapter;
                         rcvAdapter.notifyDataSetChanged();*/
                            rcvMyCases!!.visibility = View.GONE

                            TastyToast.makeText(context, getString(R.string.No_Cases_Found), TastyToast.LENGTH_SHORT, TastyToast.INFO).show()

                        }
                    }
                    else{
                        rcvMyCases!!.visibility = View.GONE

                        TastyToast.makeText(context, getString(R.string.No_Cases_Found), TastyToast.LENGTH_SHORT, TastyToast.INFO).show()

                    }
                }
            }
        }


    }

   override fun onClick(v: View?) {
        super.onClick(v)
        when (v!!.id) {
           /* R.id.tvOngoing, R.id.vOngoing -> {
                lscasestatus = ONGOING
                vOngoing.setBackgroundColor(getResources().getColor(R.color.white))
                vAccecpted.setBackgroundColor(getResources().getColor(R.color.green))
                vRejected.setBackgroundColor(getResources().getColor(R.color.green))
                vHired.setBackgroundColor(getResources().getColor(R.color.green))
                vCompleted.setBackgroundColor(getResources().getColor(R.color.green))

                showData(lscasestatus)
            }
            R.id.tvAccecpted, R.id.vAccecpted -> {
                lscasestatus = ACCEPTED
                vOngoing.setBackgroundColor(getResources().getColor(R.color.green))
                vAccecpted.setBackgroundColor(getResources().getColor(R.color.white))
                vRejected.setBackgroundColor(getResources().getColor(R.color.green))
                vHired.setBackgroundColor(getResources().getColor(R.color.green))
                vCompleted.setBackgroundColor(getResources().getColor(R.color.green))

                showData(lscasestatus)

            }
            R.id.tvRejected, R.id.vRejected -> {
                lscasestatus = REJECTED
                vOngoing.setBackgroundColor(getResources().getColor(R.color.green))
                vAccecpted.setBackgroundColor(getResources().getColor(R.color.green))
                vRejected.setBackgroundColor(getResources().getColor(R.color.white))
                vHired.setBackgroundColor(getResources().getColor(R.color.green))
                vCompleted.setBackgroundColor(getResources().getColor(R.color.green))

                showData(lscasestatus)

            }
            R.id.tvHired, R.id.vHired -> {
                lscasestatus = HIRED
                vOngoing.setBackgroundColor(getResources().getColor(R.color.green))
                vAccecpted.setBackgroundColor(getResources().getColor(R.color.green))
                vRejected.setBackgroundColor(getResources().getColor(R.color.green))
                vHired.setBackgroundColor(getResources().getColor(R.color.white))
                vCompleted.setBackgroundColor(getResources().getColor(R.color.green))

                showData(lscasestatus)

            }
            R.id.tvCompleted, R.id.vCompleted -> {
                lscasestatus = COMPLETED
                vOngoing.setBackgroundColor(getResources().getColor(R.color.green))
                vAccecpted.setBackgroundColor(getResources().getColor(R.color.green))
                vRejected.setBackgroundColor(getResources().getColor(R.color.green))
                vHired.setBackgroundColor(getResources().getColor(R.color.green))
                vCompleted.setBackgroundColor(getResources().getColor(R.color.white))
                showData(lscasestatus)

            }*/
            R.id.ivFilter -> {
                showPopup(v)

            }


        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun showPopup(view: View){

        var popup : PopupMenu? = null
        popup = android.widget.PopupMenu(this,view)

        popup.inflate(R.menu.booking_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item : MenuItem? ->

            when (item!!.itemId){
                R.id.accepted ->{

                    lscasestatus= ACCEPTED
                    setTitle(getString(R.string.Accepted_Cases)).toString()
                    showData(lscasestatus)

                    //TastyToast.makeText(context,"InProgress",Toast.LENGTH_LONG,TastyToast.SUCCESS).show()
                }
                R.id.completed ->{
                    lscasestatus= COMPLETED
                    setTitle(getString(R.string.Completed_Cases)).toString()
                    showData(lscasestatus)

                    // TastyToast.makeText(context,"Pending",Toast.LENGTH_LONG,TastyToast.INFO).show()
                }
                R.id.hired ->{
                    lscasestatus= HIRED
                    setTitle(getString(R.string.Hired_Cases)).toString()

                    showData(lscasestatus)

                    // TastyToast.makeText(context,"Done",Toast.LENGTH_LONG,TastyToast.CONFUSING).show()
                }
                R.id.rejected ->{

                    lscasestatus= REJECTED
                    showData(lscasestatus)
                    setTitle(getString(R.string.Rejected_Cases)).toString()

                }
                R.id.ongoing ->{

                    lscasestatus= ONGOING
                    setTitle(getString(R.string.Ongoing_Cases)).toString()

                    showData(lscasestatus)
                }


            }
            true

        })
        popup.show()
    }


}


