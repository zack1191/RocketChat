package com.hex.chattie.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.hex.chattie.MainActivity
import com.hex.chattie.R
import com.hex.chattie.common.GeneralClass
import com.hex.chattie.common.Hasher
import com.hex.chattie.common.Utils
import com.hex.chattie.databinding.FragmentUsernameBinding
import com.hex.chattie.ui_seperator.viewmodels.LoginViewModel
import org.json.JSONObject
import org.json.JSONTokener

class LoginDialogFragment : DialogFragment()
{
    companion object
    {

        const val TAG = "SimpleDialog"

        private const val KEY_USERNAME = "KEY_USERNAME"
        private const val KEY_PASSWORD = "KEY_PASSWORD"

        fun newInstance(username : String, password : String) : LoginDialogFragment
        {
            val args = Bundle()
            args.putString(KEY_USERNAME, username)
            args.putString(KEY_PASSWORD, password)
            val fragment = LoginDialogFragment()
            fragment.arguments = args
            return fragment
        }

    }

    private var _binding : FragmentUsernameBinding? = null

    private val binding get() = _binding

    private val mViewModel : LoginViewModel by activityViewModels()

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View?
    {

        _binding = FragmentUsernameBinding.inflate(inflater, container, false)

        return binding !!.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        Utils().deletePreference(requireContext(), GeneralClass.ACCESS_TOKEN)
        Utils().deletePreference(requireContext(), GeneralClass.USER_ID)

        val username = binding !!.etUserName.text

        val login = "{\"msg\":\"method\",\"id\":\"4\",\"method\":\"login\",\"params\":[{\"user\":{\"email\":\"${arguments?.getString(KEY_USERNAME)}\"},\"password\":{\"digest\":\"${Hasher().hash(arguments?.getString(KEY_PASSWORD) !!)}\",\"algorithm\":\"sha-256\"}}]}"
        mViewModel.registerLogin(login)
        mViewModel.registerLoginLiveData.observe(viewLifecycleOwner, Observer {
            if (it.data?.message != null)
            {
                it.data.message
                val message = JSONTokener(it.data.message).nextValue() as JSONObject

                Log.d("data", it.data.message)

                if (message.has("result"))
                {
                    val result = JSONTokener(message.getString("result")).nextValue() as JSONObject


                    Utils().savePreference(requireContext(), GeneralClass.ACCESS_TOKEN, result.getString("token"))
                    Utils().savePreference(requireContext(), GeneralClass.USER_ID, result.getString("id"))

                    Toast.makeText(requireContext(), result.getString("id"), Toast.LENGTH_SHORT).show()
                    val message = "{\"msg\":\"method\",\"id\":\"45\",\"method\":\"getUsernameSuggestion\",\"params\":[]}"
                    val accesstoken = Utils().getPreference(requireContext(), GeneralClass.ACCESS_TOKEN)
                    mViewModel.getUserNameSuggestion(message, Utils().getPreference(requireContext(), GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(requireContext(), GeneralClass.USER_ID).toString())

                    mViewModel.usernameSuggestLiveData.observe(this, Observer {
                        if (it.data != null)
                        {
                            val message = JSONTokener(it.data.message).nextValue() as JSONObject

                            binding !!.etUserName.setText(message.getString("result"))
                        }
                        else
                        {
                            Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    })

                }
                if (message.has("error"))
                {

                    Toast.makeText(requireContext(), message.getString("error").toString(), Toast.LENGTH_SHORT).show()

                }
                binding !!.btnUserName.setOnClickListener {
                    val message = "{\"msg\":\"method\",\"id\":\"49\",\"method\":\"setUsername\",\"params\":[\"${binding !!.etUserName.text}\"]}"
                    mViewModel.setUserName(message, Utils().getPreference(requireContext(), GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(requireContext(), GeneralClass.USER_ID).toString())

                }
                mViewModel.setUserNameLiveData.observe(viewLifecycleOwner, Observer { response ->
                    if (response.data != null)
                    {
                        val message = JSONTokener(response.data.message).nextValue() as JSONObject
                        if (message.has("result"))
                        {
                            Utils().savePreference(requireContext(), GeneralClass.USER_NAME, binding !!.etUserName.text.toString())
                            startActivity(MainActivity.newIntent(requireContext()))
                        }

                        if (message.has("error"))
                        {
                            Toast.makeText(requireContext(), "User Name already exist!", Toast.LENGTH_SHORT).show()
                        }

                    }
                    else
                    {
                        Toast.makeText(requireContext(), response.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                })
                binding !!.tvLogout.setOnClickListener {
                    Utils().deletePreference(requireContext(), GeneralClass.ACCESS_TOKEN)
                    Utils().deletePreference(requireContext(), GeneralClass.USER_ID)
                    startActivity(LoginActivity.newIntent(requireContext()))
                }

            }
            else
            {
                Toast.makeText(requireContext(), it.errorMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onDestroy()
    {
        super.onDestroy()
        _binding = null
    }
}