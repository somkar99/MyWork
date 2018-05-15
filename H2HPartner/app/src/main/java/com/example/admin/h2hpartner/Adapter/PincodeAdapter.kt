package com.example.admin.h2hpartner.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.example.admin.h2hpartner.Models.PinCodeModel
import com.example.admin.h2hpartner.R

/**
 * Created by nehagulati on 2/24/18.
 */
class PincodeAdapter(dataSet: MutableList<PinCodeModel>) : RecyclerView.Adapter<PincodeAdapter.ViewHolder>() {

    val dataSet: MutableList<PinCodeModel> = dataSet
    final var onClick: (View) -> Unit = {}

    internal lateinit var mItemClick: itemClickIterface


    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val viewholder: PincodeAdapter.ViewHolder
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.area_row, parent, false)
        viewholder = PincodeAdapter.ViewHolder(view)
        return viewholder
    }

    override fun getItemCount(): Int {

        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder?.let { holder ->
            holder.ivCheck.setOnClickListener(View.OnClickListener {
                if (mItemClick != null) {
                    mItemClick.itemClick(position, "EDIT")
                }
            })

            holder.ivUnCheck.setOnClickListener(View.OnClickListener {
                if (mItemClick != null) {
                    mItemClick.itemClick(position, "DELETE")
                }
            })
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ivCheck = view.findViewById<ImageView>(R.id.ivCheck)
        val ivUnCheck = view.findViewById<ImageView>(R.id.ivUnCheck)
        val tvArea = view.findViewById<TextView>(R.id.tvArea)

    }


    interface itemClickIterface {
        fun itemClick(position: Int, view: String)
    }

    fun setItemClickInterface(mItemListener: PincodeAdapter.itemClickIterface) {
        mItemClick = mItemListener
    }
}