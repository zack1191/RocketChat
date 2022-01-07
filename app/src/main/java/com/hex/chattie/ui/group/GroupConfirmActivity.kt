package com.hex.chattie.ui.group

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hex.chattie.R
import com.hex.chattie.common.GeneralClass
import com.hex.chattie.common.Utils
import com.hex.chattie.databinding.ActivityCreateGroupBinding
import com.hex.chattie.databinding.ActivityGroupConfirmBinding
import com.hex.chattie.network.models.CreateRoom
import com.hex.chattie.network.models.ExtraData
import com.hex.chattie.ui_seperator.viewmodels.RoomViewModel
import org.json.JSONObject
import org.json.JSONTokener

class GroupConfirmActivity : AppCompatActivity()
{
    companion object
    {
        fun newIntent(context : Context) : Intent
        {
            return Intent(context, GroupConfirmActivity::class.java)
        }
    }

    private lateinit var binding : ActivityGroupConfirmBinding

    private lateinit var mAdapter : GroupUserAdapter

    private val mViewModel : RoomViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityGroupConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val userList = intent.getStringArrayListExtra("userList")
        setUpRecyclerView(userList as MutableList<String>)

        binding.apply {
            tvUserCount.text = userList.size.toString() + " user"
        }
    }

    override fun onCreateOptionsMenu(menu : Menu?) : Boolean
    {
        menuInflater.inflate(R.menu.group_confirm_menu, menu)


        return true
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean
    {
        return when (item.itemId)
        {
            R.id.menu_create_confirm ->
            {
                if (binding.textChannelName.text.isNotEmpty())
                {
                    //can customize group option eg. readonly
                    val data = com.hex.chattie.network.models.ExtraData(false, "Testing", false)
                    val members = intent.getStringArrayListExtra("userList") as List<String>
                    val body = CreateRoom(data, members, binding.textChannelName.text.toString(), false)
                    mViewModel.createNewRoom(body, Utils().getPreference(applicationContext, GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(applicationContext, GeneralClass.USER_ID).toString())

                    mViewModel.createNewRoomLiveData.observe(this, Observer {
                        if (it.data != null)
                        {
                            if (it.data.group != null)
                            {
                                Toast.makeText(applicationContext,"Finished create group!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            if (it.data.error != null)
                            {
                                Toast.makeText(applicationContext, it.data.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                        else
                        {
                            Toast.makeText(applicationContext,"Team with that name already exist or server error!", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                else
                {
                    Toast.makeText(applicationContext, "Enter Group Name!", Toast.LENGTH_SHORT).show()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpRecyclerView(user : MutableList<String>)
    {
        binding.apply {
            rvGroup.layoutManager = LinearLayoutManager(this@GroupConfirmActivity)
            mAdapter = GroupUserAdapter()
            mAdapter.data = user
            rvGroup.adapter = mAdapter
        }
    }
}