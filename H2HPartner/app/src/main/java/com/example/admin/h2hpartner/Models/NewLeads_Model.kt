package com.example.admin.h2hpartner.Model

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.int
import com.beust.klaxon.string
import com.example.admin.h2hpartner.Services.StaticRefs


class NewLeads_Model {
    companion object {
        val CUSTOMERFIRSTNAME = StaticRefs.CUSTOMERFIRSTNAME
        val CUSTOMERLASTNAME = StaticRefs.CUSTOMERLASTNAME
        val CUST_ID = StaticRefs.CUST_ID
        val SERVICENAME = StaticRefs.SERVICENAME
        val SERVICETYPENAME = StaticRefs.SERVICETYPENAME
        val TRANSACTIONID = StaticRefs.TRANSACTIONID
        val RECEIVEDDATE = StaticRefs.RECEIVEDDATE
        val DISTANCEINKM = StaticRefs.DISTANCEINKM
        val LATITUDE = StaticRefs.ADDRESSLATITUDE
        val LONGITUDE = StaticRefs.ADDRESSLONGITUDE
        val MOBILENO = StaticRefs.MOBILENO
        val ADDRESSLINE1 = StaticRefs.ADDRESSLINE1
        val ADDRESSLINE2 = StaticRefs.ADDRESSLINE2
        val STATE = StaticRefs.STATE
        val CITY = StaticRefs.CITY
        val PINCODE = StaticRefs.PINCODE
        val SR_SHAREMOBILENO = StaticRefs.SR_SHAREMOBILENO
        val SR_ENABLEMESSAGING = StaticRefs.SR_ENABLEMESSAGING
        val REJECTIONREASON=StaticRefs.REJECTIONREASON
    }

    var txn_id: Int? = null;
    var lscustomerfirstname: String? = null;
    var lscustomerlastname: String? = null;
    var lsservicetypename: String? = null;
    var lsservicename: String? = null;
    var lsreceiveddate: String? = null;
    var lsdistanceinkm: String? = null;
    var lslatitude: String? = null
    var lslogitude: String? = null
    var lsMobile: String? = null
    var lsAdd_line1: String? = null
    var lsAdd_line2: String? = null
    var lsState: String? = null
    var lsCity: String? = null
    var lsPincode: String? = null
    var cust_id: String? = null
    var lsrejectionreason:String?=null
    var sr_enablemessaging: String? = null
    var sr_sharemobileno: String? = null

    constructor(jsonObject: JsonObject) {
        txn_id = jsonObject.string(TRANSACTIONID)!!.toInt()
        if(!(jsonObject.string(REJECTIONREASON).equals("")||jsonObject.string(REJECTIONREASON).equals("null"))){
            lsrejectionreason=jsonObject.string(REJECTIONREASON)
        }
        lscustomerfirstname = jsonObject.string(CUSTOMERFIRSTNAME)
        lscustomerlastname = jsonObject.string(CUSTOMERLASTNAME)
        cust_id = jsonObject.string(CUST_ID)
        lsservicetypename = jsonObject.string(SERVICETYPENAME)
        lsservicename = jsonObject.string(SERVICENAME)
        lsreceiveddate = jsonObject.string(RECEIVEDDATE)
        lsdistanceinkm = jsonObject.string(DISTANCEINKM)
        lslatitude = jsonObject.string(LATITUDE)
        lslogitude = jsonObject.string(LONGITUDE)
        lsMobile = jsonObject.string(MOBILENO)
        lsAdd_line1 = jsonObject.string(ADDRESSLINE1)
        lsAdd_line2 = jsonObject.string(ADDRESSLINE2)
        lsState = jsonObject.string(STATE)
        lsCity = jsonObject.string(CITY)
        lsPincode = jsonObject.string(PINCODE)
        sr_enablemessaging = jsonObject.string(SR_ENABLEMESSAGING)
        sr_sharemobileno = jsonObject.string(SR_SHAREMOBILENO)

      //  lsrejectionreason=jsonObject.string(REJECTIONREASON)

    }
}


class QueAnsModel {
    companion object {
        val SRD_QID="srd_qId"
        val SQ_QUESTION="sq_question"
        val SRD_ANSWER="srd_answer"
    }

    var srd_qid: Int? = null;
    var sq_question : String? = null;
    var srd_answer: String? = null;


    constructor(jsonObject: JsonObject) {
        srd_qid = jsonObject.string(SRD_QID)!!.toInt()
        sq_question = jsonObject.string(SQ_QUESTION)
        srd_answer = jsonObject.string(SRD_ANSWER)
    }
}



