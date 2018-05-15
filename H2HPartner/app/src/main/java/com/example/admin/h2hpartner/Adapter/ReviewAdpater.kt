package com.example.admin.h2hpartner.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.example.admin.h2hpartner.Models.ReviewModel
import com.example.admin.h2hpartner.Models.UploadImageModel
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.Services.StaticRefs

/**
 * Created by admin on 11-04-2018.
 */
class ReviewAdpater(dataset:List<ReviewModel>):RecyclerView.Adapter<ReviewAdpater.ViewHolder>() {
    val dataSet: List<ReviewModel> = dataset
    final var onClick: (View) -> Unit = {}
    lateinit var context: Context
    lateinit var mItemClick: ReviewAdpater.itemClickIterface

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val viewholder:ViewHolder
        val view= LayoutInflater.from(parent!!.context).inflate(R.layout.show_review_row, parent, false)
        viewholder = ReviewAdpater.ViewHolder(view);
        view.setOnClickListener(View.OnClickListener {
            this.onClick(view)
            Toast.makeText(context, "Click Event", Toast.LENGTH_LONG).show()

        })

        context = parent.context;
        return viewholder

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.let{ holder->
            holder.tvreview.setText(dataSet[position].lsreview)

            val reviewdate=StaticRefs.getDate(dataSet[position].lsreviewdate.toString())
            holder.tvreviewdate.setText(reviewdate)
            holder.tvreviewername.setText(dataSet[position].lsreviewby.toString())
            var service:String?=""
            var servicetype:String?=""
            service=dataSet[position].lsservicename.toString()
            servicetype=dataSet[position].lsservicetype.toString()
            holder.tvreviewservice.setText(service+":"+servicetype).toString()
            var rating:Float
            rating=dataSet[position].lsrating!!.toFloat()
            var overallreview:String?=""
            if(rating==1f){
                overallreview="poor"

            }
            else if(rating<=2.0){
                overallreview="Satisfactory"

            }
            else if(rating<=3.0){
                overallreview="Good"

            }
            else if(rating<=4.0){
                overallreview="Best"

            }
            else if(rating<=5.0){
                overallreview="Excellent"

            }
            holder.tvsplreview.setText(overallreview).toString()
            holder.rbrating.rating= rating



        }

    }
    interface itemClickIterface {
        fun itemClick(position: Int, lsValue: String)
    }

    fun setItemClickIterface(mItemListener: itemClickIterface) {
        mItemClick = mItemListener
    }

    class ViewHolder(view:View):RecyclerView.ViewHolder(view) {

        val tvreviewername = view.findViewById<TextView>(R.id.tvReviewerName)
        val tvreviewdate = view.findViewById<TextView>(R.id.tvReviewDate)
        val tvsplreview = view.findViewById<TextView>(R.id.tvOverallReview)
        val tvreview = view.findViewById<TextView>(R.id.tvReview)
        val tvreviewservice = view.findViewById<TextView>(R.id.tvReviewService)

        // val ivprofileimage = view.findViewById<ImageView>(R.id.cvReviwerImage)
        val rbrating = view.findViewById<RatingBar>(R.id.rbRating)

    }
}