package com.GoMobeil.H2H.Extensions

/**
 * Created by niranjanshah on 30/01/18.
 */
import android.support.v7.widget.RecyclerView
import android.view.View

interface OnItemClickListener {
    fun onItemClicked(view: View, position: Int)
}

fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
    this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewDetachedFromWindow(view: View?) {
            view?.setOnClickListener(null)
        }

        override fun onChildViewAttachedToWindow(view: View?) {
            view?.setOnClickListener({
                val holder = getChildViewHolder(view)
                onClickListener.onItemClicked(view, holder.adapterPosition)
            })
        }
    })
}
