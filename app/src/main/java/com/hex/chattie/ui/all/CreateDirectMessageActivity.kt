package com.hex.chattie.ui.all

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hex.chattie.R
import com.hex.chattie.common.GeneralClass
import com.hex.chattie.common.Utils
import com.hex.chattie.databinding.ActivityChatDetailBinding
import com.hex.chattie.databinding.ActivityCreateDirectMessageBinding
import com.hex.chattie.delegate.ChatItemDelegate
import com.hex.chattie.network.models.ResultX
import com.hex.chattie.network.responses.User
import com.hex.chattie.ui_seperator.viewmodels.MessageViewModel
import org.json.JSONObject
import org.json.JSONTokener

class CreateDirectMessageActivity : AppCompatActivity(), ChatItemDelegate
{
    companion object
    {
        fun newIntent(context : Context) : Intent
        {
            return Intent(context, CreateDirectMessageActivity::class.java)
        }
    }

    private lateinit var binding : ActivityCreateDirectMessageBinding
    private lateinit var mAdapter : UserListAdapter
    private var userList : MutableList<User> = mutableListOf()
    private val mViewModel : MessageViewModel by viewModels()
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateDirectMessageBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        mViewModel.getUserLists(Utils().getPreference(applicationContext, GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(applicationContext, GeneralClass.USER_ID).toString())

        mViewModel.userListsLiveData.observe(this, Observer {
            if (it.data != null)
            {
                Log.i("tag", "received userlist")
                userList = it.data.users as MutableList
                val filter = userList.filter { user ->
                    user.username != null
                }
                setUpRecyclerView(filter as MutableList)
            }
            else
            {
                Toast.makeText(applicationContext, it.errorMessage, Toast.LENGTH_SHORT).show()
            }
        })



        binding.apply {
            searchBar.addTextChangedListener(object : TextWatcher
            {
                override fun beforeTextChanged(s : CharSequence?, start : Int, count : Int, after : Int)
                {

                }

                override fun onTextChanged(s : CharSequence?, start : Int, before : Int, count : Int)
                {
                    if (s.isNullOrEmpty())
                    {
                        val filter = userList.filter { user ->
                            user.username != null

                        }
                        mAdapter.data = filter as MutableList
                    }
                    else
                    {
                        val filter = userList.filterNotNull().filter {
                            it.username != null && it.username.contains(s)

                        }
                        mAdapter.data = filter as MutableList
                    }
                }

                override fun afterTextChanged(s : Editable?)
                {

                }

            })

        }
    }

    private fun setUpRecyclerView(userList : MutableList<User>)
    {
        binding.apply {
            mAdapter = UserListAdapter(this@CreateDirectMessageActivity)
            rvUserList.layoutManager = LinearLayoutManager(this@CreateDirectMessageActivity)
            mAdapter.data = userList
            rvUserList.adapter = mAdapter
        }
    }

    override fun onClickChat(title : String?, channelId : String)
    {

        val message = "{\"msg\":\"method\",\"id\":\"29\",\"method\":\"createDirectMessage\",\"params\":[\"$title\"]}"
        mViewModel.createDirectMessage(message, Utils().getPreference(applicationContext, GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(applicationContext, GeneralClass.USER_ID).toString())
        mViewModel.directMessageLiveData.observe(this, Observer {
            if (it.data != null)
            {
                val directMessage = JSONTokener(it.data.message).nextValue() as JSONObject

                if (directMessage.has("result"))
                {
                    val result = JSONTokener(directMessage.getString("result")).nextValue() as JSONObject
                    val intent = ChatDetailActivity.newIntent(applicationContext)
                    intent.putExtra("username", title)
                    intent.putExtra("channelId", result.getString("rid"))
                    Log.d("tag", result.getString("rid"))
                    startActivity(intent)

                }

            }
            else
            {
                Toast.makeText(applicationContext, it.errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }
}