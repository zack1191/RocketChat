package com.hex.chattie.ui.group

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.hex.chattie.common.GeneralClass
import com.hex.chattie.common.Utils
import com.hex.chattie.databinding.ActivityCreateDirectMessageBinding
import com.hex.chattie.databinding.ActivityCreateGroupBinding
import com.hex.chattie.delegate.ChatItemDelegate
import com.hex.chattie.network.responses.User
import com.hex.chattie.ui.all.CreateDirectMessageActivity
import com.hex.chattie.ui.all.UserListAdapter
import com.hex.chattie.ui_seperator.viewmodels.MessageViewModel
import android.R
import android.R.attr
import android.R.attr.button

class CreateGroupActivity : AppCompatActivity(), ChatItemDelegate
{
    companion object
    {
        fun newIntent(context : Context) : Intent
        {
            return Intent(context, CreateGroupActivity::class.java)
        }
    }

    private lateinit var binding : ActivityCreateGroupBinding

    private val mViewModel : MessageViewModel by viewModels()

    private lateinit var mAdapter : UserListAdapter
    private var userList : MutableList<User> = mutableListOf()
    private var userName : ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
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

    override fun onCreateOptionsMenu(menu : Menu?) : Boolean
    {
        menuInflater.inflate(com.hex.chattie.R.menu.create_group_menu, menu)


        return true
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean
    {
        return when (item.itemId)
        {
            com.hex.chattie.R.id.menu_create_group ->
            {

                val intent = GroupConfirmActivity.newIntent(applicationContext)
                intent.putStringArrayListExtra("userList",userName)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpRecyclerView(userList : MutableList<User>)
    {
        binding.apply {
            mAdapter = UserListAdapter(this@CreateGroupActivity)
            rvUserList.layoutManager = LinearLayoutManager(this@CreateGroupActivity)
            mAdapter.data = userList
            rvUserList.adapter = mAdapter
        }
    }

    override fun onClickChat(title : String?, channelId : String)
    {

        if (userName.contains(title))
        {
            Toast.makeText(applicationContext, "User already added!", Toast.LENGTH_SHORT).show()
        }
        else
        {
            val chip = Chip(this)
            chip.text = title

            chip.isCloseIconVisible = true
            chip.isCheckable = false
            chip.isClickable = false

            binding.chipGroup.addView(chip)
            binding.chipGroup.visibility = View.VISIBLE

            chip.setOnCloseIconClickListener {
                binding.chipGroup.removeView(it)
                userName.remove(chip.text.toString())

            }
        }
        userName.clear()
        for (i in 0 until binding.chipGroup.childCount)
        {
            val userChip = binding.chipGroup.getChildAt(i) as Chip


            userName.add(userChip.text.toString())
        }

    }

}