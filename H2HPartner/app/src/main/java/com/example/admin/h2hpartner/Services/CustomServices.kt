package com.example.admin.h2hpartner.Services

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat.startActivity
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ADMIN on 08-02-2018.
 */
class CustomServices(context: Context) {
    internal var li_division: Int = 0

    init {
        CustomServices.context = context

    }

    companion object {

        lateinit var context: Context
        private val TAG = "Cresco_NF"

        fun AmountNF(ls_value: String?, ls_format: String,
                     lb_round: Boolean?): String {
            var ls_value = ls_value
            var ls_return = ""
            if (ls_value != null) {
                var ld_value = java.lang.Double.valueOf(ls_value)
                ls_value = ld_value.toString()
                var ll_value: Long = 0
                if (lb_round!!) {
                    ld_value = ld_value!! + 0.5
                    ld_value = Math.round(ld_value * 100).toDouble() / 100
                    ll_value = Math.round(ld_value * 100) / 100
                    ls_value = ll_value.toString()
                }

                ls_return = java.lang.Double.toString(ld_value!!)

                if (ls_format == "Rs") {
                    var dotPos = -1

                    for (i in 0 until ls_value.length) {
                        val c = ls_value[i]
                        if (c == '.') {
                            dotPos = i
                        }
                    }

                    if (dotPos == -1) {
                        ls_return = ls_value + ".00"
                    } else {
                        if (ls_value.length - dotPos == 1) {
                            ls_return = ls_value + "00"
                        } else if (ls_value.length - dotPos == 2) {
                            ls_return = ls_value + "0"
                        }
                    }
                }
            }

            return ls_return

        }


        fun setTypeFaceForViewGroup(vg: ViewGroup, mActivity: Activity) {

            for (i in 0 until vg.childCount) {
                Log.v(TAG, "In Loop")
                if (vg.getChildAt(i) is ViewGroup)
                    setTypeFaceForViewGroup(vg.getChildAt(i) as ViewGroup, mActivity)
                else if (vg.getChildAt(i) is TextView)
                    (vg.getChildAt(i) as TextView).typeface = Typeface
                            .createFromAsset(mActivity.assets,
                                    "fonts/Robot-Italic.ttf")

            }

        }


        val formattedDate: String
            get() {
                val sdf = SimpleDateFormat("dd")
                val date = Date()
                return sdf.format(date)
            }

        val todaysDate: String
            get() {
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val date = Date()
                return sdf.format(date)
            }

        fun convertToDouble(ls_amount: String?): Double? {
            var ls_amount = ls_amount
            var ld_amount: Double? = 0.0
            var isMinus = false

            if (ls_amount == null) {
                ls_amount = ""
            }
            if (ls_amount.indexOf("-") >= 0) {
                isMinus = true
                // Log.v(TAG,ls_amount);
            }

            if (ls_amount.indexOf(" ") > 0) {
                ls_amount = ls_amount.substring(ls_amount.indexOf(" "),
                        ls_amount.length)
            }
            ls_amount = ls_amount.replace(",", "").trim { it <= ' ' }

            try {
                ld_amount = java.lang.Double.parseDouble(ls_amount)
            } catch (nfe: NumberFormatException) {
                ld_amount = 0.00
            }

            if (isMinus) {
                if (ld_amount!! > 0) {
                    ld_amount = ld_amount!! * -1
                }
                // ld_amount = ld_amount * -1;
                // Log.v("Minus Amount","Neg Amt " +ld_amount);
            }
            // int li_amount = (int)(ld_amount * 10000);
            // ld_amount = (double) (li_amount / 10000);

            // Log.v(TAG,"Amount is "+ld_amount + " AND Round is "+roundedValue);
            return BigDecimal.valueOf(ld_amount!!).toDouble()
        }

        fun convertToLong(ls_amount: String?): Long? {
            var ls_amount = ls_amount
            val ld_amount = 0.0
            val isMinus = false

            if (ls_amount == null || ls_amount == "") {
                ls_amount = "0"
            }

            var roundedValue: Long = 0
            try {
                roundedValue = java.lang.Long.valueOf(ls_amount)!!
            } catch (e: Exception) {
                roundedValue = 0
            }

            // Log.v(TAG,"Amount is "+ld_amount + " AND Round is "+roundedValue);
            return roundedValue
        }

        fun getTStoDate(time: Long): Date {
            var time = time
            val cal = Calendar.getInstance()
            val tz = cal.timeZone//get your local time zone.
            val sdf = SimpleDateFormat("dd/MM")
            sdf.timeZone = tz//set time zone.
            time = time * 1000
            val localTime = sdf.format(Date(time))
            var date = Date()
            try {
                date = sdf.parse(localTime)//get local date

            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return date
        }

        fun getDate(time: Long): Date {
            val cal = Calendar.getInstance()
            val tz = cal.timeZone//get your local time zone.
            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a")
            sdf.timeZone = tz//set time zone.
            val localTime = sdf.format(time * 1000)
            var date = Date()
            try {
                date = sdf.parse(localTime)//get local date
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return date
        }


        fun showKB(mActivity: Activity) {
            mActivity.window.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }

        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }

        fun hideKB(mActivity: Activity) {
            mActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }

        fun showKB(mDialog: Dialog) {
            mDialog.window!!.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }

        fun hideKB(mDialog: Dialog) {
            mDialog.window!!.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }

        fun showKB(context: Context) {
            val imm = context
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(0, 0)

        }

        fun getAmount(ls_amount: String?): BigDecimal {
            var ls_amount = ls_amount
            if (ls_amount == null || ls_amount.length <= 0) {
                ls_amount = "0.00"
            }

            var isMinus = false

            if (ls_amount.indexOf("-") >= 0) {
                isMinus = true
                Log.v(TAG, ls_amount)
            }

            if (ls_amount.indexOf("(") >= 0) {
                isMinus = true
                ls_amount = ls_amount.replace(")", "").trim { it <= ' ' }
                ls_amount = ls_amount.replace("(", "").trim { it <= ' ' }

            }

            if (ls_amount.indexOf(" ") > 0) {
                ls_amount = ls_amount.substring(ls_amount.indexOf(" "), ls_amount.length)
            }
            ls_amount = ls_amount.replace(",", "").trim { it <= ' ' }
            var bd_amount: BigDecimal
            try {
                bd_amount = BigDecimal(ls_amount)
            } catch (nfe: NumberFormatException) {
                Log.v(TAG, nfe.toString())
                bd_amount = BigDecimal("0.00")
            }

            if (isMinus) {
                if (bd_amount.toFloat() > 0) {
                    bd_amount = bd_amount.abs()
                }

            }
            // bd_amount = bd_amount.
            Log.v(TAG, "Amount " + bd_amount)
            return bd_amount
        }

        fun getOpBalance(ls_amount: String): Double? {
            var ls_amount = ls_amount
            var ld_amount: Double? = 0.0
            var isMinus = false

            if (ls_amount.indexOf("-") >= 0) {
                isMinus = true
                // Log.v(TAG,ls_amount);
            }

            if (ls_amount.indexOf(" ") > 0) {
                ls_amount = ls_amount.substring(ls_amount.indexOf(" "),
                        ls_amount.length)
            }
            ls_amount = ls_amount.replace(",", "").trim { it <= ' ' }

            try {
                ld_amount = java.lang.Double.parseDouble(ls_amount)
            } catch (nfe: NumberFormatException) {
                ld_amount = 0.00
            }

            if (isMinus) {
                if (ld_amount!! > 0) {
                    ld_amount = ld_amount!! * -1
                }
                // ld_amount = ld_amount * -1;
                // Log.v("Minus Amount","Neg Amt " +ld_amount);
            }
            // int li_amount = (int)(ld_amount * 10000);
            // ld_amount = (double) (li_amount / 10000);

            // Log.v(TAG,"Amount is "+ld_amount + " AND Round is "+roundedValue);
            return BigDecimal.valueOf(ld_amount!!).toDouble()
        }


        fun getJSONData(json: JSONObject, objectName: String): String? {
            var ls_data: String? = null
            try {
                ls_data = json.getString(objectName)
            } catch (e: Exception) {
                Log.v(TAG, "Exception in getJSONDATA " + e.toString())
                ls_data = null
            }

            return ls_data
        }

        fun convertDouble(ls_string: String): Double {
            var ld_value = 0.00
            try {
                ld_value = java.lang.Double.valueOf(ls_string)!!
            } catch (e: Exception) {
                Log.v(TAG, "Exception in getDouble of FileType " + e.toString())
            }

            return ld_value
        }

        fun NumberFormat(value: Double, dec: Int, format: String): String {
            var retValue = ""
            try {
                val df = DecimalFormat(format)
                if (dec > 0) {
                    df.minimumFractionDigits = dec
                    val formatter = java.text.NumberFormat.getCurrencyInstance(Locale("en", "IN"))
                    retValue = formatter.format(value)
                } else {
                    retValue = df.format(value)
                }


            } catch (e: Exception) {
                Log.v(TAG, "Exception in NumberFormat " + e.toString())
                retValue = ""
            }

            return retValue
        }

        fun getCoordinates(context: Context, xPerc: Double, yPerc: Double): Bundle {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            var width = size.x
            var height = size.y
            if (xPerc > 0 && xPerc < 100) {
                width = (width * xPerc).toInt()
            }
            if (yPerc > 0) {
                height = (height * yPerc).toInt()
            }
            val b = Bundle()
            b.putInt("WIDTH", width)
            b.putInt("HEIGHT", height)
            return b
        }


        fun round(value: Double, places: Int): Double {
            if (places < 0) throw IllegalArgumentException()

            var bd = BigDecimal(value)
            bd = bd.setScale(places, RoundingMode.HALF_UP)
            return bd.toDouble()
        }

        fun padString(str: String, length: Int): String {
            var str = str
            if (str.length < length) {
                for (i in 0..length)
                    str += " "
            } else {
                str = str.substring(0, length)
            }
            return str
        }

        fun getScreenDetails(mActivity: Activity): Bundle {
            val bundle = Bundle()
            val rect = Point()
            mActivity.windowManager.defaultDisplay.getSize(rect)
            bundle.putInt("WIDTH", rect.x)
            bundle.putInt("HEIGHT", rect.y)
            return bundle

        }

        @SuppressLint("MissingPermission")
        fun isConnectionAvailable(context: Context): Boolean {
            val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                val info = connectivity.allNetworkInfo
                if (info != null)
                    for (i in info.indices)
                        if (info[i].state == NetworkInfo.State.CONNECTED) {
                            return true
                        }

            }
            return false
        }

        fun TimeValidator(time1: String, time2: String): Boolean {
            var b = false

            val sdf = SimpleDateFormat("HH:mm")

            try {
                val ArrTime = sdf.parse(time1)
                val DepTime = sdf.parse(time2)

                // Function to check whether a time is after an another time
                if (DepTime.after(ArrTime)) {
                    b = true
                }

            } catch (e: ParseException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            return b
        }


        fun getStringResourceByName(context: Context, aString: String): String {
            val packageName = context.packageName
            val resId = context.resources.getIdentifier(aString, "string", packageName)
            return context.getString(resId)
        }


        fun getColorByName(context: Context, aColor: String): Int {
            val packageName = context.packageName
            val resId = context.resources.getIdentifier(aColor, "color", packageName)
            return context.resources.getColor(resId)
        }

        fun ReadPdfs(activity: Activity, lsFile: String) {

            var file: File? = null

            file = File(Environment.getExternalStorageDirectory().toString() + "/TMF/pdfs/" + lsFile)


            if (file.exists()) {
                val target = Intent(Intent.ACTION_VIEW)

                target.setDataAndType(Uri.fromFile(file), "application/pdf")

                target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

                val intent = Intent.createChooser(target, "Open File")

                try {
                    activity.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    // Instruct the user to install a PDF reader here, or something
                }

            } else
                Toast.makeText(context.applicationContext, "File path is Invalid.", Toast.LENGTH_LONG).show()
        }

        /*fun copyImage(lsImageName: String) {
            val lsDir = App.getImageDir()
            val file = File(lsDir)
            if (!file.exists()) {
                val lbDirCreated = file.mkdirs()
            }
            val tempFile = File(lsImageName)
            val lsToFile = lsDir + "/" + tempFile.name

            try {
                FileUtils.copyFile(lsImageName.toString(), lsToFile)
            } catch (e: Exception) {

                Debugger.debug(TAG, "DB Restore error " + e.toString())
            }

        }*/


        fun setDate(textView: TextView, context: Context) {

            val cal = Calendar.getInstance()

            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                textView.text = SimpleDateFormat("dd/MM/yyyy").format(cal.time)

            }

            textView.setOnClickListener {
                DatePickerDialog(context, dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        }

        fun setTime(textView: TextView, context: Context) {

            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                textView.text = SimpleDateFormat("HH:mm").format(cal.time)
            }

            textView.setOnClickListener {
                TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }
        }

        fun ConverttoBase64(bmp: Bitmap): String {
            val baos: ByteArrayOutputStream
            baos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val bytearrayimage = baos.toByteArray()
            val encodedString = Base64.encodeToString(bytearrayimage, android.util.Base64.NO_WRAP);


            return encodedString
        }



    }
}
