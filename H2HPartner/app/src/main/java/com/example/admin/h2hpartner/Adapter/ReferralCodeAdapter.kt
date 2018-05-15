package com.example.admin.h2hpartner.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.example.admin.h2hpartner.Models.ReferralCodeModel
import com.example.admin.h2hpartner.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by admin on 17-04-2018.
 */
class ReferralCodeAdapter(dataset:List<ReferralCodeModel>): RecyclerView.Adapter<ReferralCodeAdapter.ViewHolder>() {
    val dataSet: List<ReferralCodeModel> = dataset
    final var onClick: (View) -> Unit = {}
    lateinit var context: Context
    lateinit var mItemClick: ReferralCodeAdapter.itemClickIterface

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder{
        val viewholder: ReferralCodeAdapter.ViewHolder
        val view= LayoutInflater.from(parent!!.context).inflate(R.layout.referral_row_new, parent, false)
        viewholder = ReferralCodeAdapter.ViewHolder(view);
        view.setOnClickListener(View.OnClickListener {
            this.onClick(view)
           // Toast.makeText(context, "Click Event", Toast.LENGTH_LONG).show()

        })

        context = parent.context;
        return viewholder
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.let {holder ->
            holder.tvrfName.setText(dataSet[position].lsfirstname+" "+dataSet[position].lslastname)
            if(dataSet[position].lsregisteras.equals("V")){
              //  holder.tvrfRegisterAs.setText("Service Provider")
                holder.llrfrowlayout.setBackgroundResource(R.color.greylight)
                holder.tvrfBusinessName.setText(dataSet[position].lsbusinessname)

            }
            else if(dataSet[position].lsregisteras.equals("C")){
                holder.llrfrowlayout.setBackgroundResource(R.color.white)
               // holder.tvrfRegisterAs.setText("Customer")
                holder.tvrfBusinessName.visibility=View.GONE
                holder.tvrfblank.visibility=View.VISIBLE

                //  holder.llrfbusinesslayout.visibility=View.GONE
            }
            if(!dataSet[position].lsprofileimage.equals(null)){
                holder.ivrfProfileimage.loadBase64Image(dataSet[position].lsprofileimage)
            }


            holder.tvrfRegisterOn.setText(getDate(dataSet[position].lsregisteron!!))
        }

    }
    interface itemClickIterface {
        fun itemClick(position: Int, lsValue: String)
    }

    fun setItemClickIterface(mItemListener: itemClickIterface) {
        mItemClick = mItemListener
    }
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {

        val tvrfName = view.findViewById<TextView>(R.id.tvRfName)
      //  val tvrfRegisterAs = view.findViewById<TextView>(R.id.tvRfRegisterAs)
        val tvrfRegisterOn = view.findViewById<TextView>(R.id.tvRfRegisterOn)
        val tvrfBusinessName = view.findViewById<TextView>(R.id.tvRfBusinessName)
        val tvrfblank = view.findViewById<TextView>(R.id.tvRfBlank)

        val ivrfProfileimage = view.findViewById<ImageView>(R.id.ivRfProfileImage)
        val llrfrowlayout = view.findViewById<LinearLayout>(R.id.llRfRow)
    }
    private fun getDate(timeStampStr: String): String {

        val str = timeStampStr
        val fmt = "yyyy-MM-dd HH:mm:ss"
        val df = SimpleDateFormat(fmt)

        val date = Date()
        val date1 = df.format(date)

        val dt = df.parse(str)
        val cur2 = df.parse(date1)

        val tdf = SimpleDateFormat("HH:mm")
        val dfmt = SimpleDateFormat("HH:mm dd/MM/yyyy")
        val dtonly = SimpleDateFormat("dd/MM/yyyy")

        val timeOnly = tdf.format(dt)
        val date_only = dtonly.format(dt)
        val dateOnly = dfmt.format(dt)
        val cur4 = dfmt.format(cur2)

        val Diff = getDateDiff(dfmt, dateOnly, cur4)

        if (Diff.toInt() == 0) {
            return "Today, " +timeOnly
        } else if (Diff.toInt() > 0 && Diff.toInt() < 7) {
            return Diff.toString() +" Day Ago"
        } else {
            return date_only
        }

    }

    fun getDateDiff(format: SimpleDateFormat, oldDate: String, newDate: String): kotlin.Long {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).time - format.parse(oldDate).time, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }
}