package com.hex.chattie.ui.channels

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.hex.chattie.common.GeneralClass
import com.hex.chattie.common.Utils
import com.hex.chattie.databinding.FragmentChannelsBinding
import com.hex.chattie.delegate.ChatItemDelegate
import com.hex.chattie.network.models.Result
import com.hex.chattie.ui.all.AllChatAdapter
import com.hex.chattie.ui_seperator.viewmodels.RoomViewModel
import org.json.JSONObject
import org.json.JSONTokener
import com.google.gson.reflect.TypeToken
import com.hex.chattie.R
import org.json.JSONArray
import java.sql.Types.ARRAY

class ChannelsFragment : Fragment(), ChatItemDelegate
{
    private lateinit var mAdapter : ChannelAdapter
    private lateinit var channelsViewModel : ChannelsViewModel
    private var _binding : FragmentChannelsBinding? = null

    private val mViewModel : RoomViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding !!

    override fun onCreateView(
            inflater : LayoutInflater,
            container : ViewGroup?,
            savedInstanceState : Bundle?
                             ) : View?
    {
        channelsViewModel =
                ViewModelProvider(this).get(ChannelsViewModel::class.java)

        _binding = FragmentChannelsBinding.inflate(inflater, container, false)
        val root : View = binding.root
        setHasOptionsMenu(true)


        return root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val message = "{\"msg\":\"method\",\"id\":\"11\",\"method\":\"rooms/get\",\"params\":[\"2021-09-27T05:56:52.911Z\"]}"
        mViewModel.getRooms(message, Utils().getPreference(requireContext(), GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(requireContext(), GeneralClass.USER_ID).toString())

        mViewModel.getRoomsLiveData.observe(viewLifecycleOwner, Observer {
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
                        it.t == "c"
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
    }

    private fun setUpRecyclerView(result : MutableList<Result>)
    {
        binding.apply {
            rvChannels.layoutManager = LinearLayoutManager(requireContext())
            mAdapter = ChannelAdapter(this@ChannelsFragment)
            mAdapter.data = result
            rvChannels.adapter = mAdapter
        }
    }

    override fun onCreateOptionsMenu(menu : Menu, inflater : MenuInflater)
    {
        inflater.inflate(R.menu.channel_menu, menu)

    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean
    {
        when (item.itemId)
        {
            R.id.menu_clear_chat ->
            {
                Toast.makeText(requireContext(), "Testing!", Toast.LENGTH_SHORT).show()
            }

            R.id.menu_leave_group ->
            {
                Toast.makeText(requireContext(), "Testing!", Toast.LENGTH_SHORT).show()
            }
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
        val intent = ChannelDetailActivity.newIntent(requireContext())
        intent.putExtra("title", title)

        intent.putExtra("channelId", channelId)
        startActivity(intent)
    }
}