package com.example.admin.h2hpartner.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.admin.h2hpartner.Adapter.Message_Adapter
import com.example.admin.h2hpartner.Adapter.Message_Conv_Adapter
import com.example.admin.h2hpartner.Adapter.Pricing_Adapter
import com.example.admin.h2hpartner.BaseActivity
import com.example.admin.h2hpartner.Models.Message_Conv_Model
import com.example.admin.h2hpartner.Models.Pricing_Model
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.Services.TransperantProgressDialog
import com.example.admin.h2hpartner.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.base_layout.*
import kotlinx.android.synthetic.main.message.*
import kotlinx.android.synthetic.main.message_conv.*
import kotlinx.android.synthetic.main.pricing.*
import org.json.JSONObject

/**
 * Created by apple on 16/04/18.
 */

class Message_Conv:BaseActivity() {

    override lateinit var context: Context
    override lateinit var activity: Activity
    lateinit var llm: LinearLayoutManager
    lateinit var lsMessage: String
    lateinit var rcvAdapter: RecyclerView.Adapter<Message_Conv_Adapter.ViewHolder>
    var adapter: Message_Conv_Adapter? = null
    lateinit var pd: TransperantProgressDialog
    var entity_type ="V"

    companion object {
        var TAG: String? = "Message_Conv"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message_conv)
        setTitle(getString(R.string.Messages))

        activity = this
        context = this
        pd = TransperantProgressDialog(context)
        ivNotifications.visibility = View.INVISIBLE



    }

    override fun onResume() {
        super.onResume()
        getChats()
    }

    fun getChats() {
        pd.show()

        Fuel.post(StaticRefs.CONVERSATIONS, listOf((StaticRefs.CHAT_ENTITY_ID to prefs.vendorid),StaticRefs.CHAT_ENTITY_TYPE to entity_type))
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
        val message = model.map { Message_Conv_Model(it) }.filterNotNull();

        llm = LinearLayoutManager(this);
        rcvMessageConv.layoutManager = llm;


        adapter = Message_Conv_Adapter(message);
        rcvAdapter = adapter!!
        rcvMessageConv.adapter = rcvAdapter;
        rcvAdapter.notifyDataSetChanged();

        adapter!!.setOnCardClickListener(object : Message_Conv_Adapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                var custid = message.get(position).tc_custid.toString()
                var txnid = message.get(position).tc_txnid.toString()
                var custname = message.get(position).cust_firstname.toString()
                var custimage = message.get(position).cust_image.toString()

                val intent = Intent(context, Message::class.java)
                intent.putExtra(Message_Conv_Model.CUST_ID, custid)
                intent.putExtra(StaticRefs.KEY, "CONV")
                intent.putExtra(Message_Conv_Model.CUST_FNAME, custname)
                intent.putExtra(Message_Conv_Model.TXN_ID, txnid)
                intent.putExtra(Message_Conv_Model.CUSTIMAGE,custimage)
                startActivity(intent)
            }
        })
    }
}


