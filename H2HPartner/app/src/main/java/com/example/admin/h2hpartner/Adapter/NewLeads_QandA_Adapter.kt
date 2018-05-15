package com.example.admin.h2hpartner.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.admin.h2hpartner.Model.NewLeads_Model
import com.example.admin.h2hpartner.Model.QueAnsModel
import com.example.admin.h2hpartner.R


class NewLeads_QandA_Adapter(dataSet: List<QueAnsModel>) : RecyclerView.Adapter<NewLeads_QandA_Adapter.ViewHolder>() {

    val dataSet: List<QueAnsModel> = dataSet
    lateinit var context: Context

    companion object {
        lateinit var adapter: NewLeads_QandA_Adapter
        lateinit var mItemClick: onItemClickListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvQuestion = view.findViewById<TextView>(R.id.tvQuestion)
        val tvAnswer = view.findViewById<TextView>(R.id.tvAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.que_ans_row, parent, false)
        context = parent.context;
        return ViewHolder(view)
        adapter = this
    }


    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.let { holder ->
            holder.tvQuestion.text = dataSet[position].sq_question
            holder.tvAnswer.text = dataSet[position].srd_answer
        }
    }

    fun setOnCardClickListener(mItemListener: onItemClickListener) {
        mItemClick = mItemListener
    }

    override fun getItemCount(): Int {
        return this.dataSet.size
    }

    interface onItemClickListener {
        fun onItemClick(position: Int, view: View)

    }

}

