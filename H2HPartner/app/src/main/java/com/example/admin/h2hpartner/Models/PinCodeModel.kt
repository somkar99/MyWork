package com.example.admin.h2hpartner.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.string

/**
 * Created by nehagulati on 2/24/18.
 */
class PinCodeModel {

    companion object {

        val PINCODE = "pincode"
        val AREA = "area"
    }

    var lspincode: String? = null
    var lsarea: String? = null

    constructor(jsonobject: JsonObject) {

        lspincode = jsonobject.string(PinCodeModel.PINCODE)
        lsarea = jsonobject.string(PinCodeModel.PINCODE)

    }

    constructor(lspincode: String?, lsarea: String?) {
        this.lspincode = lspincode
        this.lsarea = lsarea
    }


}