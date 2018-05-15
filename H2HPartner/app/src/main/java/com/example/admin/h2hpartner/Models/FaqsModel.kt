package com.example.admin.h2hpartner.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.string

/**
 * Created by ADMIN on 09-02-2018.
 */
class FaqsModel
{
    companion object {
        val FAQ_SERVID = "faq_servid";
        val FAQ_QID = "faq_qid";
        val FAQ_SORTINGORDER = "faq_sortingorder"
        val FAQ_QUESTION = "faq_question"
        val FAQ_ANSWER = "faq_answer"
        val FAQ_CREATEDBY = "faq_createdBy"
        val FAQ_CREATEDDATETIME = "faq_createdDateTime"
        val FAQ_UPDATEDBY = "faq_updatedBy"
        val FAQ_UPDATEDDATETIME = "faq_updatedDateTime"
        val FAQ_ISACTIVE = "faq_isActive"
    }

    var lsquestion: String? = null;
    var lsanswer: String? = null;
    var lsfaq_qid:String?=null;


    constructor(jsonObject: JsonObject) {
        lsquestion = jsonObject.string(FAQ_QUESTION)
        lsanswer = jsonObject.string(FAQ_ANSWER)

    }
}