package com.example.admin.h2hpartner.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.admin.h2hpartner.Models.ServiceModel
import com.example.admin.h2hpartner.R

/**
 * Created by Computer on 06-03-2018.
 */
class ServiceListAdapter(dataset: List<ServiceModel>) : RecyclerView.Adapter<ServiceListAdapter.ViewHolder>() {

    val dataSet: List<ServiceModel> = dataset
    final var onClick: (View) -> Unit = {}

    lateinit var context: Context

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.let { holder ->
            holder.tvservicename.setText(dataSet[position].lssrv_name)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {

        val viewholder: ViewHolder
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.service_row, parent, false)

        view.setOnClickListener(View.OnClickListener {
            this.onClick(view)
            Toast.makeText(context, "Click Event", Toast.LENGTH_LONG).show()

        })

        context = parent.context;
        viewholder = ViewHolder(view)
        return viewholder

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvservicename = view.findViewById<TextView>(R.id.tvservicename)


    }
}