package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.GoMobeil.H2H.Extensions.OnItemClickListener
import com.GoMobeil.H2H.Extensions.addOnItemClickListener
import com.GoMobeil.H2H.Extensions.toast
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.admin.h2hpartner.Adapter.Pricing_Adapter
import com.example.admin.h2hpartner.App
import com.example.admin.h2hpartner.BaseActivity
import com.example.admin.h2hpartner.Models.Pricing_Model
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.CustomServices
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.home2.*
import kotlinx.android.synthetic.main.price_row.*
import kotlinx.android.synthetic.main.pricing.*
import kotlinx.android.synthetic.main.pricing_details.*
import org.json.JSONObject

class Pricing : BaseActivity() {
    override lateinit var context: Context
    override lateinit var activity: Activity
    lateinit var llm: LinearLayoutManager
    lateinit var rcvAdapter: RecyclerView.Adapter<Pricing_Adapter.ViewHolder>
    var adapter: Pricing_Adapter? = null
    lateinit var pd: TransperantProgressDialog

    companion object {
        var TAG: String? = "Pricing"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pricing)
        setTitle(getString(R.string.Pricing))
        hideFooter(true)

        context = this
        activity = this
        CustomServices.hideKB(activity)

        App.prefs?.dashboard(tvPricingName1, tvPricingNumber1, tvPricingPriBusiness1)

        initlayout()
        showData()
    }

    fun initlayout() {

        pd = TransperantProgressDialog(context)

        ivAddPricing.setOnClickListener {
            val intent = Intent(this, PricingDetails::class.java)
            intent.putExtra(StaticRefs.KEY, "New")
            startActivity(intent)
            finish()
        }
        llm = LinearLayoutManager(this);
        rcvPricing.layoutManager = llm;
        cbNext.setOnClickListener {
            TastyToast.makeText(this, getString(R.string.Update_Successfully), Toast.LENGTH_LONG, TastyToast.SUCCESS).show()
            finish()
        }

    }

    fun showData() {
        pd.show()

        Fuel.post(StaticRefs.PRICE_SHOW, listOf((StaticRefs.TOKEN to prefs.token), (StaticRefs.SPID to prefs.vendorid)))
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
        val response = json.getString(StaticRefs.DATA)
        val parser = Parser()
        val stringBuilder = StringBuilder(response)
        val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
        val price_model = model.map { Pricing_Model(it) }.filterNotNull();

        adapter = Pricing_Adapter(price_model);
        rcvAdapter = adapter!!
        rcvPricing.adapter = rcvAdapter;
        rcvAdapter.notifyDataSetChanged();

        //Delete clicked

        adapter!!.setOnCardClickListener(object : Pricing_Adapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                var serv_typeid = price_model.get(position).pst_serv_type
                var price_type = price_model.get(position).pst_costtype
                var price_unit = price_model.get(position).pst_cost_unit
                deleteRecord(serv_typeid!!, price_type!!, price_unit!!)

            }
        })

        //Edit Clicked

        adapter!!.setOnCardClickListener1(object : Pricing_Adapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                var servicename = price_model.get(position).srv_typedescription
                var priceunit = price_model.get(position).pst_cost_unit
                var pricetype = price_model.get(position).pst_costtype
                var remark = price_model.get(position).pst_cost_remarks
                var costto = price_model.get(position).pst_cost_to
                var costfrom = price_model.get(position).pst_cost_from
                var fixed = price_model.get(position).pst_cost_fixed
                var visiting = price_model.get(position).pst_cost_visiting
                var rate = price_model.get(position).pst_cost_rate
                var pricetype_name = price_model.get(position).price_type
                var priceunit_name = price_model.get(position).price_unit

                val intent = Intent(context, PricingDetails::class.java)
                intent.putExtra(Pricing_Model.PRICETYPE, pricetype)
                intent.putExtra(Pricing_Model.PRICEUNIT, priceunit)
                intent.putExtra(Pricing_Model.SERVICENAME, servicename)
                intent.putExtra(Pricing_Model.COSTTO, costto)
                intent.putExtra(Pricing_Model.REMARK, remark)
                intent.putExtra(Pricing_Model.FIXED, fixed)
                intent.putExtra(Pricing_Model.COSTFROM, costfrom)
                intent.putExtra(Pricing_Model.VISITING, visiting)
                intent.putExtra(Pricing_Model.RATE, rate)
                intent.putExtra(Pricing_Model.PRICETYPE_NAME, pricetype_name)
                intent.putExtra(Pricing_Model.PRICEUNIT_NAME, priceunit_name)
                intent.putExtra(StaticRefs.KEY, "Edit")
                startActivity(intent)

            }
        })
    }

    fun deleteRecord(serv_type: String, price_type: String, price_unit: String) {
        Fuel.post(StaticRefs.PRICEDETAILS, listOf(StaticRefs.SERVICETYPE to serv_type,
                (StaticRefs.SERV_ID to prefs.serviceid),
                (StaticRefs.COST_TYPE to price_type),
                (StaticRefs.PRICEUNIT to price_unit),
                (StaticRefs.UPDATEDBY to "VIKAS"),
                (StaticRefs.SPID to prefs.vendorid),
                (StaticRefs.TOKEN to prefs.token),
                (StaticRefs.ISACTIVE to "N")))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                    parseResponse2(result.get().content)

                }
    }

    fun parseResponse2(response: String) {
        val json = JSONObject(response)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {
                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                TastyToast.makeText(this, message, Toast.LENGTH_LONG, TastyToast.ERROR).show()
            } else {
                showData()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

