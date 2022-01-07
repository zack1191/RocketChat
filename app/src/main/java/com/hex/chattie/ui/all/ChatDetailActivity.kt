package com.hex.chattie.ui.all

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hex.chattie.R
import com.hex.chattie.common.ActionSnackBar
import com.hex.chattie.common.GeneralClass
import com.hex.chattie.common.KeyboardHelper
import com.hex.chattie.common.Utils
import com.hex.chattie.databinding.ActivityChatDetailBinding
import com.hex.chattie.databinding.ItemChatFromMeBinding
import com.hex.chattie.databinding.ItemChatFromOtherBinding
import com.hex.chattie.databinding.ItemChatOtherBinding
import com.hex.chattie.network.models.Message
import com.hex.chattie.network.models.ResultX
import com.hex.chattie.ui.channels.ChannelDetailActivity
import com.hex.chattie.ui_seperator.viewmodels.LoginViewModel
import com.hex.chattie.ui_seperator.viewmodels.MessageViewModel
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import org.json.JSONObject
import org.json.JSONTokener
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

class ChatDetailActivity : AppCompatActivity()
{
    companion object
    {
        fun newIntent(context : Context) : Intent
        {
            return Intent(context, ChatDetailActivity::class.java)
        }
    }

    private val mViewModel : LoginViewModel by viewModels()

    private val messageViewModel : MessageViewModel by viewModels()

    private lateinit var actionSnackBar : ActionSnackBar

    //    private lateinit var mViewModel : LoginViewModel
    private lateinit var binding : ActivityChatDetailBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityChatDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val userName = intent.getStringExtra("username")
        val channelId = intent.getStringExtra("channelId")
        binding.tvToolbarTitle.text = userName
        //        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        val message = "{\"msg\":\"method\",\"id\":\"10\",\"method\":\"loadHistory\",\"params\":[\"${intent.getStringExtra("channelId")}\",null,50,\"2021-09-27T06:05:35.356Z\",false]}"
        mViewModel.loadHistory(message, Utils().getPreference(applicationContext, GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(applicationContext, GeneralClass.USER_ID).toString())


        mViewModel.loadHistoryLiveData.observe(this, Observer {
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

                    val chatFromOther = messageList.messages.filter { message ->
                        message.u.username != Utils().getPreference(applicationContext, GeneralClass.USER_NAME)
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
                    mAdapter.notifyDataSetChanged()

                }
                else
                {

                    Toast.makeText(applicationContext, "No message yet!", Toast.LENGTH_SHORT).show()
                }

            }
            else
            {
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
            }
        })


        binding.include2.buttonSend.setOnClickListener {
            val sendMessage = "{\"msg\":\"method\",\"id\":\"26\",\"method\":\"sendMessage\",\"params\":[{\"rid\":\"${intent.getStringExtra("channelId")}\",\"msg\":\"${binding.include2.textMessage.text.toString()}\"}]}"
            messageViewModel.sendMessage(sendMessage, Utils().getPreference(applicationContext, GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(applicationContext, GeneralClass.USER_ID).toString())

        }
        messageViewModel.sendMessageLiveData.observe(this, Observer { sendMessageResponse ->
            if (sendMessageResponse.data != null)
            {
                mViewModel.loadHistory(message, Utils().getPreference(applicationContext, GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(applicationContext, GeneralClass.USER_ID).toString())
                binding.include2.textMessage.text.clear()
            }

        })

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId)
            {
                R.id.menu_leave_group ->
                {
                    /* val snackBar = Snackbar.make  (binding.rvChatDetail, "Test", Snackbar.LENGTH_INDEFINITE)

                     val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout

                     val snackView : View = layoutInflater.inflate(R.layout.message_action_bar, null) // custom_snac_layout is your custom xml

                     snackBarLayout.addView(snackView, 0)
                     snackBar.show()*/
                    actionSnackBar = ActionSnackBar.make(binding.messageListContainer)
                    actionSnackBar.show()

                    actionSnackBar.cancelView.setOnClickListener {
                        actionSnackBar.dismiss()
                    }
                    KeyboardHelper.showSoftKeyboard(binding.include2.textMessage)
                }
            }
            return@setOnMenuItemClickListener true
        }

    }

    /**
     * convert message list to chat from other list
     * */
    private fun List<Message>.toChatFromMe() : List<ChatFromMe>
    {
        return this.map {
            ChatFromMe(it)
        }
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

    class ChatFromOther(private var message : Message) : BindableItem<ItemChatOtherBinding>()
    {
        override fun bind(viewBinding : ItemChatOtherBinding, position : Int)
        {
            viewBinding.apply {
                tvChat.text = message.msg

                tvTimeStamp.text = ChatDetailActivity().convertTime(message.ts.`$date`)

                cardGchatMessageMe.setOnClickListener {
                    Toast.makeText(it.context, "Click!", Toast.LENGTH_SHORT).show()
                }

            }
        }

        override fun getLayout() : Int
        {
            return R.layout.item_chat_other
        }

        override fun initializeViewBinding(view : View) : ItemChatOtherBinding
        {
            return ItemChatOtherBinding.bind(view)
        }

    }

    class ChatFromMe(private var message : Message) : BindableItem<ItemChatFromMeBinding>()
    {
        override fun bind(viewBinding : ItemChatFromMeBinding, position : Int)
        {
            viewBinding.apply {
                tvChat.text = message.msg
                tvTimeStamp.text = ChatDetailActivity().convertTime(message.ts.`$date`)

                cardGchatMessageMe.setOnClickListener {
                    Toast.makeText(it.context, "Click!", Toast.LENGTH_SHORT).show()
                }
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

    fun convertTime(time : Long) : String?
    {
        val date = Date(time)
        val format : Format = SimpleDateFormat("h:mm a")
        return format.format(date)
    }

}