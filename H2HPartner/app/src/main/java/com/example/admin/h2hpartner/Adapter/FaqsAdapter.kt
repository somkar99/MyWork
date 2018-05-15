package com.example.admin.h2hpartner.Adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.example.admin.h2hpartner.Models.FaqsModel
import com.example.admin.h2hpartner.R
import com.hendraanggrian.widget.ExpandableItem
import com.hendraanggrian.widget.ExpandableRecyclerView

/**
 * Created by ADMIN on 09-02-2018.
 */
class FaqsAdapter(layout: LinearLayoutManager,internal var dataSet : List<FaqsModel> ) :ExpandableRecyclerView.Adapter<FaqsAdapter.ViewHolder>(layout)
{
    private var context : Context? = null
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {

        val item: ExpandableItem = itemView.findViewById<ExpandableItem>(R.id.row) as ExpandableItem
        val tvQuestion = item.headerLayout.findViewById<TextView>(R.id.tvQuestion) as TextView
       /* val ivOpen = item.headerLayout.findViewById<ImageView>(R.id.ivOpen) as ImageView
        val ivClose = item.headerLayout.findViewById<ImageView>(R.id.ivClose) as ImageView*/
        val tvAnswer = item.contentLayout.findViewById<TextView>(R.id.tvAnswer) as TextView




    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        context = parent!!.context;
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.faq_new_row, parent, false))
    }



    override fun onBindViewHolder(holder: FaqsAdapter.ViewHolder?, position: Int) {
            super.onBindViewHolder(holder, position)
            holder!!.tvQuestion.setText(dataSet[position].lsquestion)
            holder.tvAnswer.setText(dataSet[position].lsanswer)



    }


    override fun getItemCount(): Int {

        return dataSet.size

    }





}