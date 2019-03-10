package com.sports.analyst.sportsanalyst.utils

import android.support.v7.widget.RecyclerView

interface AdapterAction<in M, in H : RecyclerView.ViewHolder> {
    fun bind(model: M, viewHolder: H)
}

class OnRootClickAdapterAction<in M, in H : RecyclerView.ViewHolder>(val onClickListener: (M)-> Unit) : AdapterAction<M, H> {
    override fun bind(model: M, viewHolder: H) {
        viewHolder.itemView.setOnClickListener { onClickListener.invoke(model) }
    }
}