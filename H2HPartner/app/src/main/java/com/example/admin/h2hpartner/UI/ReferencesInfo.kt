package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.GoMobeil.H2H.Extensions.toast
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.admin.h2hpartner.*
import com.example.admin.h2hpartner.Adapter.RefAdapter
import com.example.admin.h2hpartner.Models.AddReferencesModel
import com.example.admin.h2hpartner.Services.CustomServices
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.reference.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Admin on 06-02-18.
 */
class ReferencesInfo : BaseActivity() {


    companion object {
        lateinit var contactlist: MutableList<AddReferencesModel>
    }

    override lateinit var context: Context
    override lateinit var activity: Activity

    lateinit var alertdialog: android.support.v7.app.AlertDialog
    var lsname: String = ""
    var lscontactno: String = ""
    var lsMessage = ""

    lateinit var rcvList: RecyclerView
    lateinit var rcvAdapter: RecyclerView.Adapter<RefAdapter.ViewHolder>
    lateinit var layoutManager: RecyclerView.LayoutManager
    var adapter: RefAdapter? = null

    lateinit var pd: TransperantProgressDialog


    lateinit var aladdresslist: ArrayList<String>

    lateinit var reflist: JSONArray
    lateinit var model: AddReferencesModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reference)
        hideFooter(true)
        setTitle(getString(R.string.Reference_Details))
        activity = this;
        context = this
        CustomServices.hideKB(activity)

        pd = TransperantProgressDialog(context)


        App.prefs?.dashboard(tvRefname, tvRefmobileNo, tvRefpricebusiness)
        initlayout()
        getData()
    }

    fun initlayout() {
        ReferencesInfo.contactlist = mutableListOf()
        rcvList = findViewById(R.id.rcvReference)
        layoutManager = LinearLayoutManager(context)
        rcvList.layoutManager = layoutManager


        AddReference.setOnClickListener {

            getrefwithPopup()

        }

        cbRefsave.setOnClickListener {

            saveData()

        }
    }

    fun getrefwithPopup() {

        val layoutInflater = LayoutInflater.from(this)
        val dialogview = layoutInflater.inflate(R.layout.reference_dialog, null)
        val etName = dialogview.findViewById<EditText>(R.id.etName)
        val etMobNo = dialogview.findViewById<EditText>(R.id.etMobNo)

        val cbReset = dialogview.findViewById<Button>(R.id.cbReset)
        val cbcancel = dialogview.findViewById<ImageView>(R.id.ivcancel)
        val cbSave = dialogview.findViewById<Button>(R.id.cbSave)

        val popup1 = android.support.v7.app.AlertDialog.Builder(this)
        popup1.setView(dialogview)

        alertdialog = popup1.create()

        alertdialog.show()

        cbSave.setOnClickListener {
            //CustomServices.hideSoftKeyboard(activity)
            activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

            lsname = etName.text.toString().trim()
            lscontactno = etMobNo.text.toString().trim()

            var lbProceedAhead = true;


            if (!((lsname != null && lsname.length > 0))) {
                genMessage(getString(R.string.Contact_Name));
                lbProceedAhead = false;
            }

            if (!((lscontactno != null && lscontactno.length > 0))) {
                genMessage(getString(R.string.Contact_No));
                lbProceedAhead = false;
            }

            if (lbProceedAhead) {

                if (!StaticRefs.isValidContact(lscontactno)) {
                    etMobNo.setError(getString(R.string.Enter_Valid_Contact_No))
                    lbProceedAhead = false

                    TastyToast.makeText(context, getString(R.string.Enter_Valid_Contact_No), Toast.LENGTH_LONG, TastyToast.WARNING).show()

                }
            }


            if (!(lbProceedAhead) && lsMessage.length > 0) {
                if (lsMessage.length > 30) {

                    TastyToast.makeText(context, getString(R.string.Please_Enter_All_Details), Toast.LENGTH_LONG, TastyToast.WARNING).show()

                } else {

                    TastyToast.makeText(context, getString(R.string.Please_Enter_Valid) + lsMessage, Toast.LENGTH_LONG, TastyToast.WARNING).show()

                }

            } else {

                alertdialog.dismiss()
                saveReferenceData()

            }

        }
        cbReset.setOnClickListener {
            etMobNo.setText("")
            etName.setText("")
        }
        cbcancel.setOnClickListener {
         //   CustomServices.hideSoftKeyboard(activity)
            activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

            alertdialog.dismiss()
        }
    }

    fun saveReferenceData() {

        if (contactlist.size > 0) {

            var json = JsonObject()
            json.put(AddReferencesModel.PR_PID, prefs.vendorid)
            json.put(AddReferencesModel.PR_REFERENCE, lsname)
            json.put(AddReferencesModel.PR_REFCONTACTNUMBER, lscontactno)
            json.put(AddReferencesModel.PR_ISACTIVE, "Y")
            json.put(AddReferencesModel.PR_UPDATEDBY, prefs.fullname)
            json.put(AddReferencesModel.PR_CREATEDBY, prefs.fullname)

            model = AddReferencesModel(json)

            ReferencesInfo.contactlist.add(model)
            rcvAdapter.notifyItemInserted(contactlist.size - 1)
        } else {
            var json = JsonObject()
            json.put(AddReferencesModel.PR_PID, prefs.vendorid)
            json.put(AddReferencesModel.PR_REFERENCE, lsname)
            json.put(AddReferencesModel.PR_REFCONTACTNUMBER, lscontactno)
            json.put(AddReferencesModel.PR_ISACTIVE, "Y")
            json.put(AddReferencesModel.PR_UPDATEDBY, prefs.fullname)
            json.put(AddReferencesModel.PR_CREATEDBY, prefs.fullname)
            model = AddReferencesModel(json)
            ReferencesInfo.contactlist.add(model)
            adapter = RefAdapter(contactlist);
            rcvAdapter = adapter!!
            rcvList.adapter = rcvAdapter;
            rcvAdapter.notifyDataSetChanged();
        }
        addressdelete()
    }

    fun genMessage(msg: String) {
        if (lsMessage.length > 0) {
            lsMessage = lsMessage + ", " + msg;
        } else {
            lsMessage = lsMessage + " " + msg;
        }
    }

    fun saveData() {
        aladdresslist = ArrayList()

        reflist = JSONArray()

        if (contactlist.size > 0) {

            for (i in 0..contactlist.size - 1) {
                var adddressjs = JSONObject()
                adddressjs.put(StaticRefs.PR_PID, contactlist[i].lspid)
                adddressjs.put(StaticRefs.PR_REFERENCE, contactlist[i].lscontactname)
                adddressjs.put(StaticRefs.PR_REFCONTACTNUMBER, contactlist[i].lscontactno)
                adddressjs.put(StaticRefs.PR_ISACTIVE, contactlist[i].lsisactive)
                adddressjs.put(StaticRefs.PR_CREATEDBY, contactlist[i].lscreatedby)
                adddressjs.put(StaticRefs.PR_UPDATEDBY, prefs.fullname)

                // aladdresslist.add(i,adddressjs.toString())\

                reflist.put(adddressjs)

            }
            pd.show()
            Fuel.post(StaticRefs.VENDORREFERENCE, listOf(StaticRefs.TOKEN to prefs.token,
                    (StaticRefs.DATA to reflist.toString())))
                    .timeoutRead(StaticRefs.TIMEOUTREAD)
                    .responseJson()
                    { request,
                      response,
                      result ->
                        result.fold({ d ->

                            parseJsonSave(result.get().content)

                        }, { err ->
                            TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        })
                    }

        } else {
            pd.dismiss()

            TastyToast.makeText(context, getString(R.string.Please_Add_Atlest_One_Reference) + lsMessage, Toast.LENGTH_LONG, TastyToast.WARNING).show()

        }

    }

    fun parseJsonSave(response: String) {

        val json = JSONObject(response)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {

                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                pd.dismiss()
                val error = json.getString("errors")
                TastyToast.makeText(this, error, Toast.LENGTH_LONG, TastyToast.ERROR).show()

            } else if (json.getString(StaticRefs.STATUS).equals(StaticRefs.SUCCESS)) {
                if (prefs.profilestatus.equals(StaticRefs.INCOMPLETE)) {
                    prefs.referenceinfostatus = StaticRefs.COMPLETE
                }
                pd.dismiss()
                finish()

                TastyToast.makeText(context, message + lsMessage, Toast.LENGTH_LONG, TastyToast.SUCCESS).show()

            }


        }
    }

    fun getData() {
        pd.show()
        Fuel.post(StaticRefs.VENDORREFERENCESHOW, listOf((StaticRefs.PR_PID to prefs.vendorid), StaticRefs.TOKEN to prefs.token))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->

                        parseJson(result.get().content)

                    }, { err ->
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })

                }

    }

    fun parseJson(response: String) {
        pd.dismiss()
        val json = JSONObject(response)
        if (response.contains(StaticRefs.DATA) && json.getString(StaticRefs.DATA) != null && !json.getString(StaticRefs.DATA).equals("")) {

            val data = json.getString(StaticRefs.DATA)
            val jsondata = JSONArray(data)
            if (jsondata.length() > 0) {


                val refjsonarr = json.getJSONArray(StaticRefs.DATA)
                if (refjsonarr.length() > 0) {

                    var json1: JSONObject? = null
                    for (i in 0..refjsonarr.length() - 1) {
                        json1 = refjsonarr.getJSONObject(i)
                        val refjson: JsonObject
                        refjson = JsonObject()
                        refjson.put(AddReferencesModel.PR_PID, json1.getString(StaticRefs.PR_PID))
                        refjson.put(AddReferencesModel.PR_SLNO, json1.getString(StaticRefs.PR_SLNO))
                        refjson.put(AddReferencesModel.PR_REFERENCE, json1.getString(StaticRefs.PR_REFERENCE))
                        refjson.put(AddReferencesModel.PR_REFCONTACTNUMBER, json1.getString(StaticRefs.PR_REFCONTACTNUMBER))
                        refjson.put(AddReferencesModel.PR_ISACTIVE, json1.getString(StaticRefs.PR_ISACTIVE))
                        refjson.put(AddReferencesModel.PR_CREATEDBY, json1.getString(StaticRefs.PR_CREATEDBY))
                        refjson.put(AddReferencesModel.PR_UPDATEDBY, json1.getString(StaticRefs.PR_UPDATEDBY))
                        val model = AddReferencesModel(refjson)
                        ReferencesInfo.contactlist.add(model)
                    }

                    adapter = RefAdapter(contactlist);
                    rcvAdapter = adapter!!
                    rcvList.adapter = rcvAdapter;
                    rcvAdapter.notifyDataSetChanged();
                    addressdelete()

                } else {
                    TastyToast.makeText(this, "No Record is present in reference", Toast.LENGTH_LONG, TastyToast.WARNING).show()

                }
            } else {
                TastyToast.makeText(this, "No Record is present in reference", Toast.LENGTH_LONG, TastyToast.WARNING).show()

            }
        } else {
            pd.dismiss()

            TastyToast.makeText(this, getString(R.string.No_Record_Present_in_refernce), Toast.LENGTH_LONG, TastyToast.WARNING).show()

        }
    }

    fun addressdelete() {
        adapter!!.setItemClickIterface(object : RefAdapter.itemClickIterface {
            override fun itemClick(position: Int) {


                //TastyToast.makeText(context, "Delete Call for position ${position + 1}", Toast.LENGTH_LONG, TastyToast.WARNING).show()


                ReferencesInfo.contactlist.removeAt(position)
                adapter!!.notifyDataSetChanged()


            }
        })

    }


}

