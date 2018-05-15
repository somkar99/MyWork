package com.example.admin.h2hpartner.UI

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.admin.h2hpartner.Adapter.NewLeads_QandA_Adapter
import com.example.admin.h2hpartner.BaseActivity
import com.example.admin.h2hpartner.Model.QueAnsModel
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.CustomServices
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.base_layout.*
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.newlead_detail.*
import org.json.JSONObject
import java.text.SimpleDateFormat


class NewLeads_Detail : BaseActivity() {
    override lateinit var context: Context
    override lateinit var activity: Activity
    lateinit var pd: TransperantProgressDialog
    lateinit var llm: LinearLayoutManager
    lateinit var rcvAdapter: RecyclerView.Adapter<NewLeads_QandA_Adapter.ViewHolder>
    var adapter: NewLeads_QandA_Adapter? = null
    var lsTransaction: Int? = null
    lateinit var lsMobile:String
    var lsMessage = ""
    var lsRejectionReason = ""
    var lsStatus = ""
    var lscasestatus=""
    var Key:String?=""
     var cust_id: String?=""
    var call:String?=""
    var message:String?=""
    var customername:String?=""
    var rejectionreason:String?=""



    lateinit var alertdialog: android.support.v7.app.AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newlead_detail)

        hideFooter(true)
        ivNotifications.visibility = View.INVISIBLE

        context = this
        activity = this

        initlayout()
        showDetails()

    }


    @SuppressLint("ResourceAsColor")
    override fun onResume() {
        super.onResume()

        cust_id = getIntent().getStringExtra(StaticRefs.CUST_ID)

        Key = getIntent().getStringExtra("key")
        call=getIntent().getStringExtra("call")
       message= getIntent().getStringExtra("message")
        rejectionreason=getIntent().getStringExtra(StaticRefs.REJECTIONREASON)
        if(!(rejectionreason.equals("")||rejectionreason.equals("null")||rejectionreason.equals(null)))
        {
            tvLocation.visibility=View.GONE
            tvRejectionReason.visibility=View.VISIBLE
            tvRejectionReason.setText("Rejected as:"+rejectionreason)
            tvCall.visibility=View.INVISIBLE
            cbMessage.visibility=View.INVISIBLE

        }
        else{
            tvRejectionReason.visibility=View.GONE
            tvLocation.visibility=View.VISIBLE
            tvCall.visibility=View.VISIBLE
            cbMessage.visibility=View.VISIBLE
        }

        if(!call.equals("Y")){
            tvCall.setTextColor(R.color.light_grey)
            tvCall.isClickable=false

        }
        if(!message.equals("Y")){
            cbMessage.setTextColor(R.color.light_grey)
            cbMessage.isClickable=false

        }

        if(Key.equals("Leads")){
            setTitle(getString(R.string.Leads_Details))
            cbStartService.visibility=View.GONE

        }

        else if(Key.equals("Cases"))
        {
            lscasestatus = getIntent().getStringExtra("status")

            setTitle(getString(R.string.My_Cases))
            llButtons.visibility=View.GONE
            if(lscasestatus.equals(MyCases.HIRED)){
                llButtons.visibility=View.VISIBLE
                llacceptreject.visibility=View.GONE
                cbStartService.visibility=View.VISIBLE

            }
        }
    }

    fun initlayout() {

        pd = TransperantProgressDialog(context)
        lsTransaction = getIntent().getIntExtra(StaticRefs.TRANSACTIONID,0)

        ivLeads.isEnabled = false
        tvLeads.isEnabled = false

        llm = LinearLayoutManager(this);
        rcvQueAns.layoutManager = llm;

        tvCall.setOnClickListener {
            if(prefs.profilestatus.equals(StaticRefs.APPROVED)){
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + lsMobile))
            startActivity(intent)
        }else{
            TastyToast.makeText(context,getString(R.string.Profile_Not_Completed_Waiting_Approval),TastyToast.LENGTH_LONG,TastyToast.WARNING)

        }

        }
        cbAccept.setOnClickListener {
            if(prefs.profilestatus.equals(StaticRefs.APPROVED)){
            lsStatus=MyCases.ACCEPTED
            updateTransactionStatus()
            }else{
                TastyToast.makeText(context,getString(R.string.Profile_Not_Completed_Waiting_Approval),TastyToast.LENGTH_LONG,TastyToast.WARNING)

            }
        }
        cbReject.setOnClickListener {
            if(prefs.profilestatus.equals(StaticRefs.APPROVED)){
            lsStatus=MyCases.REJECTED
            rejectionReasonPopup()
        }else{
                TastyToast.makeText(context,getString(R.string.Profile_Not_Completed_Waiting_Approval),TastyToast.LENGTH_LONG,TastyToast.WARNING)

        }
        }
        cbStartService.setOnClickListener {
            lsStatus=MyCases.ONGOING
            updateTransactionStatus()
        }
        cbMessage.setOnClickListener {
            if(prefs.profilestatus.equals(StaticRefs.APPROVED)){
            val transactionid=1
            val i = Intent(context, Message::class.java)
            i.putExtra(StaticRefs.CUST_ID, cust_id)
            i.putExtra(StaticRefs.TRANSACTIONID, transactionid)
            i.putExtra(StaticRefs.CUSTOMERFIRSTNAME,customername )
            startActivity(i)
        }else{
            TastyToast.makeText(context,getString(R.string.Profile_Not_Completed_Waiting_Approval),TastyToast.LENGTH_LONG,TastyToast.WARNING)

        }
        }

    }

    fun showDetails() {
        Fuel.post(StaticRefs.NEWLEADS_DETAILED, listOf((StaticRefs.TOKEN to prefs.token), (StaticRefs.TRANSACTIONID1 to lsTransaction)))
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
        if(json.has(StaticRefs.DATA)&&!(json.getString(StaticRefs.DATA).equals("null"))&&!(json.getString(StaticRefs.DATA).equals(null))&&!(json.getString(StaticRefs.DATA).equals("")))
        {
        val response = json.getString(StaticRefs.DATA)
        val response1 = JSONObject(response)
            if(response1.length()>0) {
                val response2 = response1.getString(StaticRefs.QUESTION_ANSWER)

                var lsFName = response1.getString(StaticRefs.CUSTOMERFIRSTNAME)
                var lsLName = response1.getString(StaticRefs.CUSTOMERLASTNAME)
                customername = response1.getString(StaticRefs.CUSTOMERFIRSTNAME)
                var lsServiceName = response1.getString(StaticRefs.SERVICENAME)
                var lsTypeName = response1.getString(StaticRefs.SERVICETYPENAME)
                var lsDate = response1.getString(StaticRefs.SERV_PLANNERDATE)
                var lsTime = response1.getString(StaticRefs.SERV_PLANNERTIME)
                lsMobile = response1.getString(StaticRefs.MOBILENO)
                // var lsDistance = response1.getString(StaticRefs.CUSTOMERLASTNAME)
                var lsLocation = response1.getString(StaticRefs.ADDRESSLINE1) + ", " + response1.getString(StaticRefs.ADDRESSLINE2) + ", " + response1.getString(StaticRefs.CITY) + ", " + response1.getString(StaticRefs.STATE) + ", " + response1.getString(StaticRefs.PINCODE)

                tvName.setText("$lsFName $lsLName")
                tvServiceAsked.setText("$lsServiceName $lsTypeName")
                tvLocation.setText(lsLocation)
                tvDate.setText(getDate(lsDate))

                val parser = Parser()
                val stringBuilder = StringBuilder(response2)
                val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
                val new = model.map { QueAnsModel(it) }.filterNotNull();

                adapter = NewLeads_QandA_Adapter(new);
                rcvAdapter = adapter!!
                rcvQueAns.adapter = rcvAdapter;
                rcvAdapter.notifyDataSetChanged();
            }else{
                TastyToast.makeText(context,getString(R.string.No_Data_Found),TastyToast.LENGTH_SHORT,TastyToast.WARNING).show()

            }


        }
        else{
            TastyToast.makeText(context,getString(R.string.No_Data_Found),TastyToast.LENGTH_SHORT,TastyToast.WARNING).show()

        }
        }



    fun rejectionReasonPopup(){
        var Reason:String?=null
        val layoutInflater = LayoutInflater.from(this)
        val dialogview = layoutInflater.inflate(R.layout.newlead_rejection_dialog, null)

        val etOtherreason = dialogview.findViewById<EditText>(R.id.etOtherReason)
        val cvOtherreason = dialogview.findViewById<CardView>(R.id.cvOtherreason)

        val ivBusyChecked = dialogview.findViewById<ImageView>(R.id.ivBusyChecked)
        val ivBusyUnChecked = dialogview.findViewById<ImageView>(R.id.ivBusyUnChecked)
        val tvBusy = dialogview.findViewById<TextView>(R.id.tvBusy)

        val ivFarChecked = dialogview.findViewById<ImageView>(R.id.ivLocationFarChecked)
        val ivFarUnChecked = dialogview.findViewById<ImageView>(R.id.ivLocationFarUnChecked)
        val tvFar = dialogview.findViewById<TextView>(R.id.tvFar)

        val ivDontChecked = dialogview.findViewById<ImageView>(R.id.ivDontProvideChecked)
        val ivDontUnChecked = dialogview.findViewById<ImageView>(R.id.ivDontProvideUnChecked)
        val tvDontProvide = dialogview.findViewById<TextView>(R.id.tvDontProvide)

        val ivOtherChecked = dialogview.findViewById<ImageView>(R.id.ivOtherChecked)
        val ivOtherUnChecked = dialogview.findViewById<ImageView>(R.id.ivOtherUnChecked)
        val tvOther = dialogview.findViewById<TextView>(R.id.tvOther)


        val cbSubmit = dialogview.findViewById<Button>(R.id.cbReasonSubmit)


        val popup1 = android.support.v7.app.AlertDialog.Builder(this)
        popup1.setView(dialogview)

        alertdialog = popup1.create()

        alertdialog.show()

        ivBusyChecked.setOnClickListener {
            ivBusyUnChecked.visibility = View.VISIBLE
            ivBusyChecked.visibility = View.GONE
        }
        ivBusyUnChecked.setOnClickListener {
            ivBusyUnChecked.visibility = View.GONE
            ivBusyChecked.visibility = View.VISIBLE

        }

        ivFarChecked.setOnClickListener {
            ivFarUnChecked.visibility = View.VISIBLE
            ivFarChecked.visibility = View.GONE
        }
        ivFarUnChecked.setOnClickListener {
            ivFarUnChecked.visibility = View.GONE
            ivFarChecked.visibility = View.VISIBLE

        }

        ivDontChecked.setOnClickListener {
            ivDontUnChecked.visibility = View.VISIBLE
            ivDontChecked.visibility = View.GONE
        }
        ivDontUnChecked.setOnClickListener {
            ivDontUnChecked.visibility = View.GONE
            ivDontChecked.visibility = View.VISIBLE

        }

        ivOtherChecked.setOnClickListener {
            ivOtherUnChecked.visibility = View.VISIBLE
            ivOtherChecked.visibility = View.GONE
            cvOtherreason.visibility=View.GONE
            CustomServices.hideKB(alertdialog)
        }
        ivOtherUnChecked.setOnClickListener {
            ivOtherUnChecked.visibility = View.GONE
            ivOtherChecked.visibility = View.VISIBLE
            cvOtherreason.visibility=View.VISIBLE

        }

        cbSubmit.setOnClickListener {
            if(ivBusyChecked.visibility==View.VISIBLE){
                genMessage(tvBusy.text.toString())
            }
            if(ivFarChecked.visibility==View.VISIBLE){
                genMessage(tvFar.text.toString())
            }
            if(ivDontChecked.visibility==View.VISIBLE){
                genMessage(tvDontProvide.text.toString())
            }
            if(ivOtherChecked.visibility==View.VISIBLE){
                genMessage(etOtherreason.text.toString())
            }

            if(lsMessage.length>0){

                lsRejectionReason=lsMessage
                TastyToast.makeText(context,lsRejectionReason,TastyToast.LENGTH_SHORT,TastyToast.SUCCESS).show()
                alertdialog.dismiss()
                updateTransactionStatus()

            }
            else{
                TastyToast.makeText(context,getString(R.string.Please_Select_Rejection_Reason),TastyToast.LENGTH_SHORT,TastyToast.WARNING).show()
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
    fun updateTransactionStatus(){

        if(lsStatus.equals(MyCases.ACCEPTED)||lsStatus.equals(MyCases.ONGOING)) {

            Fuel.post(StaticRefs.UPDATETRASCTIONSTATUS, listOf(StaticRefs.TOKEN to prefs.token, (StaticRefs.LEAD_SERVICEPROVIDERID to prefs.vendorid),
                    (StaticRefs.TRANSACTIONID to lsTransaction.toString()),(StaticRefs.LEADSTATUS to lsStatus)))
                    .responseJson()
                    { request,
                      response,
                      result ->
                        result.fold({ d ->
                            pd.show()
                            parseUpdateTransactionStatus(result.get().content)
                        }, { err ->pd.dismiss()
                            TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                            //do something with error
                        })


                    }
        }
        else if(lsStatus.equals(MyCases.REJECTED)){

            Fuel.post(StaticRefs.UPDATETRASCTIONSTATUS, listOf(StaticRefs.TOKEN to prefs.token, (StaticRefs.LEAD_SERVICEPROVIDERID to prefs.vendorid),
                    (StaticRefs.TRANSACTIONID to lsTransaction.toString()),(StaticRefs.LEADSTATUS to lsStatus),
                    (StaticRefs.REJECTIONREASON to lsRejectionReason)))
                    .responseJson()
                    { request,
                      response,
                      result ->
                        result.fold({ d ->
                            pd.show()
                            parseUpdateTransactionStatus(result.get().content)
                        }, { err ->
                            TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                            //do something with error
                        })


                    }

        }
    }
    fun parseUpdateTransactionStatus(response: String){
        pd.dismiss()
        val json = JSONObject(response)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {
                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {

                TastyToast.makeText(this, message, Toast.LENGTH_LONG,TastyToast.ERROR).show()
            }
            else if(json.getString(StaticRefs.STATUS).equals(StaticRefs.SUCCESS)){
                pd.dismiss()
                finish()
                TastyToast.makeText(context, message, TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show()
            }

        }

    }
    private fun getDate(lsVal: String):String {
        val fmt = "yyyy-MM-dd"
        val df = SimpleDateFormat(fmt)
        val dt = df.parse(lsVal)
        val dfmt = SimpleDateFormat("dd/MM/yyyy")
        val dateOnly = dfmt.format(dt)
        return dateOnly
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}


