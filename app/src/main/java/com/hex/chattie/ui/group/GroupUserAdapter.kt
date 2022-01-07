package com.hex.chattie.ui.group

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hex.chattie.R
import com.hex.chattie.common.DiffCallBack
import com.hex.chattie.databinding.ItemChatBinding
import com.hex.chattie.databinding.ItemUserListBinding
import kotlin.properties.Delegates

class GroupUserAdapter : RecyclerView.Adapter<GroupUserAdapter.GroupUserViewHolder>(), DiffCallBack
{
    var data : MutableList<String> by Delegates.observable(ArrayList()) { _, old, new ->
        compareItem(old, new) { o, n -> o.toString() === n.toString() }
    }

    inner class GroupUserViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        private lateinit var binding : ItemUserListBinding
        @SuppressLint("SetTextI18n")
        fun bindData(user : String)
        {
            binding = ItemUserListBinding.bind(itemView)

            binding.apply {
                tvUserName.text = user
                tvName.text = "@$user"
            }
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : GroupUserViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)

        return GroupUserViewHolder(view)
    }

    override fun onBindViewHolder(holder : GroupUserViewHolder, position : Int)
    {
        holder.bindData(data[position])
    }

    override fun getItemCount() : Int
    {
        return data.size
    }
}