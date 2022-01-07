package com.hex.chattie.ui.channels

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hex.chattie.R
import com.hex.chattie.common.DiffCallBack
import com.hex.chattie.databinding.ItemChatBinding
import com.hex.chattie.delegate.ChatItemDelegate
import com.hex.chattie.network.models.Result
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class ChannelAdapter(private var mDelegate : ChatItemDelegate) : RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder>(), DiffCallBack
{
    var data : MutableList<Result> by Delegates.observable(ArrayList()) { _, old, new ->
        compareItem(old, new) { o, n -> o._id.toString() === n._id.toString() }
    }

    inner class ChannelViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        private lateinit var binding : ItemChatBinding
        @SuppressLint("SetTextI18n")
        fun bindData(result : Result)
        {
            binding = ItemChatBinding.bind(itemView)

            binding.apply {
                tvChannelName.text = result.name


                if (result.lastMessage != null)
                {
                    tvLastMessages.text = result.lastMessage.msg
                }
                else
                {
                    tvLastMessages.text = "No message"
                }
                tvTime.text = convertTime(result.ts.`$date`)

            }
            binding.chatLayout.setOnClickListener {
                mDelegate.onClickChat(result.name, result._id)
            }

        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ChannelViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)

        return ChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder : ChannelViewHolder, position : Int)
    {
        holder.bindData(data[position])
    }

    override fun getItemCount() : Int
    {
        return data.size
    }

    fun convertTime(time : Long) : String?
    {
        val date = Date(time)
        val format : Format = SimpleDateFormat("h:mm a")
        return format.format(date)
    }

}