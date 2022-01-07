package com.hex.chattie.ui.all

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hex.chattie.R
import com.hex.chattie.databinding.FragmentAllBinding
import com.hex.chattie.databinding.FragmentMessageBottomSheetBinding
import com.hex.chattie.databinding.ItemChatBinding
import com.hex.chattie.databinding.MessageActionItemBinding

class MessageBottomSheetFragment : BottomSheetDialogFragment()
{
    private var _binding : FragmentMessageBottomSheetBinding? = null
    private val binding get() = _binding !!

    private val adapter = MessageActionAdapter()

    override fun getTheme() : Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View?
    {
        _binding = FragmentMessageBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun addItems(items : List<MenuItem>, itemClickListener : MenuItem.OnMenuItemClickListener)
    {
        adapter.addItems(items, ActionItemClickListener(dismissAction = { dismiss() },
            itemClickListener = itemClickListener))
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvBottomSheet.layoutManager = LinearLayoutManager(requireContext())
            rvBottomSheet.adapter = adapter
        }

    }

    private class ActionItemClickListener(
            val dismissAction : () -> Unit,
            val itemClickListener : MenuItem.OnMenuItemClickListener
                                         )

    private class MessageActionAdapter : RecyclerView.Adapter<MessageActionViewHolder>()
    {

        private lateinit var itemClickListener : ActionItemClickListener
        private val menuItems = mutableListOf<MenuItem>()

        override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MessageActionViewHolder
        {
            return MessageActionViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.message_action_item, parent, false)
                                          )
        }

        override fun getItemCount() = menuItems.size

        override fun onBindViewHolder(holder : MessageActionViewHolder, position : Int)
        {
            holder.bind(menuItems[position], itemClickListener)
        }

        fun addItems(items : List<MenuItem>, itemClickListener : ActionItemClickListener)
        {
            this.itemClickListener = itemClickListener
            menuItems.clear()
            menuItems.addAll(items)
            notifyDataSetChanged()
        }
    }

    private class MessageActionViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {

        private lateinit var binding : MessageActionItemBinding

        fun bind(item : MenuItem, itemClickListener : ActionItemClickListener)
        {
            binding = MessageActionItemBinding.bind(itemView)

            binding.apply {
                messageActionTitle.text = item.title

                messageActionIcon.setImageDrawable(item.icon)
                binding.root.setOnClickListener {
                    itemClickListener.itemClickListener.onMenuItemClick(item)
                    itemClickListener.dismissAction.invoke()
                }
            }
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}



