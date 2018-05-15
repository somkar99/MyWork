package com.example.admin.h2hpartner.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.int
import com.beust.klaxon.string

/**
 * Created by apple on 26/02/18.
 */

class Spinner_Model {
    companion object {
        val LOV_DISPLAYVALUE = "lov_displayvalue";
        val LOV_TYPE = "lov_type";
        val LOV_STOREVALUE = "lov_storevalue";
        val SERVICETYPE = "srv_typedescription";
        val SERVICETYPEID = "srv_typeid";

    }

    var lov_displayvalue: String? = null;
    var lov_storevalue: String? = null;
    var lov_type: String? = null;
    var srv_typedescription: String? = null;
    var srv_typeid: Int? = null;

    constructor(jsonObject: JsonObject) {
        lov_displayvalue = jsonObject.string(LOV_DISPLAYVALUE)
        lov_storevalue = jsonObject.string(LOV_STOREVALUE)
        lov_type = jsonObject.string(LOV_TYPE)
        srv_typedescription = jsonObject.string(SERVICETYPE)
        srv_typeid = jsonObject.int(SERVICETYPEID)
    }

    constructor(lov_displayvalue: String?, lov_storevalue: String?) {
        this.lov_displayvalue = lov_displayvalue
        this.lov_storevalue = lov_storevalue
    }

    constructor(srv_typedescription: String?, srv_typeid: Int?) {
        this.srv_typedescription = srv_typedescription
        this.srv_typeid = srv_typeid
    }


}