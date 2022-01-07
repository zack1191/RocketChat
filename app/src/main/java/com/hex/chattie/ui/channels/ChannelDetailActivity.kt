package com.hex.chattie.ui.channels

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hex.chattie.R
import com.hex.chattie.common.GeneralClass
import com.hex.chattie.common.Utils
import com.hex.chattie.databinding.ActivityChannelDetailBinding
import com.hex.chattie.databinding.ItemChatFromOtherBinding
import com.hex.chattie.network.models.Message
import com.hex.chattie.network.models.Result
import com.hex.chattie.network.models.ResultX
import com.hex.chattie.ui_seperator.viewmodels.LoginViewModel
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import org.json.JSONObject
import org.json.JSONTokener
import android.view.View.OnTouchListener
import com.hex.chattie.databinding.ItemChatChannelBinding

class ChannelDetailActivity : AppCompatActivity()
{
    companion object
    {
        fun newIntent(context : Context) : Intent
        {
            return Intent(context, ChannelDetailActivity::class.java)
        }
    }

    private val mViewModel : LoginViewModel by viewModels()
    private lateinit var binding : ActivityChannelDetailBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityChannelDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvToolbarTitle.text = intent.getStringExtra("title")
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val message = "{\"msg\":\"method\",\"id\":\"10\",\"method\":\"loadHistory\",\"params\":[\"${intent.getStringExtra  ("channelId")}\",null,50,\"2021-09-27T06:05:35.356Z\",false]}"
        mViewModel.loadHistory(message, Utils().getPreference(applicationContext, GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(applicationContext, GeneralClass.USER_ID).toString())

        mViewModel  .loadHistoryLiveData.observe(this, Observer {
            if (it.data != null)
            {
                it.data.message

                val history = JSONTokener(it.data.message).nextValue() as JSONObject

                if (history.has("result"))
                {
                    val messageList : ResultX = Gson().fromJson(history.getString("result"),
                        object : TypeToken<ResultX>()
                        {}.type)
                    var mAdapter = GroupieAdapter()
                    mAdapter.addAll(messageList.messages.toChatFromOther())

                    binding.rvChatDetail.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
                    binding.rvChatDetail.adapter = mAdapter

                }
                else
                {
                    Toast.makeText(applicationContext, "No message yet!", Toast.LENGTH_SHORT).show()
                }
                Log.d("history", it.data.message)
            }
            else
            {
                Toast.makeText(applicationContext, it.errorMessage, Toast.LENGTH_SHORT).show()
            }
        })


    }

    /**
     * convert message list to chat from other list
     * */
    private fun List<Message>.toChatFromOther() : List<ChatFromOther>
    {
        return this.map {
            ChatFromOther(it)
        }
    }

    class ChatFromOther(private var message : Message) : BindableItem<ItemChatChannelBinding>()
    {
        override fun bind(viewBinding  :ItemChatChannelBinding, position : Int)
        {
            viewBinding.apply {
                tvChatFromOther.text = message.msg

                tvName.text=message.rid
            }
        }

        override fun getLayout() : Int
        {
            return R.layout.item_chat_channel
        }

        override fun initializeViewBinding(view : View)  :ItemChatChannelBinding
        {
            return ItemChatChannelBinding.bind(view)
        }

    }
}