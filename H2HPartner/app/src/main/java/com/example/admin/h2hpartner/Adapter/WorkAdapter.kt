package com.example.admin.h2hpartner.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.example.admin.h2hpartner.Models.UploadImageModel
import com.example.admin.h2hpartner.R

/**
 * Created by nehagulati on 2/22/18.
 */
class WorkAdapter(dataset: List<UploadImageModel>) : RecyclerView.Adapter<WorkAdapter.ViewHolder>() {

    val dataSet: List<UploadImageModel> = dataset
    final var onClick: (View) -> Unit = {}

    lateinit var context: Context
    lateinit var mItemClick: itemClickIterface

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val viewHolder: WorkAdapter.ViewHolder;
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.document_attached_row, parent, false)
        viewHolder = WorkAdapter.ViewHolder(view);
        view.setOnClickListener(View.OnClickListener {
            this.onClick(view)
            Toast.makeText(context, "Click Event", Toast.LENGTH_LONG).show()

        })

        context = parent.context;
        return WorkAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.let { holder ->
            holder.tvDocumentName.setText(dataSet[position].image_name)
            holder.tvDocumentName.setOnClickListener(View.OnClickListener {
                if (mItemClick != null) {
                    mItemClick.itemClick(position, "Show")
                }
            })

            holder.ivDeleteDoc.setOnClickListener(View.OnClickListener {
                if (mItemClick != null) {
                    mItemClick.itemClick(position, "Delete")
                }
            })


        }
    }

    interface itemClickIterface {
        fun itemClick(position: Int, lsValue: String)
    }

    fun setItemClickIterface(mItemListener: itemClickIterface) {
        mItemClick = mItemListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvDocumentName = view.findViewById<TextView>(R.id.tvDocName)
        val ivDeleteDoc = view.findViewById<ImageView>(R.id.ivDeleteDoc)

    }
}