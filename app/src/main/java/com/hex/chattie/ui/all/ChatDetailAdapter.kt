/*
package com.hex.chattie.ui.all

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hex.chattie.R
import com.hex.chattie.common.DiffCallBack
import com.hex.chattie.network.models.Message
import com.hex.chattie.network.models.Result
import kotlin.properties.Delegates
import jdk.javadoc.internal.doclets.toolkit.util.DocPath.parent

import android.R
import android.icu.text.Collator.ReorderCodes

import android.icu.text.Collator.ReorderCodes.OTHERS

class ChatDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), DiffCallBack
{
    var data : MutableList<Message> by Delegates.observable(ArrayList()) { _, old, new ->
        compareItem(old, new) { o, n -> o._id.toString() === n._id.toString() }
    }

    inner class ChatDetailViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        fun binData(message : Message)
        {

        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : RecyclerView.ViewHolder
    {
        var viewHolder : ChatDetailViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType)
        {
            1 ->
            {
                val v1 : View = inflater.inflate(com.hex.chattie.R.layout.item_chat_from_me, parent, false)
                viewHolder = ChatDetailViewHolder(v1)
            }

            2 ->
            {
                val v2 : View = inflater.inflate(com.hex.chattie.R.layout.item_chat_from_other, parent, false)
                viewHolder = ChatDetailViewHolder(v2)
            }

        }

        return viewHolder !!
    }

    override fun onBindViewHolder(holder : RecyclerView.ViewHolder, position : Int)
    {
        holder.
    }

    override fun getItemCount() : Int
    {
        return data.size
    }

}*/
