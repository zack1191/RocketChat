package com.hex.chattie.ui.group

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hex.chattie.R
import com.hex.chattie.common.GeneralClass
import com.hex.chattie.common.Utils
import com.hex.chattie.databinding.ActivityGroupChatBinding
import com.hex.chattie.databinding.ItemChatFromMeBinding
import com.hex.chattie.databinding.ItemChatFromOtherBinding
import com.hex.chattie.network.models.Message
import com.hex.chattie.network.models.ResultX
import com.hex.chattie.network.models.User
import com.hex.chattie.network.responses.GetRoomsResponse
import com.hex.chattie.network.responses.RegisterResponse
import com.hex.chattie.ui.all.ChatDetailActivity
import com.hex.chattie.ui.channels.ChannelDetailActivity
import com.hex.chattie.ui_seperator.viewmodels.LoginViewModel
import com.hex.chattie.ui_seperator.viewmodels.MessageViewModel
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import org.json.JSONObject
import org.json.JSONTokener
import java.util.ArrayList
import java.util.Observer

class GroupChatActivity : AppCompatActivity()
{
    companion object
    {
        fun newIntent(context : Context) : Intent
        {
            return Intent(context, GroupChatActivity::class.java)
        }
    }

    private lateinit var mAdapter : UserAdapter
    private val mViewModel : LoginViewModel by viewModels()
    private lateinit var binding : ActivityGroupChatBinding

    private val messageViewModel : MessageViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityGroupChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvToolbarTitle.text = intent.getStringExtra("title")
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val message = "{\"msg\":\"method\",\"id\":\"10\",\"method\":\"loadHistory\",\"params\":[\"${intent.getStringExtra("channelId")}\",null,50,\"2021-09-27T06:05:35.356Z\",false]}"
        mViewModel.loadHistory(message, Utils().getPreference(applicationContext, GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(applicationContext, GeneralClass.USER_ID).toString())

        mViewModel.loadHistoryLiveData.observe(this, {
            if (it.data != null)
            {

                val history = JSONTokener(it.data.message).nextValue() as JSONObject

                if (history.has("result"))
                {
                    val messageList : ResultX = Gson().fromJson(history.getString("result"),
                        object : TypeToken<ResultX>()
                        {}.type)

                    if (messageList.messages.isEmpty())
                    {
                        binding.imageChatIcon.visibility = View.VISIBLE
                        binding.textChatTitle.visibility = View.VISIBLE
                        binding.textChatDescription.visibility = View.VISIBLE
                    }
                    else
                    {
                        binding.imageChatIcon.visibility = View.GONE
                        binding.textChatTitle.visibility = View.GONE
                        binding.textChatDescription.visibility = View.GONE

                    }

                    var mAdapter = GroupieAdapter()
                    for (i in messageList.messages.indices)
                    {
                        if (messageList.messages[i].u.username == Utils().getPreference(applicationContext, GeneralClass.USER_NAME))
                        {
                            mAdapter.add(ChatFromMe(messageList.messages[i]))
                        }
                        else
                        {
                            mAdapter.add(ChatFromOther(messageList.messages[i]))
                        }

                    }

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




        binding.include.buttonSend.setOnClickListener {
            val sendMessage = "{\"msg\":\"method\",\"id\":\"26\",\"method\":\"sendMessage\",\"params\":[{\"rid\":\"${intent.getStringExtra("channelId")}\",\"msg\":\"${binding.include.textMessage.text.toString()}\"}]}"
            messageViewModel.sendMessage(sendMessage, Utils().getPreference(applicationContext, GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(applicationContext, GeneralClass.USER_ID).toString())

        }
        messageViewModel.sendMessageLiveData.observe(this, androidx.lifecycle.Observer { sendMessageResponse ->
            if (sendMessageResponse.data != null)
            {
                mViewModel.loadHistory(message, Utils().getPreference(applicationContext, GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(applicationContext, GeneralClass.USER_ID).toString())
                binding.include.textMessage.text.clear()
            }

        })

        /*binding.etChannel.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP)
            {
                if (event.rawX >= binding.etChannel.right - binding.etChannel.compoundDrawables.get(DRAWABLE_RIGHT).bounds.width())
                {
                    Toast.makeText(applicationContext, binding.etChannel.text.toString(), Toast.LENGTH_SHORT).show()
                    return@OnTouchListener true
                }
            }
            false
        })*/
    }

    /**
     * convert message list to chat from other list
     * */
    private fun List<Message>.toChatFromMe() : List<GroupChatActivity.ChatFromMe>
    {
        return this.map {
            GroupChatActivity.ChatFromMe(it)
        }
    }

    /**
     * convert message list to chat from other list
     * */
    private fun List<Message>.toChatFromOther() : List<GroupChatActivity.ChatFromOther>
    {
        return this.map {
            GroupChatActivity.ChatFromOther(it)
        }
    }

    class ChatFromOther(private var message : Message) : BindableItem<ItemChatFromOtherBinding>()
    {
        override fun bind(viewBinding : ItemChatFromOtherBinding, position : Int)
        {
            viewBinding.apply {
                tvChatFromOther.text = message.msg

                tvName.text = message.u.username

                tvTimeStamp.text = ChatDetailActivity().convertTime(message.ts.`$date`)
            }
        }

        override fun getLayout() : Int
        {
            return R.layout.item_chat_from_other
        }

        override fun initializeViewBinding(view : View) : ItemChatFromOtherBinding
        {
            return ItemChatFromOtherBinding.bind(view)
        }

    }

    class ChatFromMe(private var message : Message) : BindableItem<ItemChatFromMeBinding>()
    {
        override fun bind(viewBinding : ItemChatFromMeBinding, position : Int)
        {
            viewBinding.apply {
                tvChat.text = message.msg
                tvTimeStamp.text = ChatDetailActivity().convertTime(message.ts.`$date`)
            }
        }

        override fun getLayout() : Int
        {
            return R.layout.item_chat_from_me
        }

        override fun initializeViewBinding(view : View) : ItemChatFromMeBinding
        {
            return ItemChatFromMeBinding.bind(view)
        }

    }

}