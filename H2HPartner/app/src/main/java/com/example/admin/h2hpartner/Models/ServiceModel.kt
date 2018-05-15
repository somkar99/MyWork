package com.example.admin.h2hpartner.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.int
import com.beust.klaxon.string

/**
 * Created by Computer on 06-03-2018.
 */
class ServiceModel {

    companion object {

        val SERVICEID = "srv_id"
        val SERVICENAME = "srv_name"
        val SRV_DESCRIPTION = "srv_description"
        val SRV_IMAGE = "srv_image"


    }

    var lisrv_id = 0
    var lssrv_name = ""
    var lssrv_description: String? = null;
    var lssrv_image: String? = null;

    constructor(jsonobject: JsonObject) {

        lisrv_id = jsonobject.int(ServiceModel.SERVICEID)!!
        lssrv_name = jsonobject.string(ServiceModel.SERVICENAME)!!
        lssrv_description = jsonobject.string(SRV_DESCRIPTION)
        lssrv_image = jsonobject.string(SRV_IMAGE)

    }

}