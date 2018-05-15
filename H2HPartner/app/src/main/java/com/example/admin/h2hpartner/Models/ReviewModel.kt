package com.example.admin.h2hpartner.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.double
import com.beust.klaxon.float
import com.beust.klaxon.string
import com.example.admin.h2hpartner.Services.StaticRefs

/**
 * Created by admin on 11-04-2018.
 */
class ReviewModel {


    companion object {
        val REVIEW=StaticRefs.REVIEW
        val REVIEWDATE=StaticRefs.REVIEWDATE
        val RATING=StaticRefs.RATING
        val REVIEWBY=StaticRefs.REVIEWBY
        val REVIEWSERVICENAME=StaticRefs.REVIEWSERVICENAME
        val REVIEWSERVICETYPENAME=StaticRefs.REVIEWSERVICETYPENAME
        val REVIEWERPROFILIMAGE=StaticRefs.REVIEWERPROFILIMAGE
    }
    var lsreview:String?=""
    var lsreviewdate:String?=""
    var lsrating:String?=""
    var lsreviewby:String?=""
    var lsservicename:String?=""
    var lsservicetype:String?=""
    var lsreviewerprofileimage:String?=""
    var lirating:Float?=0f

    constructor(jsonObject: JsonObject) {
        lsreview = jsonObject.string(ReviewModel.REVIEW)
        lsreviewdate = jsonObject.string(ReviewModel.REVIEWDATE)
        lsrating = jsonObject.string(ReviewModel.RATING)
        lsreviewby = jsonObject.string(ReviewModel.REVIEWBY)
        lsservicename = jsonObject.string(ReviewModel.REVIEWSERVICENAME)
        lsservicetype = jsonObject.string(ReviewModel.REVIEWSERVICETYPENAME)

        //    lsreviewerprofileimage = jsonObject.string(ReviewModel.REVIEWERPROFILIMAGE)
       // lirating = jsonObject.float(ReviewModel.RATING)


    }

}