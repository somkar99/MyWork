package com.example.admin.h2hpartner.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.int
import com.beust.klaxon.string
import com.example.admin.h2hpartner.Services.StaticRefs

/**
 * Created by nehagulati on 2/21/18.
 */
class UploadImageModel {

    companion object {
        val IMAGEID = StaticRefs.IMAGEID
        val IMAGESERNO = StaticRefs.IMAGESERNO
        val IMAGEVENDERID = StaticRefs.IMAGEVENDERID
        val IMAGEPROVIDERBUSINESSID = StaticRefs.IMAGEPROVIDERBUSINESSID
        val IMAGETYPE = StaticRefs.IMAGETYPE
        val IMAGENAME = StaticRefs.IMAGENAME
        val IMAGE = StaticRefs.IMAGE
        val IMAGEISACTIVE = StaticRefs.IMAGEISACTIVE
        val IMAGECREATEDBY = StaticRefs.IMAGECREATEDBY
        val IMAGEUPDATEDBY = StaticRefs.IMAGEUPDATEDBY
    }

    var image_id: Int? = null
    var vendor_id: Int? = null
    var lsvendor_id: String? = null
    var image_name: String? = null
    var image: String? = null
    var imagesrno: Int? = null
    var lsimagesrno: String? = null

    constructor(jsonObject: JsonObject) {

        lsvendor_id = jsonObject.string(IMAGEVENDERID)
        image_name = jsonObject.string(IMAGENAME)
        image = jsonObject.string(IMAGE)
        // lsimagesrno=jsonObject.string(IMAGESERNO)

    }

    constructor(image_name: String?) {
        this.image_name = image_name
    }


}