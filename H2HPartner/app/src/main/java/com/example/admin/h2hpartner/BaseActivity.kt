package com.example.admin.h2hpartner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import com.example.admin.h2hpartner.Services.StaticRefs
import com.example.admin.h2hpartner.UI.*
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.base_layout.*
import kotlinx.android.synthetic.main.footer.*

open class BaseActivity : Activity(), View.OnClickListener {

    open lateinit var context: Context
    open lateinit var activity: Activity
    var baseLayout: LinearLayout? = null
    var llfooter: LinearLayout? = null
    var llheading: LinearLayout? = null
    internal var tvTitle: TextView? = null
    internal var ivSearch: ImageView? = null
    internal var ivActivityTitle: TextView? = null
    internal var ivClose: ImageView? = null
    internal var mDrawer: DrawerLayout? = null
    internal var leftMenu: LinearLayout? = null
    internal var llDrawerLogout: LinearLayout? = null
    internal var llDrawerMyReport: LinearLayout? = null
    internal var llDrawerMyBookings: LinearLayout? = null
    internal var llDrawerPrescribtion: LinearLayout? = null
    internal var icon: ImageView? = null
    internal var intent: Intent? = null
    internal var etSearch: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.setContentView(R.layout.base_layout)

        context = this
        activity = this

        baseLayout = findViewById<View>(R.id.baseLayout) as LinearLayout
        llfooter = findViewById<View>(R.id.llfooter) as LinearLayout
        llheading = findViewById<View>(R.id.app_heading) as LinearLayout
        ivSearch = findViewById<View>(R.id.ivMyCases) as ImageView
        icon = findViewById<View>(R.id.ivMenu) as ImageView
        leftMenu = findViewById<View>(R.id.leftMenu) as LinearLayout
        ivActivityTitle = findViewById<View>(R.id.tvActivityTitle) as TextView
        llDrawerLogout = findViewById<View>(R.id.llDrawerLogout) as LinearLayout
        mDrawer = findViewById<View>(R.id.drawer) as DrawerLayout

        ivbackPressed!!.visibility = View.GONE
        llDrawerLogout!!.setOnClickListener(this)
        llDrawerSettings.setOnClickListener(this)
        llDrawerAboutus.setOnClickListener(this)
        llDrawerMyCases.setOnClickListener(this)
        llDrawerReferfriend.setOnClickListener(this)
        llDrawerwritetous.setOnClickListener(this)
        llDrawerReferfriend.setOnClickListener(this)
        llDrawerTnC.setOnClickListener(this)
        llDrawerRateApp.setOnClickListener(this)

        tvDrawerAboutus!!.setOnClickListener(this)
        ivDrawerAboutus!!.setOnClickListener(this)





        ivMenu!!.setOnClickListener(this)
        ivbackPressed!!.setOnClickListener(this)

        ivHome!!.setOnClickListener(this)
        tvHome!!.setOnClickListener(this)
        ivLeads!!.setOnClickListener(this)
        tvLeads!!.setOnClickListener(this)
        ivMyCases!!.setOnClickListener(this)
        tvMyCases!!.setOnClickListener(this)
        tvFAQ!!.setOnClickListener(this)
        ivFAQ!!.setOnClickListener(this)
        ivNotifications!!.setOnClickListener(this)

    }

    fun closeDrawer() {
        if (mDrawer != null) {
            mDrawer!!.closeDrawers()
        }
    }


    override fun setContentView(id: Int) {
        val inflater = baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(id, baseLayout)
    }

    override fun setTitle(ls_title: CharSequence) {
        icon!!.visibility = View.GONE
        // ivActivityTitle!!.visibility = View.GONE
        ivbackPressed!!.visibility = View.VISIBLE
        ivActivityTitle!!.visibility = View.VISIBLE
        ivActivityTitle!!.text = ls_title
    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.ivMenu -> if (mDrawer!!.isDrawerOpen(Gravity.LEFT)) {
                closeDrawer()
            } else {
                mDrawer!!.openDrawer(leftMenu)
                //setupMenu();
            }

            R.id.ivbackPressed -> {
                //toast("back clicked")
                finish()
            }
            R.id.llDrawerReferfriend -> {
                try {
                    val j = Intent(Intent.ACTION_SEND)
                    j.type = "text/plain"
                    j.putExtra(Intent.EXTRA_SUBJECT, "H2H Partner")
                    var sAux = "\n Best Application to book Home Services Refer My Code '" + prefs.referralcode + "' and  put While Registering on App\n"
                    sAux = sAux + "http://weapplify.tech \n"
                    j.putExtra(Intent.EXTRA_TEXT, sAux)
                    startActivity(Intent.createChooser(j, "choose one"))
                } catch (e: Exception) {
//e.toString();
                }
                // TastyToast.makeText(context," feature will be implemented shortly..", Toast.LENGTH_SHORT, TastyToast.INFO).show()

            }
            R.id.llDrawerRegisterAsPartner -> {
                closeDrawer()
                TastyToast.makeText(context, " feature will be implemented shortly..", Toast.LENGTH_SHORT, TastyToast.INFO).show()

            }
            R.id.llDrawerRateApp -> {
                closeDrawer()
                TastyToast.makeText(context, " feature will be implemented shortly..", Toast.LENGTH_SHORT, TastyToast.INFO).show()

            }
            R.id.llDrawerMyCases -> {
                closeDrawer()
                val intent = Intent(activity, MyCases::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }

            R.id.llDrawerSettings -> {
                closeDrawer()
                val intent = Intent(activity, Setting::class.java)
                intent.putExtra(StaticRefs.FROMSETTING, StaticRefs.YES)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)


            }

            R.id.llDrawerLogout -> {
                prefs.logout()
                val intent = Intent(activity, Login::class.java)
                startActivity(intent)
                finish()
            }
            R.id.llDrawerAboutus, R.id.ivDrawerAboutus, R.id.tvDrawerAboutus -> {
                closeDrawer()
                TastyToast.makeText(context, " feature will be implemented shortly..", Toast.LENGTH_SHORT, TastyToast.INFO).show()

            }
            R.id.llDrawerwritetous -> {
                closeDrawer()
                var email = "harsha.gulati@weapplify.tech"
                var subject = "Vendor id :${prefs.vendorid}, Name: ${prefs.first_name} ${prefs.last_name}"
                var body = "Hi Help2Help \\n\\n"
                var chooserTitle = "Send mail using..."

                val uri = Uri.parse("mailto:$email")
                        .buildUpon()
                        .appendQueryParameter("subject", subject)
                        .appendQueryParameter("body", body)
                        .build()

                val emailIntent = Intent(Intent.ACTION_SENDTO, uri)
                startActivity(Intent.createChooser(emailIntent, chooserTitle))
            }

            R.id.llDrawerTnC -> {
                closeDrawer()
                TastyToast.makeText(context, " feature will be implemented shortly..", Toast.LENGTH_SHORT, TastyToast.INFO).show()

            }

            R.id.ivHome, R.id.tvHome -> {
                closeDrawer()
                if (prefs.profilestatus.equals(StaticRefs.APPROVED)) {
                    val intent = Intent(this, ApprovedVendorHome::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    finish()
                } else {

                    val intent = Intent(this, Home::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    finish()
                }
            }
            R.id.ivLeads, R.id.tvLeads -> {
                closeDrawer()
                val intent = Intent(activity, NewLeads::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }
            R.id.ivMyCases, R.id.tvMyCases -> {
                closeDrawer()
                val intent = Intent(activity, MyCases::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }
            R.id.ivFAQ, R.id.tvFAQ -> {
                closeDrawer()
                val intent = Intent(activity, Faqs::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }

            R.id.ivNotifications ->     //Notifications From HEader onclick
            {
                closeDrawer()
                TastyToast.makeText(context, " feature will be implemented shortly..", Toast.LENGTH_SHORT, TastyToast.INFO).show()

            }

        }

    }

    fun hideFooter(value: Boolean?) {

        if (value == true) {
            llfooter!!.visibility = View.GONE
        }
    }
}



