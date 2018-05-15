package com.example.admin.h2hpartner.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.admin.h2hpartner.Model.NewLeads_Model
import com.example.admin.h2hpartner.R
import com.sdsmdg.tastytoast.TastyToast
import java.lang.Long
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class NewLeads_Adapter(dataSet: List<NewLeads_Model>) : RecyclerView.Adapter<NewLeads_Adapter.ViewHolder>() {
    val dataSet: List<NewLeads_Model> = dataSet
    //  var onClick: (View) -> Unit = {}
    lateinit var context: Context
    var lsFullname:String?=null
    var lsLocation:String?=null
    var lsService:String?=null

    companion object {
        lateinit var adapter: NewLeads_Adapter
        lateinit var mItemClick:onItemClickListener
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvName = view.findViewById<TextView>(R.id.tvCustomerName)
        val tvLocation = view.findViewById<TextView>(R.id.tvServiceLocation)
        val tvService = view.findViewById<TextView>(R.id.tvServiceAsked)
        val tvDistance=view.findViewById<TextView>(R.id.tvNewDistance)

        val tvReceivedDate=view.findViewById<TextView>(R.id.tvReceivedDate)
        val ivCall = view.findViewById<ImageView>(R.id.ivCustomerCall)
        val ivMessage = view.findViewById<ImageView>(R.id.ivCustomerMessage)
        val llLeadDetails=view.findViewById<LinearLayout>(R.id.llLeadsDetails)
/*
      val tvView = view.findViewById<TextView>(R.id.tvView)
*/
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.new_leads_row_1, parent, false)
        context = parent.context;
        return ViewHolder(view)
        adapter = this
    }


    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.let { holder ->

            val enableMobile = dataSet.get(position).sr_sharemobileno
            val enableMessage = dataSet.get(position).sr_enablemessaging

            if(!enableMobile.equals("Y")||enableMobile.equals(null)||enableMobile.equals("null")){

               // holder.ivCall.setColorFilter(R.color.light_grey,android.graphics.PorterDuff.Mode.MULTIPLY);
                holder.ivCall.setImageResource(R.mipmap.call_dis)
                holder.ivCall.isClickable=false
            }
            else{
                holder.ivCall.isClickable=true
                holder.ivCall.setImageResource(R.mipmap.callicon)
            }
            if(!enableMessage.equals("Y")||enableMessage.equals(null)||enableMessage.equals("null"))
            {
                holder.ivMessage.setImageResource(R.mipmap.msg_dis)
               // holder.ivMessage.setColorFilter(R.color.light_grey,android.graphics.PorterDuff.Mode.MULTIPLY);
                holder.ivMessage.isClickable=false

            }else{
                holder.ivMessage.isClickable=true
                holder.ivMessage.setImageResource(R.mipmap.messageicon)

            }
            lsService = dataSet[position].lsservicename+"  "+dataSet[position].lsservicetypename
            lsLocation = dataSet[position].lsAdd_line1+", "+dataSet[position].lsAdd_line2+", " +dataSet[position].lsCity + ", "+dataSet[position].lsState+", "+dataSet[position].lsPincode
            lsFullname=dataSet[position].lscustomerfirstname+" "+dataSet[position].lscustomerlastname.toString()
            holder.tvName.text = lsFullname
            holder.tvLocation.text = lsLocation
            holder.tvService.text = lsService

            holder.tvDistance.text=dataSet[position].lsdistanceinkm+ "km"
            var timeStampStr = dataSet[position].lsreceiveddate
            holder.tvReceivedDate.text=getDate(timeStampStr!!)





            holder.ivCall.setOnClickListener(View.OnClickListener {

                if (mItemClick != null) {
                    mItemClick!!.onItemClick(position,holder.ivCall)
                }
            })
            holder.ivMessage.setOnClickListener(View.OnClickListener {

                if (mItemClick != null) {
                    mItemClick!!.onItemClick(position,holder.ivMessage)
                }
            })
            holder.llLeadDetails.setOnClickListener(View.OnClickListener {
                if (mItemClick != null) {
                    mItemClick!!.onItemClick(position,holder.llLeadDetails)
                }
            })
        }
    }

    fun setOnCardClickListener(mItemListener: onItemClickListener) {
        mItemClick = mItemListener
    }
    override fun getItemCount(): Int {
        return this.dataSet.size
    }
    interface onItemClickListener {
        fun onItemClick(position: Int,view: View)

    }
    // Send Date String in format = yyyy-MM-dd HH:mm:ss
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





