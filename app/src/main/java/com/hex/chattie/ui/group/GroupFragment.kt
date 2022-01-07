package com.hex.chattie.ui.group

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hex.chattie.R
import com.hex.chattie.common.GeneralClass
import com.hex.chattie.common.Utils
import com.hex.chattie.databinding.FragmentGroupBinding
import com.hex.chattie.delegate.ChatItemDelegate
import com.hex.chattie.network.models.Result
import com.hex.chattie.ui.all.AllChatAdapter
import com.hex.chattie.ui.all.CreateDirectMessageActivity
import com.hex.chattie.ui.channels.ChannelAdapter
import com.hex.chattie.ui.channels.ChannelDetailActivity
import com.hex.chattie.ui_seperator.viewmodels.RoomViewModel
import org.json.JSONObject
import org.json.JSONTokener

class GroupFragment : Fragment(), ChatItemDelegate
{
    private lateinit var mAdapter : ChannelAdapter
    private lateinit var groupViewModel : GroupViewModel
    private var _binding : FragmentGroupBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding !!
    private val mViewModel : RoomViewModel by activityViewModels()

    override fun onCreateView(
            inflater : LayoutInflater,
            container : ViewGroup?,
            savedInstanceState : Bundle?
                             ) : View?
    {
        groupViewModel =
                ViewModelProvider(this).get(GroupViewModel::class.java)

        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        val root : View = binding.root
        setHasOptionsMenu(true)

        /*   val textView : TextView = binding.textReflow
           reflowViewModel.text.observe(viewLifecycleOwner, Observer {
               textView.text = it
           })*/
        return root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val message = "{\"msg\":\"method\",\"id\":\"11\",\"method\":\"rooms/get\",\"params\":[\"2021-09-27T05:56:52.911Z\"]}"
        mViewModel.getRooms(message, Utils().getPreference(requireContext(), GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(requireContext(), GeneralClass.USER_ID).toString())

        mViewModel.getRoomsLiveData.observe(viewLifecycleOwner, Observer {
            // if the fragment in resumed state then only start observing data

            if (it.data != null)
            {
                val data = JSONTokener(it.data.message).nextValue() as JSONObject

                if (data.has("result"))
                {

                    val result = data.getJSONArray("result")
                    val obj : MutableList<Result> = Gson().fromJson(data.getString("result"),
                        object : TypeToken<List<Result>>()
                        {}.type)
                    val filter = obj.filter {
                        it.t == "p"
                    }

                    setUpRecyclerView(filter as MutableList)
                }
                else
                {
                    Toast.makeText(requireContext(), "No channel yet!", Toast.LENGTH_SHORT).show()
                }

            }
            else
            {
                Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
        mViewModel.getLiveData(message, Utils().getPreference(requireContext(), GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(requireContext(), GeneralClass.USER_ID).toString()).observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it.success.toString(), Toast.LENGTH_SHORT).show()
        }
                                                                                                                                                                                                        )

    }

    private fun setUpRecyclerView(result : MutableList<Result>)
    {
        binding.apply {
            rvGroup.layoutManager = LinearLayoutManager(requireContext())
            mAdapter = ChannelAdapter(this@GroupFragment)
            mAdapter.data = result
            rvGroup.adapter = mAdapter
        }
    }

    override fun onCreateOptionsMenu(menu : Menu, inflater : MenuInflater)
    {
        inflater.inflate(R.menu.group_chat_menu, menu)
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean
    {
        when (item.itemId)
        {
            R.id.menu_create_group -> startActivity(CreateGroupActivity.newIntent(requireContext()))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickChat(title : String?, channelId : String)
    {
        val intent = GroupChatActivity.newIntent(requireContext())
        intent.putExtra("title", title)

        intent.putExtra("channelId", channelId)
        startActivity(intent)
    }

    override fun onResume()
    {
        super.onResume()

        val message = "{\"msg\":\"method\",\"id\":\"11\",\"method\":\"rooms/get\",\"params\":[\"2021-09-27T05:56:52.911Z\"]}"
        mViewModel.getRooms(message, Utils().getPreference(requireContext(), GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(requireContext(), GeneralClass.USER_ID).toString())

    }
}