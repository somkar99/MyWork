package com.example.admin.h2hpartner.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.int
import com.beust.klaxon.string
import com.example.admin.h2hpartner.Services.StaticRefs

/**
 * Created by nehagulati on 2/22/18.
 */
class AddReferencesModel {


    companion object {

        val PR_REFCONTACTNUMBER = StaticRefs.PR_REFCONTACTNUMBER
        val PR_REFERENCE = StaticRefs.PR_REFERENCE
        val PR_PID = StaticRefs.PR_PID
        val PR_SLNO = StaticRefs.PR_SLNO
        val PR_CREATEDBY = StaticRefs.PR_CREATEDBY
        val PR_UPDATEDBY = StaticRefs.PR_UPDATEDBY
        val PR_ISACTIVE = StaticRefs.PR_ISACTIVE
        val PR_ID = StaticRefs.PR_ID
    }

    var lscontactno: String? = null
    var lscontactname: String? = null
    var lipid: Int? = 0
    var liid: Int? = 0
    var lisrno: Int? = 0
    var lspid: String? = null
    var lsid: String? = null
    var lssrno: String? = null
    var lsisactive: String? = null
    var lscreatedby: String? = null
    var lsupdatedby: String? = null

    constructor(jsonObject: JsonObject) {
        lscontactno = jsonObject.string(AddReferencesModel.PR_REFCONTACTNUMBER)
        lscontactname = jsonObject.string(AddReferencesModel.PR_REFERENCE)
        lspid = jsonObject.string(AddReferencesModel.PR_PID)
        lssrno = jsonObject.string(AddReferencesModel.PR_SLNO)
        lsisactive = jsonObject.string(AddReferencesModel.PR_ISACTIVE)
        lscreatedby = jsonObject.string(AddReferencesModel.PR_CREATEDBY)
        lsupdatedby = jsonObject.string(AddReferencesModel.PR_UPDATEDBY)

    }


}