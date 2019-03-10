package com.sports.analyst.sportsanalyst.utils

import android.support.v7.widget.RecyclerView

class AdapterActionsBinder {

    private val actionsMap = HashMap<Class<*>, ArrayList<AdapterAction<*, *>>>()

    fun <M : Any, H : RecyclerView.ViewHolder> addActionForModel(action: AdapterAction<M, H>, modelClass: Class<M>) {
        if (actionsMap.containsKey(modelClass)) {
            val actions = actionsMap[modelClass]
            actions?.add(action)
        } else {
            val actions = ArrayList<AdapterAction<*, *>>()
            actions.add(action)
            actionsMap.put(modelClass, actions)
        }
    }

    fun <M : Any, H : RecyclerView.ViewHolder> bindActions(model: M, viewHolder: H) {

        actionsMap[model.javaClass]?.forEach {
            val action: AdapterAction<M, H> = it as AdapterAction<M, H>
            action.bind(model, viewHolder)
        }
    }
}