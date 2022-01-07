package com.hex.chattie.ui.all

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hex.chattie.R
import com.hex.chattie.common.DiffCallBack
import com.hex.chattie.databinding.ItemChatBinding
import com.hex.chattie.databinding.ItemUserListBinding
import com.hex.chattie.delegate.ChatItemDelegate
import com.hex.chattie.network.models.Result
import com.hex.chattie.network.responses.User
import kotlin.properties.Delegates

class UserListAdapter(private var mDelegate : ChatItemDelegate) : RecyclerView.Adapter<UserListAdapter.UserListViewHolder>(), DiffCallBack
{
    var data : MutableList<User> by Delegates.observable(ArrayList()) { _, old, new ->
        compareItem(old, new) { o, n -> o._id.toString() === n._id.toString() }
    }

    inner class UserListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        private lateinit var binding : ItemUserListBinding

        fun bindData(user : User)
        {
            binding = ItemUserListBinding.bind(itemView)
            binding.apply {
                tvUserName.text = user.username
                tvName.text = "@" + user.name

                chatLayout.setOnClickListener {
                    mDelegate.onClickChat(user.username, user._id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : UserListViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)

        return UserListViewHolder(view)
    }

    override fun onBindViewHolder(holder : UserListViewHolder, position : Int)
    {
        holder.bindData(data[position])
    }

    override fun getItemCount() : Int
    {
        return data.size
    }
}