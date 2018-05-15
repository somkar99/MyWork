package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcel
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.admin.h2hpartner.App
import com.example.admin.h2hpartner.Models.Spinner_Model
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.CustomServices
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import kotlinx.android.synthetic.main.pricing_details.*
import org.json.JSONArray
import org.json.JSONObject
import android.widget.Spinner
import com.example.admin.h2hpartner.BaseActivity
import com.example.admin.h2hpartner.Models.Pricing_Model
import android.content.res.Configuration.HARDKEYBOARDHIDDEN_YES
import android.content.res.Configuration.HARDKEYBOARDHIDDEN_NO
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.sdsmdg.tastytoast.TastyToast


class PricingDetails() : BaseActivity(), AdapterView.OnItemSelectedListener {

    companion object {
        var TAG: String? = "PricingDetails"
    }

    override lateinit var context: Context
    override lateinit var activity: Activity

    var Key: String? = null
    var priceUnit: String? = null
    var priceType: String? = null
    var priceTypename: String? = null
    var priceUnitname: String? = null
    var serviceNameType: String? = null
    var costTo: String? = null
    var costFrom: String? = null
    var visiting: String? = null
    var rate: String? = null
    var fixed: String? = null
    var remark: String? = null
    var lsServicetype: String = ""
    var liServiceId: Int? = 0
    var lsPriceunit: String = ""
    var lsPriceTypeSV: String = ""
    var lsPriceunitSV: String = ""
    var lsType: String = ""
    var liRangefrom: Int? = 0
    var liRangeto: Int? = 0
    var liFixedamount: Int = 0
    var liRate: Int = 0
    var liVisitingcharge: Int = 0
    var lsRemark: String = ""
    var lsMessage: String = ""
    lateinit var alPriceUnit: ArrayList<Spinner_Model>
    lateinit var alPriceType: ArrayList<Spinner_Model>
    lateinit var alServiceType: ArrayList<Spinner_Model>
    lateinit var alServiceTypeSV: ArrayList<Int>
    val alArrayPriceTypeSV: ArrayList<String>? = ArrayList()        //array list for Storevalue PriceType
    val alArrayPriceUnit: ArrayList<String>? = ArrayList()   //array list for Storevalue PriceUnit
    var spPriceType: Spinner? = null
    var spServiceType: Spinner? = null
    var spPriceUnit: Spinner? = null
    lateinit var pd: TransperantProgressDialog

    constructor(parcel: Parcel) : this() {
        lsServicetype = parcel.readString()
        lsPriceunit = parcel.readString()
        lsType = parcel.readString()
        liRangefrom = parcel.readValue(Int::class.java.classLoader) as? Int
        liRangeto = parcel.readValue(Int::class.java.classLoader) as? Int
        liFixedamount = parcel.readInt()
        liRate = parcel.readInt()
        liVisitingcharge = parcel.readInt()
        lsRemark = parcel.readString()
        lsMessage = parcel.readString()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pricing_details)
        setTitle(getString(R.string.Pricing_Details))
        hideFooter(true)

        context = this
        activity = this

        pd = TransperantProgressDialog(context)
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        App.prefs?.dashboard(tvPricingName, tvPricingNumber, tvPricingPriBusiness)

        CustomServices.hideKB(activity)

        spPriceUnit = findViewById<Spinner>(R.id.spPriceunit) as Spinner
        spServiceType = findViewById<Spinner>(R.id.spServicetype) as Spinner
        spPriceType = findViewById<Spinner>(R.id.spType) as Spinner

        alPriceType = ArrayList()
        alPriceUnit = ArrayList()
        alServiceType = ArrayList()

        //Intent values
        Key = getIntent().getStringExtra(StaticRefs.KEY);
        priceUnit = getIntent().getStringExtra(Pricing_Model.PRICEUNIT)
        priceType = getIntent().getStringExtra(Pricing_Model.PRICETYPE)
        priceTypename = getIntent().getStringExtra(Pricing_Model.PRICETYPE_NAME)
        priceUnitname = getIntent().getStringExtra(Pricing_Model.PRICEUNIT_NAME)
        serviceNameType = getIntent().getStringExtra(Pricing_Model.SERVICENAME)
        costTo = getIntent().getStringExtra(Pricing_Model.COSTTO)
        costFrom = getIntent().getStringExtra(Pricing_Model.COSTFROM)
        rate = getIntent().getStringExtra(Pricing_Model.RATE)
        fixed = getIntent().getStringExtra(Pricing_Model.FIXED)
        visiting = getIntent().getStringExtra(Pricing_Model.VISITING)
        remark = getIntent().getStringExtra(Pricing_Model.REMARK)

        if (Key.equals("Edit")) {
            spServiceType!!.isEnabled = false
            spServiceType!!.isClickable = false
            spServiceType!!.setBackgroundColor(resources.getColor(R.color.white))
            getPriceType(priceUnitname!!)
            getPriceUnit(priceTypename!!)
            getServiceType(serviceNameType!!)

            setvalues();
        } else {
            getPriceType("")
            getPriceUnit("")
            getServiceType("")
        }

        initlayout()

    }

    fun setvalues() {
        var costTo = costTo.toString()
        var fixed = fixed.toString()
        var costFrom = costFrom.toString()
        var visiting = visiting.toString()
        var rate = rate.toString()
        etPto.setText(costTo)
        etPfrom.setText(costFrom)
        etVistingcharge.setText(visiting)
        etFixedamount.setText(fixed)
        etRate.setText(rate)
        etRemark.setText(remark)

    }

    fun initlayout() {

        cbPreset.setOnClickListener {
            clearData()
        }

        cbPsave.setOnClickListener {
            getData()
        }

    }

    fun getPriceType(string: String) {

        Fuel.post(StaticRefs.LOVS, listOf(StaticRefs.TOKEN to prefs.token,
                (StaticRefs.LOV_TYPE to "PT")))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->

                    parseResponse(result.get().content, string)
                }
    }

    fun parseResponse(response: String, string: String) {

        var jobject: JSONObject? = null
        val json = JSONObject(response)
        val data = JSONObject()

        try {
            val res = JSONObject(response).getString(StaticRefs.DATA)
            val jarray = JSONArray(res)

            for (i in 0..jarray.length() - 1) {
                jobject = jarray.getJSONObject(i)

                val displayValue = jobject.getString("lov_displayvalue")
                val storeValue = jobject.getString("lov_storevalue")

                val spinnermodel = Spinner_Model(displayValue, storeValue)
                alPriceType!!.add(spinnermodel)

            }

        } catch (e: Exception) {
            println("$TAG Exception 1453 is $e")
        }

        showPriceType(string)
    }

    fun showPriceType(defaulttype: String) {
        val alArray: ArrayList<String>? = ArrayList()

        for (i in 0..alPriceType.size - 1) {
            var lsDisplayVal = alPriceType.get(i).lov_displayvalue
            var lsStoreVal = alPriceType.get(i).lov_storevalue
            alArray!!.add(lsDisplayVal!!)
            alArrayPriceTypeSV!!.add(lsStoreVal!!)

        }

        spPriceType!!.setOnItemSelectedListener(this)
        val serviceTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, alArray)
        serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPriceType!!.setAdapter(serviceTypeAdapter)
        selectSpinnerValue(spPriceType!!, defaulttype)

    }

    private fun selectSpinnerValue(spinner: Spinner, myString: String) {
        val index = 0
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == myString) {
                spinner.setSelection(i)
                break
            }
        }
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

        when (arg0.id) {
            R.id.spType -> {
                if (spPriceType!!.id == R.id.spType) {
                    lsPriceTypeSV = alArrayPriceTypeSV!!.get(position)
                    if (lsPriceTypeSV.equals("PF")) {
                        llFixed.visibility = View.VISIBLE
                        llRange.visibility = View.GONE
                        llRate.visibility = View.GONE

                        lsPriceTypeSV = alArrayPriceTypeSV!!.get(position)
                    } else if (lsPriceTypeSV.equals("PR")) {

                        llFixed.visibility = View.GONE
                        llRange.visibility = View.VISIBLE
                        llRate.visibility = View.GONE
                        lsPriceTypeSV = alArrayPriceTypeSV!!.get(position)
                    } else if (lsPriceTypeSV.equals("RT")) {

                        llFixed.visibility = View.GONE
                        llRange.visibility = View.GONE
                        llRate.visibility = View.VISIBLE
                        lsPriceTypeSV = alArrayPriceTypeSV!!.get(position)

                    } else {
                        llFixed.visibility = View.GONE
                        llRange.visibility = View.GONE
                        llRate.visibility = View.GONE
                        lsPriceTypeSV = alArrayPriceTypeSV!!.get(position)

                    }
                }
            }
            R.id.spPriceunit -> {

                if (spPriceUnit!!.id == R.id.spPriceunit) {
                    lsPriceunitSV = alArrayPriceUnit!!.get(position)
                }
            }
            R.id.spServicetype ->
                if (spServiceType!!.id == R.id.spServicetype) {
                    liServiceId = alServiceTypeSV.get(position)

                }
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {}

    fun clearData() {
        etPfrom.setText("")
        etPto.setText("")
        etFixedamount.setText("")
        etRate.setText("")
        etVistingcharge.setText("")
        etRemark.setText("")

    }

    fun getData() {
        var lbProceedAhead = true;

        lsServicetype = spServicetype.selectedItem.toString().trim()

        if ((lsServicetype != null && lsServicetype.equals("No Service Type"))) {
            genMessage(getString(R.string.Service_Type));
            lbProceedAhead = false;
        }

        lsPriceunit = spPriceunit!!.selectedItem.toString().trim()

        if (!((lsPriceunit != null && lsPriceunit.length > 0))) {
            genMessage(getString(R.string.Price_Unit));
            lbProceedAhead = false;
        }

        lsType = spType!!.selectedItem.toString().trim()
        if (!((lsType != null && lsType.length > 0))) {
            genMessage(getString(R.string.Type));
            lbProceedAhead = false;
        }

        if (lsPriceTypeSV.equals("PR")) {
            val lsrangefrom = etPfrom.text.toString().trim()

            if (!((lsrangefrom != null && lsrangefrom.length > 0))) {
                genMessage(getString(R.string.Range_From))
                lbProceedAhead = false;
            } else {
                liRangefrom = lsrangefrom.toInt()
            }

            val lsrangeto = etPto.text.toString().trim()

            if (!((lsrangeto != null && lsrangeto.length > 0))) {
                genMessage(getString(R.string.Range_To))
                lbProceedAhead = false;
            } else {
                liRangeto = lsrangeto.toInt()
            }

            if (!(liRangefrom!! < liRangeto!!)) {
                lbProceedAhead = false;
                genMessage(getString(R.string.Range_must_Be_Greater))
            } else {

            }

        } else if (lsPriceTypeSV.equals("PF")) {
            val lsfixed = etFixedamount.text.toString().trim()

            if (!((lsfixed != null && lsfixed.length > 0))) {
                genMessage(getString(R.string.Fixed_Amount))
                lbProceedAhead = false;
            } else {

                liFixedamount = lsfixed.toInt()
            }
        } else if (lsPriceTypeSV.equals("RT")) {

            val lsrate = etRate.text.toString()
            if (!((lsrate != null && lsrate.length > 0))) {
                genMessage(getString(R.string.Rate))
                lbProceedAhead = false;
            } else {
                liRate = lsrate.toInt()
            }
        } else if (lsPriceTypeSV.equals("OI")) {
        }

        val lsvisitingcharge = etVistingcharge.text.toString()

        if (!((lsvisitingcharge != null && lsvisitingcharge.length > 0))) {
            genMessage(getString(R.string.Visiting_Charge))
            lbProceedAhead = false;
        } else {
            liVisitingcharge = lsvisitingcharge.toInt()
        }

        lsRemark = etRemark.text.toString().trim()
        if (!((lsRemark != null && lsRemark.length > 0))) {
            genMessage(getString(R.string.Remark))
            lbProceedAhead = false;
        }


        if (!(lbProceedAhead) && lsMessage.length > 0) {
            if (lsMessage.length > 100) {
                TastyToast.makeText(context, getString(R.string.Please_Enter_All_Details), TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
            } else {
                TastyToast.makeText(context, getString(R.string.Please_Enter_Valid) + lsMessage, TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
            }

        } else {
            saveData()
        }
    }

    fun saveData() {

        var params: Map<String, String>
        pd.show()
        Fuel.post(StaticRefs.PRICEDETAILS, listOf(StaticRefs.SERVICETYPE to liServiceId,
                (StaticRefs.PRICEUNIT to lsPriceunitSV),
                (StaticRefs.SERV_ID to prefs.serviceid),              //int
                (StaticRefs.COST_TYPE to lsPriceTypeSV),
                (StaticRefs.SPID to prefs.vendorid),   //int
                (StaticRefs.PRICEFROM to liRangefrom),
                (StaticRefs.PRICETO to liRangeto),
                (StaticRefs.FIX_PRICE to liFixedamount),
                (StaticRefs.RATE to liRate),
                (StaticRefs.TOKEN to prefs.token),
                (StaticRefs.VISITING_CHARGES to liVisitingcharge),
                (StaticRefs.UPDATEDBY to "VIKAS"),
                (StaticRefs.ISACTIVE to "Y"),        //Varchar (1)
                (StaticRefs.REMARK to lsRemark)))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->
                        parseResponse2(result.get().content)

                    }, { err ->
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        //do something with error
                    })

                }
    }

    fun parseResponse2(response: String) {
        val json = JSONObject(response)
        // val data = JSONObject(response).getJSONObject(StaticRefs.DATA)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {

                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                pd.dismiss()
                TastyToast.makeText(this, message, TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
            } else {
                TastyToast.makeText(context, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
                pd.dismiss()
                if (prefs.profilestatus.equals(StaticRefs.INCOMPLETE)) {
                    prefs.pricinginfostatus = StaticRefs.COMPLETE
                }
                val intent = Intent(this, Pricing::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun genMessage(msg: String) {
        if (lsMessage.length > 0) {
            lsMessage = lsMessage + ", " + msg;
        } else {
            lsMessage = lsMessage + " " + msg;
        }
    }

    fun showData() {
        var params: Map<String, String>
        Fuel.post(StaticRefs.PRICE_SHOW, listOf(StaticRefs.SPID to prefs.VENDORID,
                (StaticRefs.TOKEN to prefs.token)))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->
                        parseResponse1(result.get().content)
                    }, { err ->
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        //do something with error
                    })
                }

    }

    fun parseResponse1(response: String) {
        val json = JSONObject(response)
        val data = JSONObject(response).getJSONObject(StaticRefs.DATA)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {

                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {

                TastyToast.makeText(this, message, TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
            } else {
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                TastyToast.makeText(context, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()

            }
        }
    }

    fun getServiceType(lsVal: String) {
        Fuel.post(StaticRefs.SERVICE_TYPE_LOV, listOf(StaticRefs.TOKEN to prefs.token,
                (StaticRefs.SRV_ID to prefs.serviceid)))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                    parseResponse3(result.get().content, lsVal)
                }
    }

    fun parseResponse3(response: String, lsVal: String) {
        var jobject: JSONObject? = null
        val json = JSONObject(response)
        val abc = json.getString(StaticRefs.DATA)
        val xyz = JSONArray(abc)

        if (xyz.length() > 0) {
            try {
                val res = JSONObject(response).getString(StaticRefs.DATA)
                val jarray = JSONArray(res)

                for (i in 0..jarray.length() - 1) {
                    jobject = jarray.getJSONObject(i)

                    val servicetype = jobject.getString("srv_typedescription")
                    val servicetypeid = jobject.getInt("srv_typeid")

                    val spinnermodel = Spinner_Model(servicetype, servicetypeid)
                    alServiceType.add(spinnermodel)

                }

            } catch (e: Exception) {
                println("$TAG Exception 1453 is $e")
            }
            showServiceType(lsVal)
        } else {

            lsServicetype = "null"
            TastyToast.makeText(context, getString(R.string.No_Service_Type), TastyToast.LENGTH_SHORT, TastyToast.INFO).show()

        }
    }

    fun showServiceType(lsVal: String) {
        val alArray: ArrayList<String>? = ArrayList()
        alServiceTypeSV = ArrayList()

        for (i in 0..alServiceType!!.size - 1) {
            var lsDisplayVal = alServiceType.get(i).srv_typedescription
            var lsStoreVal = alServiceType.get(i).srv_typeid
            alArray!!.add(lsDisplayVal!!)
            alServiceTypeSV!!.add(lsStoreVal!!)
        }

        spServiceType!!.setOnItemSelectedListener(this)
        val serviceTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, alArray)
        serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spServiceType!!.setAdapter(serviceTypeAdapter)
        selectSpinnerValue(spServiceType!!, lsVal)
    }

    fun getPriceUnit(lsVal: String) {

        Fuel.post(StaticRefs.LOVS, listOf(StaticRefs.TOKEN to prefs.token,
                (StaticRefs.LOV_TYPE to "PU")))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                    parseResponse4(result.get().content, lsVal)
                }
    }

    fun parseResponse4(response: String, lsVal: String) {

        var jobject: JSONObject? = null
        val json = JSONObject(response)
        val data = JSONObject()

        try {
            val res = JSONObject(response).getString(StaticRefs.DATA)
            val jarray = JSONArray(res)

            for (i in 0..jarray.length() - 1) {
                jobject = jarray.getJSONObject(i)

                val displayValue = jobject.getString("lov_displayvalue")
                val storeValue = jobject.getString("lov_storevalue")
                val spinnermodel = Spinner_Model(displayValue, storeValue)
                alPriceUnit!!.add(spinnermodel)
            }

        } catch (e: Exception) {
            println("$TAG Exception 1453 is $e")
        }

        showPriceUnit(lsVal)
    }

    fun showPriceUnit(lsVal: String) {

        val alArray: ArrayList<String>? = ArrayList()

        for (i in 0..alPriceUnit.size - 1) {
            var lsDisplayVal = alPriceUnit.get(i).lov_displayvalue
            var lsStoreVal = alPriceUnit.get(i).lov_storevalue
            alArray!!.add(lsDisplayVal!!)
            alArrayPriceUnit!!.add(lsStoreVal!!)
        }

        spPriceUnit!!.setOnItemSelectedListener(this)
        val serviceTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, alArray)
        serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPriceUnit!!.setAdapter(serviceTypeAdapter)
        selectSpinnerValue(spPriceUnit!!, lsVal)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}



