package com.hex.chattie.ui.all

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hex.chattie.R
import com.hex.chattie.common.GeneralClass
import com.hex.chattie.common.Utils
import com.hex.chattie.databinding.FragmentAllBinding
import com.hex.chattie.delegate.ChatItemDelegate
import com.hex.chattie.network.models.Result
import com.hex.chattie.ui_seperator.viewmodels.RoomViewModel
import org.json.JSONObject
import org.json.JSONTokener

import android.view.LayoutInflater

/**
 * Fragment that demonstrates a responsive layout pattern where the format of the content
 * transforms depending on the size of the screen. Specifically this Fragment shows items in
 * the [RecyclerView] using LinearLayoutManager in a small screen
 * and shows items using GridLayoutManager in a large screen.
 */
class AllFragment : Fragment(), ChatItemDelegate, MenuItem.OnMenuItemClickListener
{

    private lateinit var mAdapter : AllChatAdapter

    private lateinit var allViewModel : AllViewModel

    private val mViewModel : RoomViewModel by activityViewModels()
    private var _binding : FragmentAllBinding? = null

    private var chatList : List<Result> = mutableListOf()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding !!

    override fun onCreateView(
            inflater : LayoutInflater,
            container : ViewGroup?,
            savedInstanceState : Bundle?
                             ) : View
    {
        allViewModel = ViewModelProvider(this).get(AllViewModel::class.java)
        _binding = FragmentAllBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        val root : View = binding.root


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
                    chatList = obj.filter {
                        it.t == "d"
                    }
                    setUpRecyclerView(chatList as MutableList)
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

    override fun onCreateOptionsMenu(menu : Menu, inflater : MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.chat_menu, menu)
        /*val searchMenuItem = menu.findItem(R.id.menu_search)
        val searchView = searchMenuItem?.actionView as SearchView
        with(searchView) {
            setIconifiedByDefault(true)
            maxWidth = Integer.MAX_VALUE
            onQueryTextListener { queryChatRoomsByName(it) }
        }
        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener
        {
            override fun onMenuItemActionCollapse(item : MenuItem) : Boolean
            {
                activity?.invalidateOptionsMenu()
                menu.findItem(R.id.menu_search).isVisible = false


                return true
            }

            override fun onMenuItemActionExpand(item : MenuItem) : Boolean
            {
                // We need to hide the all the menu items here.
                menu.findItem(R.id.menu_create).isVisible = false
                return true
            }
        })*/
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean
    {
        when (item.itemId)
        {
            R.id.menu_create ->
            {
                val menuItems = requireContext().inflate(R.menu.message_actions).toList()

                val actionsBottomSheet = MessageBottomSheetFragment()
                actionsBottomSheet.addItems(menuItems, this)
                actionsBottomSheet.show(requireActivity().supportFragmentManager, null)
            }
            //                startActivity(CreateDirectMessageActivity.newIntent(requireContext()))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpRecyclerView(result : MutableList<Result>)
    {
        binding.apply {
            rvChat.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            mAdapter = AllChatAdapter(this@AllFragment)
            mAdapter.data = result
            rvChat.adapter = mAdapter
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickChat(title : String?, channelId : String)
    {
        val intent = ChatDetailActivity.newIntent(requireContext())
        intent.putExtra("username", title)
        Log.d("channelId", channelId)
        intent.putExtra("channelId", channelId)
        startActivity(intent)
    }

    fun SearchView.onQueryTextListener(queryListener : (String) -> Unit)
    {
        return this.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query : String) : Boolean
            {
                queryListener(query)
                return true
            }

            override fun onQueryTextChange(newText : String) : Boolean
            {
                queryListener(newText)
                return true
            }
        })
    }

    fun TextView.setTextViewAppearance(context : Context, style : Int)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            setTextAppearance(style)
        }
        else
        {
            setTextAppearance(context, style)
        }
    }
    /*
        private fun queryChatRoomsByName(name : String?) : Boolean
        {
            if (name.isNullOrEmpty())
            {
                //            showAllChats()
                mAdapter.data = chatList as MutableList
            }
            else
            {
                TODO()
                //filter with username
                val data = chatList.filter {
                    it._id.contains(name)
                }
                mAdapter.data = data as MutableList
            }
            return true
        }*/

    override fun onResume()
    {
        super.onResume()
        val message = "{\"msg\":\"method\",\"id\":\"11\",\"method\":\"rooms/get\",\"params\":[\"2021-09-27T05:56:52.911Z\"]}"
        mViewModel.getRooms(message, Utils().getPreference(requireContext(), GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(requireContext(), GeneralClass.USER_ID).toString())

    }

    override fun onMenuItemClick(item : MenuItem?) : Boolean
    {
        when (item !!.itemId)
        {
            R.id.action_reply ->
            {

            }

            R.id.action_copy ->
            {
                Toast.makeText(requireContext(), "Copy!", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    @SuppressLint("RestrictedApi")
    fun Context.inflate(@MenuRes menuRes : Int) : Menu
    {
        val menu = MenuBuilder(this)
        val menuInflater = SupportMenuInflater(this)
        menuInflater.inflate(menuRes, menu)
        return menu
    }

    fun Menu.toList() : List<MenuItem>
    {
        val menuItems = ArrayList<MenuItem>(this.size())
        (0 until this.size()).mapTo(menuItems) { this.getItem(it) }
        return menuItems
    }
}