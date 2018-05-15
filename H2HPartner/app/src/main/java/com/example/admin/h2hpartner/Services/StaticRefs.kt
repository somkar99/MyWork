package com.example.admin.h2hpartner.Services

import android.os.Environment
import ch.acra.acra.R
import java.text.SimpleDateFormat
import java.util.regex.Pattern

/**
 * Created by Admin on 07-02-18.
 */
class StaticRefs {

    companion object {

        var isvalidadharcard: Boolean = false
        var APPName = "H2HPartner"
        var TIMEOUT=30000
        var TIMEOUTREAD=60000
        var PROFILECOMPLITION=0

        //URLS
        val BASEURL = "http://help2help.weapplify.tech"
        val REGISTERURL = "/api/vendorregistration"
        val LoginURL = "/api/vendorlogin"
        val VENDORDETAILSURL = "/api/vendorprofile"
        val VENDOREDIT = "/api/vendoredit"
        val VENDORBUSINESSEDIT = "api/providerbusiness/edit"
        val VENDORBUSINESSSHOW = "api/providerbusiness/show"
        val VENDORREFERENCE = "api/providerreference/create"
        val VENDORREFERENCESHOW = "api/providerreference/show"
        val SERVICELIST = "api/services/searchbyname"
        val VENDORUPLOADIMAGES = "api/serviceproviderimage/create"
        val VENDORUPLOADIMAGESSHOW = "api/serviceproviderimage/show"
        val VENDORPROFILE = "api/vendorgetstatus"
        val VENDORPROFILEUPDATE = "api/vendorsetstatus"
        val CHANGEPASSOWRDURL = "/api/vendorchangepassword"
        val NEWLEADS = "/api/serviceproviderleads/details"
        val NEWLEADS_DETAILED = "/api/servicerequest/details"
        val UPDATETRASCTIONSTATUS = "/api/serviceproviderleads/changestatus"
        val SERVICES = "/api/services/show"
        val FAQSSHOW="/api/faqs/showvendor"
        val FEEDBACK="/api/vendors/feedback"
        val HOMEANALYTICS="/api/vendors/analytics"
        val CHATSENDMESSAGE="/api/txnchat/create"
        val CHATSHOW="/api/txnchat/show"
        val CONVERSATIONS="/api/txnchat/getconv"
        val REFERRALCODE="/api/vendors/referaldata"

        //ChangePassword
        val new_sp_password = "new_sp_password"
        val old_sp_password = "old_sp_password"

        //global keys
        val TOKEN = "token"
        val DEVICETOKEN="device_token"
        val STATUS = "status"
        var MESSAGE = "message"
        val FAILED = "Failed"
        val SUCCESS = "Success"
        val COMPLETE = "complete"
        val INCOMPLETE = "incomplete"
        val WAITINGFORAPPROVAL = "waiting for approval"
        val APPROVED = "approved"
        val REJECTED = "rejected"
        val PROFILEREJECTIONREASON = "rejection_reason"
        val DATA = "data"
        val DOCS = "docs"
        val CREATEDAT = "created_at"
        val UPDATEDAT = "updated_at"
        val USERID = "userid"
        val KEY = "key"
        val YES="yes"
        val FIRSTTIME_FLAG = "firsttimeflag"
        val REMEMBER_ME = "remember_me"
        val CHANGEPASSWORD = "changepassword"
        val FROMSETTING = "fromsetting"
        val PROFILESTATUS = "profile_status"
        val PERSONALINFOSTATUS = "personaldetails"
        val BUSINESSINFOSTATUS = "businessdetails"
        val REFERENCESINFOSTATUS = "references"
        val PRICINGINFOSTATUS = "costdetails"
        val WORKIMAGESINFOSTATUS = "workimages"
        val KYCINFOSTATUS = "kycimages"
        val ACCEPTED="accepted"
        val COMPLETED="completed"
        val AVERAGERATING="rating"
        val BASE64LINK="data:image/jpeg;base64,"


        //SERVICE_PROVIDERS
        val VENDORID = "sp_id"
        val CUST_ID = "cust_id"
        val USERNAME = "sp_username"
        val FIRSTNAME = "sp_firstname"
        val LASTNAME = "sp_lastname"
        val FULLNAME = "sp_fullname"
        val DOB = "sp_dob"
        val REFERBY="sp_referby"
        val VENDORDEVICEID="sp_deviceid"
        val VENDORREFRRALCODE="sp_referalcode"
        val GENDER = "sp_gender"
        val VENDORMOBILENO = "sp_mobileno"
        val MOB = "sp_mobile"
        val ALTMOBILENO = "sp_altmobileno"
        val LANDLINE = "sp_landlineno"
        val EMAIL = "sp_email"
        val PASSWORD = "sp_password"
        val LANGUAGEPREFERENCE = "sp_languagepreference"
        val LANGUAGE = "language"
        val CATEGORY = "sp_category"
        val AADHARNO = "sp_aadharcardno"
        val VENDORPRIMARYBUSINESS = "sp_primarybusiness"
        val VENDORIMAGE = "sp_image"
        val VENDORSTATUS = "sp_status"
        val VENDORISACTIVE = "sp_isactive"
        val VENDORCREATEDBY = "sp_createdby"
        val VENDORUPDATEDBY = "sp_updatedby"
        val APITOKEN = "api_token"
        val BUSINESSID = "sp_primarybusiness_id"
        val CREATEDDATE = "sp_primarybusiness_id"

        //ADDRESS
        val ADDID = "add_srno"
        val ENTITYTYPE = "add_entitytype"
        val ENTITYTYPEID = "add_entityid"
        val ADDRESSTYPE = "add_type"
        val ADDRESSLINE1 = "add_line1"
        val ADDRESSLINE2 = "add_line2"
        val CITY = "add_city"
        val STATE = "add_state"
        val PINCODE = "add_pincode"
        val ADDRESSISACTIVE = "add_isactive"
        val ADDRESSCREATEDBY = "add_createdby"
        val ADDRESSUPDATEDBY = "add_updatedby"
        val BUSINESSADD = "businessadd"
        val RESIDENTIALADD = "vendoradd"
        val ADDRESSLATITUDE = "add_latitude"
        val ADDRESSLONGITUDE = "add_longitude"


        //ProviderBusiness

        val PBVENDORID = "pb_spid"
        val PBSERVID = "pb_servId"
        val PBBUSINESSNAME = "pb_businessname"
        val PBINTRODUCTION = "pb_introduction"
        val PBUSP = "pb_usp"
        val PBESTABLISHON = "pb_establishon"
        val PBBUSINESSCONTACTNO = "pb_businesscontactno"
        val PBWEBURL = "pb_websiteurl"
        val PBSERVICEAREA = "pb_servicearea"
        val PBEXPERIENCE = "pb_experience"
        val PBUPDATEDBY = "pb_updatedby"
        val PBLOGO = "pb_logo"

        //Pricing Details
        val PRICEDETAILS = "/api/providerservicetypes/create"
        val PRICE_SHOW = "/api/providerservicetypes/show"
        val LOVS = "/api/lovs/show"
        val SERVICE_TYPE_LOV = "/api/servicetype/show"
        val LOV_TYPE = "lov_type"

        //Reference
        val PR_ID = "pr_id"
        val PR_PID = "pr_pid"
        val PR_SLNO = "pr_slno"
        val PR_REFERENCE = "pr_references"
        val PR_REFCONTACTNUMBER = "pr_refcontactnumber"
        val PR_CREATEDBY = "pr_createdBy"
        val PR_UPDATEDBY = "pr_updatedBy"
        val PR_ISACTIVE = "pr_isActive"

        //Service
        val SERVICEID = "srv_id"
        val SERVICENAME = "srv_name"
        val LOGINID = "loginid"


        //PRICEDETAILS
        val SERVICETYPE = "pst_serv_type"
        val SPID = "pst_spid"
        val SERV_ID = "pst_servid"
        val PRICEUNIT = "pst_cost_unit"
        val COST_TYPE = "pst_costtype"
        val PRICEFROM = "pst_cost_from"
        val PRICETO = "pst_cost_to"
        val FIX_PRICE = "pst_cost_fixed"
        val RATE = "pst_cost_rate"
        val VISITING_CHARGES = "pst_cost_visiting"
        val REMARK = "pst_cost_remarks"
        val ISACTIVE = "pst_isActive"
        val UPDATEDBY = "pst_updatedBy"
        val SRV_ID = "srv_id"
        val FAQSERV_ID="faq_servid"
        val SERVICETYPED = "srv_typedescription"

        //Provider images
        val IMAGEID = "spi_id"
        val IMAGESERNO = "spi_srno"
        val IMAGEVENDERID = "spi_spid"
        val IMAGEPROVIDERBUSINESSID = "spi_pbid"
        val IMAGETYPE = "spi_imagetype"
        val IMAGENAME = "spi_imagename"
        val IMAGE = "spi_image"
        val IMAGEISACTIVE = "spi_isactive"
        val IMAGECREATEDBY = "spi_createdby"
        val IMAGEUPDATEDBY = "spi_updatedby"

        val lsImageDirectory = Environment.getExternalStorageDirectory().toString() + "/" + APPName + "/Images"


        //New Leads List
        val CUSTOMERFIRSTNAME = "cust_firstname"
        val CUSTOMERLASTNAME = "cust_lastname"
        val SERVICENAME_LEADS = "srv_name"
        val SERVICETYPENAME = "srv_typedescription"
        val TRANSACTIONID = "spl_txnid"
        val RECEIVEDDATE = "created_at"
        val DISTANCEINKM = "spl_distanceinkm"
        val MOBILENO = "cust_mobileno"
        val LEAD_SERVICEPROVIDERID = "spl_spid"
        val SERV_PLANNERDATE = "sr_planneddate"
        val SERV_PLANNERTIME = "sr_plannedtime"
        val LEADSTATUS = "spl_status"
        val SR_ENABLEMESSAGING = "sr_enablemessaging"
        val SR_SHAREMOBILENO = "sr_sharemobileno"


        val REJECTIONREASON = "spl_rejectionreason"
        val QUESTION = "sq_question"
        val ANSWER = "srd_answer"
        val TRANSACTIONID1 = "sr_txnid"
        val QUESTION_ANSWER = "question_answers"

        //Reviews
        val REVIEW="sr_review"
        val TRANSACTIONVENDORID="sr_spid"
        val REVIEWDATE="sr_reviewdate"
        val REVIEWSERVICENAME="sr_service"
        val REVIEWSERVICETYPENAME="sr_servicetype"
        val RATING="sr_rating"
        val REVIEWBY="sr_custname"
        val REVIEWERID="sr_custid"
        val REVIEWERPROFILIMAGE="sp_image"

        //CHAT
        val CHAT_ID="tc_id"
        val CHATCUSTOMERID="tc_custid"
        val CHATVENDORID="tc_spid"
        val CHATTXNID="tc_txnid"
        val CHATSENDER="tc_sender"
        val CHATMESSAGE="tc_message"
        val CHAT_DATETIME="created_at"
        val CUST_NAME="cust_name"
        val SRV_NAME="srv_name"
        val CHAT_ENTITY_TYPE="entity_type"
        val CHAT_ENTITY_ID="entity_id"
        val CUST_PROFILEIMAGE="cust_profileimage"

        //Referrals
        val RFFIRSTNAME="firstname"
        val RFLASTNAME="lastname"
        val RFREGISTERON="created_at"
        val RFREGISTERAS="registered_as"
        val RFBUSINESSNAME="primary_business"
        val RFPROFILEIMAGE="profile_image"


        //Notification
        val NOTIFICATIONTYPE="notification_type"
        val NOTIFICATIONDATA="data"
        val NOTIFICATIONMESSAGE="message"
        val NOTIFICATIONNEWLEADS="new_lead"
        val NOTIFICATIONMYCASES="my_cases"

        //Notifciation_type=message
        val CUSTOMERNAME="cust_name"
        val VENDORNAME="sp_name"


        //REGISTRATION
        /*  val FNAME = "fname"
          val LNAME = "lname"*/
        // val EMAIL = "email"
        //val MOBILE = "mobile"
        //val PRIBUSINESS = "primarybusiness"
        // val PASSWORD = "password"
        //LOGIN
        //val VENDORID = "vendor_id"
        //DASHBOARD
        //Profileinfo
        //val LANDLINE = "landlineno"
        //val ALTMOBILENO = "altmobileno"
        //  val CATEGORY="category"
        //  val USERNAME="username"
        // val GENDER="gender"
        /* val ADDRESSLINE1="line1"
         val ADDRESSLINE2="line2"*/
        /* val CITY="city"
         val STATE="state"
         val PINCODE="pincode"*/
        // val TYPE="type"
        //  val UPDATEDBY="updatedby"


        fun isValidEmail(email: String): Boolean {
            val EMAIL_PATTERN = "^[_a-zA-Z0-9]+(\\.[_a-zA-Z0-9]+)*@" + "[a-z0-9]+(\\.[a-z0-9]+)*(\\.[a-z]{2,3})$"

            val pattern = Pattern.compile(EMAIL_PATTERN)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }

        fun isValidContact(contact: String): Boolean {
            val CONTACT_NAME = "[0-9]{10,11}"
            val pattern = Pattern.compile(CONTACT_NAME)
            val matcher = pattern.matcher(contact)
            return matcher.matches()
        }


        fun isValidUser(name: String): Boolean {
            val USER_NAME = "[a-zA-Z]{3,50}"
            val pattern = Pattern.compile(USER_NAME)
            val matcher = pattern.matcher(name)
            return matcher.matches()

        }

        fun isValidPassword(password: String): Boolean {
            val PASSWORD = "[a-zA-Z0-9\\.\\(\\)\\/\\,\\@,\\$,\\%,\\&,\\!]{6,18}"
            val pattern = Pattern.compile(PASSWORD)
            val matcher = pattern.matcher(password)
            return matcher.matches()

        }

        fun validateadhar(adharnumber: String): Boolean {

            val pattern = Pattern.compile("\\d{12}")
            isvalidadharcard = pattern.matcher(adharnumber).matches()

            /* if(isvalidadharcard){

                 isvalidadharcard=ValidateAdharcard.validateVerhoeff(adharnumber)
             }
 */
            return isvalidadharcard
        }
        fun getDate(timeStampStr: String):String {

            val str = timeStampStr
            val fmt = "yyyy-MM-dd HH:mm:ss"
            val df = SimpleDateFormat(fmt)

            val dt = df.parse(str)

            val tdf = SimpleDateFormat("HH:mm:ss")
            val dfmt = SimpleDateFormat("dd/MM/yyyy")

            val timeOnly = tdf.format(dt)
            val dateOnly = dfmt.format(dt)

            return dateOnly
        }


    }
}