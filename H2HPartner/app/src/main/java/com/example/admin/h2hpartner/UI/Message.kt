package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.WeApplify.ITR.Models.Message_Model
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.admin.h2hpartner.Adapter.Message_Adapter
import com.example.admin.h2hpartner.Adapter.Pricing_Adapter
import com.example.admin.h2hpartner.Models.Message_Conv_Model
import com.example.admin.h2hpartner.Models.Pricing_Model
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.message.*
import kotlinx.android.synthetic.main.pricing.*
import org.json.JSONObject
import java.net.CacheResponse

/**
 * Created by apple on 12/04/18.
 */
class Message :Activity()
{

    lateinit var context: Context
    lateinit var activity: Activity
    lateinit var llm: LinearLayoutManager
    lateinit var lsMessage:String
    lateinit var rcvAdapter: RecyclerView.Adapter<Message_Adapter.ViewHolder>
    var adapter: Message_Adapter? = null
    lateinit var pd: TransperantProgressDialog
    var cust_id:String?=""
    var txn_id:String?=""
    var Key:String?=""
    var custname:String?=""
    var custimage:String?=""

    companion object {
        var TAG: String? = "Message"
        val CHATSENDER="V"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message)

        activity = this
        context = this
        pd = TransperantProgressDialog(context)
        Key=getIntent().getStringExtra(StaticRefs.KEY)
        if(Key.equals(StaticRefs.NOTIFICATIONTYPE)){
            cust_id = getIntent().getStringExtra(StaticRefs.CHATCUSTOMERID)
            custname = getIntent().getStringExtra(StaticRefs.CUSTOMERFIRSTNAME)
        }
        else if (Key.equals("CONV")){
            cust_id = getIntent().getStringExtra((Message_Conv_Model.CUST_ID))
            txn_id = getIntent().getStringExtra(Message_Conv_Model.TXN_ID)
            custname = getIntent().getStringExtra(Message_Conv_Model.CUST_FNAME)
            custimage=getIntent().getStringExtra(Message_Conv_Model.CUSTIMAGE)
        }
        else{
            cust_id = getIntent().getStringExtra(StaticRefs.CUST_ID)
         txn_id = getIntent().getStringExtra(StaticRefs.TRANSACTIONID)
            custname = getIntent().getStringExtra(StaticRefs.CUSTOMERFIRSTNAME)

        }

        tvCustName.setText(custname)
        if(!(custimage.equals("null")||custimage.equals(null)||custimage.equals(""))) {
            ivChatProfileImage.loadBase64Image(custimage)
        }
        else{
            ivChatProfileImage.setImageResource(R.drawable.uploadprofile_icon)
        }

        getChats()
        llm = LinearLayoutManager(this);
        rcvMessage.layoutManager = llm;
        initlayout()
    }

    fun initlayout(){
        //  ivChatProfileImage.loadBase64Image(prefs.profileimage)
        ivSendMessage.setOnClickListener {
            lsMessage=etSendMessage.text.toString()
            if(!lsMessage.equals("")){
                sendMessage()
            }
        }
        ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    fun getChats() {
        pd.show()
        Fuel.post(StaticRefs.CHATSHOW, listOf((StaticRefs.CHATVENDORID to prefs.vendorid), (StaticRefs.CHATCUSTOMERID to cust_id)))
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->
                        parseJson(result.get().content)
                        // pd.dismiss()
                    }, { err ->
                        pd.dismiss()
                        TastyToast.makeText(context,getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
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

                val error = json.getString("errors")

                TastyToast.makeText(this, error, Toast.LENGTH_LONG, TastyToast.ERROR).show()

            } else {

                if (json.has(StaticRefs.DATA) && json.getString(StaticRefs.DATA) != null) {

                    val json = JSONObject(data)
                    if(json.length()>0){

                    val response = json.getString(StaticRefs.DATA)
                    val parser = Parser()
                    val stringBuilder = StringBuilder(response)
                    val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
                    val price_model = model.map { Message_Model(it) }.filterNotNull();

                    adapter = Message_Adapter(price_model);
                    rcvAdapter = adapter!!
                    rcvMessage.adapter = rcvAdapter;
                    llm.setStackFromEnd(true)
                    rcvAdapter.notifyDataSetChanged();
                    pd.dismiss()
                    }
                    else{
                        TastyToast.makeText(context,"No Chats ",Toast.LENGTH_SHORT,TastyToast.WARNING)

                    }
                }else{
                    TastyToast.makeText(context,"No Chats ",Toast.LENGTH_SHORT,TastyToast.WARNING)
                }

            }
        }


    }

    fun sendMessage(){
        pd.show()
        Fuel.post(StaticRefs.CHATSENDMESSAGE, listOf((StaticRefs.CHATVENDORID to prefs.vendorid),(StaticRefs.CHATSENDER to CHATSENDER),
                (StaticRefs.CHATMESSAGE to lsMessage),(StaticRefs.CHATTXNID to txn_id),
                (StaticRefs.CHATCUSTOMERID to cust_id)))
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->
                        ParseSendMessageResponse(result.get().content)
                        // pd.dismiss()
                    }, { err ->
                       pd.dismiss()
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })
                }

    }

    fun ParseSendMessageResponse(response:String){
        val json = JSONObject(response)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {

                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {

                val error = json.getString("errors")

                TastyToast.makeText(this, error, Toast.LENGTH_LONG, TastyToast.ERROR).show()

            } else {


                etSendMessage.setText("")
                getChats()


            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}


