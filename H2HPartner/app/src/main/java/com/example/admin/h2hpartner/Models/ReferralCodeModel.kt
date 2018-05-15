package com.example.admin.h2hpartner.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.string
import com.example.admin.h2hpartner.Services.StaticRefs

/**
 * Created by admin on 16-04-2018.
 */
class ReferralCodeModel {
    companion object {
        val RFFIRSTNAME= StaticRefs.RFFIRSTNAME
        val RFLASTNAME= StaticRefs.RFLASTNAME
        val RFREGISTERON= StaticRefs.RFREGISTERON
        val RFREGISTERAS= StaticRefs.RFREGISTERAS
        val RFBUSINESSNAME= StaticRefs.RFBUSINESSNAME
        val RFPROFILEIMAGE= StaticRefs.RFPROFILEIMAGE

    }
    var lsfirstname:String?=""
    var lslastname:String?=""
    var lsregisteron:String?=""
    var lsregisteras:String?=""
    var lsbusinessname:String?=""
    var lsprofileimage:String?=""


    constructor(jsonObject: JsonObject) {
        lsfirstname = jsonObject.string(ReferralCodeModel.RFFIRSTNAME)
        lslastname = jsonObject.string(ReferralCodeModel.RFLASTNAME)
        lsregisteron = jsonObject.string(ReferralCodeModel.RFREGISTERON)
        lsregisteras = jsonObject.string(ReferralCodeModel.RFREGISTERAS)
        lsbusinessname = jsonObject.string(ReferralCodeModel.RFBUSINESSNAME)
        if(!(jsonObject.string(ReferralCodeModel.RFBUSINESSNAME).equals("null")||jsonObject.string(ReferralCodeModel.RFBUSINESSNAME).equals("")||jsonObject.string(ReferralCodeModel.RFBUSINESSNAME).equals(null))){
            lsprofileimage=jsonObject.string(ReferralCodeModel.RFPROFILEIMAGE)
        }else{
            lsprofileimage=null
        }


        //    lsreviewerprofileimage = jsonObject.string(ReviewModel.REVIEWERPROFILIMAGE)
        // lirating = jsonObject.float(ReviewModel.RATING)


    }
}