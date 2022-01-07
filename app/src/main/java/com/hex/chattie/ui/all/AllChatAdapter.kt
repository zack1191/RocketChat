package com.hex.chattie.ui.all

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hex.chattie.R
import com.hex.chattie.common.DiffCallBack
import com.hex.chattie.common.GeneralClass
import com.hex.chattie.common.Utils
import com.hex.chattie.databinding.ItemChatBinding
import com.hex.chattie.delegate.ChatItemDelegate
import com.hex.chattie.network.models.Result
import com.squareup.picasso.Picasso
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class AllChatAdapter(private var mDelegate : ChatItemDelegate) : RecyclerView.Adapter<AllChatAdapter.AllChatViewHolder>(), DiffCallBack
{
    var data : MutableList<Result> by Delegates.observable(ArrayList()) { _, old, new ->
        compareItem(old, new) { o, n -> o._id.toString() === n._id.toString() }
    }

    inner class AllChatViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        private lateinit var binding : ItemChatBinding
        @SuppressLint("SetTextI18n")
        fun bindData(result : Result)
        {
            binding = ItemChatBinding.bind(itemView)

            binding.apply {
                if (result.usernames != null)
                {
                    val filter = result.usernames.filter {
                        it.toString() != Utils().getPreference(itemView.context, GeneralClass.USER_NAME)
                    }
                    tvChannelName.text = filter.toString().filter {
                        it.toString() != "[" && it.toString() != "]"
                    }
                    if (result.lastMessage != null)
                    {
                        tvLastMessages.text = result.lastMessage.msg

                        tvTime.text = convertTime(result.ts.`$date`)
                    }
                    else
                    {
                        tvLastMessages.text = "No message"
                    }


                    Log.d("username", filter.toString())
                }
            }
            binding.chatLayout.setOnClickListener {
                mDelegate.onClickChat(binding.tvChannelName.text.toString(), result._id)
            }

        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : AllChatViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)

        return AllChatViewHolder(view)
    }

    override fun onBindViewHolder(holder : AllChatViewHolder, position : Int)
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