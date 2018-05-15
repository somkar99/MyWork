package com.example.admin.h2hpartner.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.double
import com.beust.klaxon.int
import com.beust.klaxon.string
import java.util.zip.DeflaterOutputStream

/**
 * Created by apple on 06/03/18.
 */
class Pricing_Model {
    companion object {
        val PRICETYPE = "pst_costtype";
        val PRICEUNIT = "pst_cost_unit";
        val SERVICENAME = "srv_typedescription";
        val COSTFROM = "pst_cost_from"
        val COSTTO = "pst_cost_to"
        val REMARK = "pst_cost_remarks"
        val RATE = "pst_cost_rate"
        val FIXED = "pst_cost_fixed"
        val VISITING = "pst_cost_visiting"
        val SERVTYPE_ID = "pst_serv_type"
        val PRICETYPE_NAME = "price_type"
        val PRICEUNIT_NAME = "price_unit"


    }

    var srv_typedescription: String? = null;
    var pst_cost_unit: String? = null;
    var pst_costtype: String? = null;
    var pst_serv_type: String? = null;
    var pst_cost_remarks: String? = null;
    var price_type: String? = null;
    var price_unit: String? = null;
    var pst_cost_from: String? = null;
    var pst_cost_to: String? = null;
    var pst_cost_rate: String? = null;
    var pst_cost_fixed: String? = null;
    var pst_cost_visiting: String? = null;

    constructor(jsonObject: JsonObject) {
        pst_costtype = jsonObject.string(PRICETYPE)
        pst_cost_unit = jsonObject.string(PRICEUNIT)
        srv_typedescription = jsonObject.string(SERVICENAME)
        pst_serv_type = jsonObject.string(SERVTYPE_ID)
        price_type = jsonObject.string(PRICETYPE_NAME)
        price_unit = jsonObject.string(PRICEUNIT_NAME)
        pst_cost_to = jsonObject.string(COSTTO)
        pst_cost_from = jsonObject.string(COSTFROM)
        pst_cost_fixed = jsonObject.string(FIXED)
        pst_cost_remarks = jsonObject.string(REMARK)
        pst_cost_rate = jsonObject.string(RATE)
        pst_cost_visiting = jsonObject.string(VISITING)
    }
}