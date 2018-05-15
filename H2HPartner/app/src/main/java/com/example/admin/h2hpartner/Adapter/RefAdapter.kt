package com.example.admin.h2hpartner.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.admin.h2hpartner.Models.AddReferencesModel
import com.example.admin.h2hpartner.R


class RefAdapter(dataSet: List<AddReferencesModel>) : RecyclerView.Adapter<RefAdapter.ViewHolder>() {

    val dataSet: List<AddReferencesModel> = dataSet
    lateinit var mItemClick: itemClickIterface
    final var onClick: (View) -> Unit = {}
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val viewholder: RefAdapter.ViewHolder
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.reference_row, parent, false)
        viewholder = RefAdapter.ViewHolder(view)

        view.setOnClickListener(View.OnClickListener {
            this.onClick(view)
            Toast.makeText(context, "Click Event", Toast.LENGTH_LONG).show()
        })

        context = parent.context;

        return viewholder
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivdelete = view.findViewById<ImageView>(R.id.ivDeleteref)
        val tvname = view.findViewById<TextView>(R.id.tvRefName)
        val tvmobile = view.findViewById<TextView>(R.id.tvRefMobileno)

    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.let { holder ->
            holder.tvname.setText(dataSet[position].lscontactname.toString().trim())
            holder.tvmobile.setText(dataSet[position].lscontactno.toString().trim())

            /*    holder.ivedit.setOnClickListener(View.OnClickListener {
                    if (mItemClick != null)
                    {
                        mItemClick!!.itemClick(position,"EDIT")
                    }
                })*/

            holder.ivdelete.setOnClickListener(View.OnClickListener {
                if (mItemClick != null) {
                    mItemClick!!.itemClick(position)
                }
            })

        }
    }

    interface itemClickIterface {
        fun itemClick(position: Int)
    }

    fun setItemClickIterface(mItemListener: itemClickIterface) {
        mItemClick = mItemListener
    }

    override fun getItemCount(): Int {

        return dataSet.size
    }

}