package com.example.admin.h2hpartner.Adapter

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.admin.h2hpartner.Models.Pricing_Model
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.R.id.ivDelete
import com.example.admin.h2hpartner.R.id.ivDelete1

class Pricing_Adapter(dataSet: List<Pricing_Model>) : RecyclerView.Adapter<Pricing_Adapter.ViewHolder>() {
    val dataSet: List<Pricing_Model> = dataSet
    lateinit var context: Context

    companion object {
        lateinit var adapter: Pricing_Adapter
        lateinit var mItemClickListener: onItemClickListener
        lateinit var mItemClickListener1: onItemClickListener
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        internal val ivDelete: ImageView
        internal val ivEdit: ImageView
        val tvServiceName = view.findViewById<TextView>(R.id.tvServiceName)
        val tvPriceUnit = view.findViewById<TextView>(R.id.tvPriceUnit)
        val tvPriceType = view.findViewById<TextView>(R.id.tvPriceType)


        init {

            ivDelete = view.findViewById(R.id.ivDelete1)
            ivEdit = view.findViewById(R.id.ivEdit1)

            ivDelete.setOnClickListener(this)
            ivEdit.setOnClickListener(this)
        }


        override fun onClick(view: View) {
            when (view.id) {
                R.id.ivDelete1 -> {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(adapterPosition)
                    }
                }

                R.id.ivEdit1 -> {
                    mItemClickListener1.onItemClick(adapterPosition)

                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.price_row, parent, false)
        context = parent.context;
        return ViewHolder(view)
        adapter = this

    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.let { holder ->
            holder.tvPriceType.text = dataSet[position].price_type
            holder.tvServiceName.text = dataSet[position].srv_typedescription

            var lsPriceType = dataSet[position].price_unit

            if (lsPriceType.equals("Range")) {
                holder.tvPriceUnit.text = dataSet[position].price_unit + "   " + dataSet[position].pst_cost_from + " to " + dataSet[position].pst_cost_to
            } else if (lsPriceType.equals("Fixed")) {
                holder.tvPriceUnit.text = dataSet[position].price_unit + "  " + dataSet[position].pst_cost_fixed
            } else if (lsPriceType.equals("Rate")) {
                holder.tvPriceUnit.text = dataSet[position].price_unit + "  " + dataSet[position].pst_cost_rate
            } else if (lsPriceType.equals("On Inspection")) {
                holder.tvPriceType.text = dataSet[position].price_unit
            }
        }
    }

    override fun getItemCount(): Int {
        return this.dataSet.size
    }

    fun setOnCardClickListener(mListener: onItemClickListener) {
        mItemClickListener = mListener
    }

    fun setOnCardClickListener1(mListener: onItemClickListener) {
        mItemClickListener1 = mListener
    }


    interface onItemClickListener {
        fun onItemClick(position: Int)

    }

}


