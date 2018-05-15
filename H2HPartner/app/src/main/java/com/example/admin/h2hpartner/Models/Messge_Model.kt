package com.WeApplify.ITR.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.string
import com.example.admin.h2hpartner.Services.StaticRefs

/**
 * Created by apple on 10/10/17.
 */

class Message_Model {

    companion object {
        val CHATCUSTOMERID = StaticRefs.CHATCUSTOMERID
        val CHATVENDORID = StaticRefs.CHATVENDORID
        val CHATTXNID = StaticRefs.CHATTXNID
        val CHATSENDER = StaticRefs.CHATSENDER
        val CHATMESSAGE = StaticRefs.CHATMESSAGE
        val CHAT_DATETIME = StaticRefs.CHAT_DATETIME
    }

    var tc_custid: Int? = null;
    var tc_spid: Int? = null;
    var tc_txnid: Int? = null;
    var tc_sender: String? = null;
    var tc_message: String? = null;
    var created_at: String? = null;

    constructor(jsonObject: JsonObject) {
        tc_custid = jsonObject.string(CHATCUSTOMERID)!!.toInt()
        tc_spid = jsonObject.string(CHATVENDORID)!!.toInt()
       /*  val tc_txnid1 = jsonObject.string(CHATTXNID)
        tc_txnid=tc_txnid1!!.toInt()*/
        tc_sender = jsonObject.string(CHATSENDER)
        tc_message = jsonObject.string(CHATMESSAGE)
        created_at = jsonObject.string(CHAT_DATETIME)

    }
}

