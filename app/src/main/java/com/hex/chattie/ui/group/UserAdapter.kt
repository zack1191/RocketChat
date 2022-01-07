package com.hex.chattie.ui.group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hex.chattie.R
import com.hex.chattie.common.DiffCallBack
import com.hex.chattie.databinding.ItemChatBinding
import com.hex.chattie.network.models.Result
import com.hex.chattie.network.models.User
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class UserAdapter(private var userList:MutableList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>(), DiffCallBack
{
    /*var data : MutableList<User> by Delegates.observable(ArrayList()) { _, old, new ->
        compareItem(old, new) { o, n -> o.description.toString() === n.description.toString() }
    }*/

    inner class UserViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        private lateinit var binding : ItemChatBinding

        fun bindData(user : User)
        {
            binding = ItemChatBinding.bind(itemView)

                    binding.apply {
                        tvChannelName.text = user.title


                    }

        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : UserViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)

        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder : UserViewHolder, position : Int)
    {
        holder.bindData  (userList[position])
    }

    override fun getItemCount() : Int
    {
        return userList.size
    }

}