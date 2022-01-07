package com.hex.chattie.ui.login

import android.content.Context
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonToken
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.hex.chattie.MainActivity
import com.hex.chattie.R
import com.hex.chattie.common.GeneralClass
import com.hex.chattie.common.Hasher
import com.hex.chattie.common.Utils
import com.hex.chattie.databinding.ActivityLoginBinding
import com.hex.chattie.ui_seperator.viewmodels.LoginViewModel
import org.json.JSONObject
import org.json.JSONTokener
import java.math.BigInteger
import java.security.MessageDigest
import android.content.DialogInterface
import android.content.Intent

import android.text.Editable

import android.widget.EditText

class LoginActivity : AppCompatActivity()
{
    companion object
    {
        fun newIntent(context : Context) : Intent
        {
            return Intent(context, LoginActivity::class.java)
        }
    }

    private lateinit var binding : ActivityLoginBinding

    private val mViewModel : LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            tvTitle.setTypeface(tvTitle.typeface, Typeface.BOLD_ITALIC)

            btnLogin.setOnClickListener {
                val message = "{\"msg\":\"method\",\"id\":\"4\",\"method\":\"login\",\"params\":[{\"user\":{\"email\":\"${etUserNameLogin.text}\"},\"password\":{\"digest\":\"${Hasher().hash(etPasswordEditText.text.toString())}\",\"algorithm\":\"sha-256\"}}]}"

                mViewModel.login(message)

            }
            mViewModel.loginLiveData.observe(this@LoginActivity, Observer {
                if (it.data?.message != null)
                {
                    it.data.message
                    val message = JSONTokener(it.data.message).nextValue() as JSONObject

                    Log.d("data", it.data.message)

                    if (message.has("result"))
                    {
                        val result = JSONTokener(message.getString("result")).nextValue() as JSONObject


                        Utils().savePreference(applicationContext, GeneralClass.ACCESS_TOKEN, result.getString("token"))
                        Utils().savePreference(applicationContext, GeneralClass.USER_ID, result.getString("id"))
                        val intent = MainActivity.newIntent(applicationContext)
                        startActivity(intent)
                        Toast.makeText(applicationContext, result.getString("id"), Toast.LENGTH_SHORT).show()

                    }
                    if (message.has("error"))
                    {
                        etUserNameLogin.error = "User name or password incorrect!"
                        Toast.makeText(applicationContext, message.getString("error").toString(), Toast.LENGTH_SHORT).show()

                    }

                }
                else
                {
                    Toast.makeText(applicationContext, it.errorMessage.toString(), Toast.LENGTH_SHORT).show()
                }
            })

            btnSignUp.setOnClickListener {
                val register = "{\"msg\":\"method\",\"id\":\"4\",\"method\":\"registerUser\",\"params\":[{\"name\":\"${etNameSignUp.text}\",\"email\":\"${etEmailSignUp.text}\",\"pass\":\"${etPasswordSignUp.text}\",\"confirm-pass\":\"${etConfirmPasswordSignUp.text}\"}]}"
                mViewModel.register(register)
                btnSignUp.text = "Please wait..."
                wp7progressBar.showProgressBar()
            }
            mViewModel.registerLiveData.observe(this@LoginActivity, Observer {
                if (it.data != null)
                {
                    val register = JSONTokener(it.data.message).nextValue() as JSONObject

                    if (register.has("result"))
                    {
                        btnSignUp.text = "Register"
                        wp7progressBar.hideProgressBar()
                        var dialog = LoginDialogFragment.newInstance(etEmailSignUp.text.toString(), etPasswordSignUp.text.toString())
                        dialog.isCancelable = false
                        dialog.show(supportFragmentManager, null)
                        Toast.makeText(applicationContext, register.getString("result").toString(), Toast.LENGTH_SHORT).show()

                    }

                    if (register.has("error"))
                    {
                        btnSignUp.text = "Register"
                        Toast.makeText(applicationContext, register.getString("error").toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    btnSignUp.text = "Register"
                    Toast.makeText(applicationContext, it.errorMessage.toString(), Toast.LENGTH_SHORT).show()
                }
            })

            tvSignup.setOnClickListener {
                loginLayout.visibility = View.GONE
                btnLogin.visibility = View.GONE
                signUpLayout.visibility = View.VISIBLE
                btnSignUp.visibility = View.VISIBLE
                tvSignup.visibility = View.GONE
                tvLogin.visibility = View.VISIBLE

            }

            tvLogin.setOnClickListener {
                loginLayout.visibility = View.VISIBLE
                btnLogin.visibility = View.VISIBLE
                signUpLayout.visibility = View.GONE
                btnSignUp.visibility = View.GONE
                tvSignup.visibility = View.VISIBLE
                tvLogin.visibility = View.GONE
            }

        }
    }

    private fun dialog()
    {
        /*val alert : AlertDialog.Builder = AlertDialog.Builder(this)
        val edittext = EditText(this)
        alert.setMessage("The username is used to allow others to mention you in messages.")
        alert.setTitle("REGISTER USERNAME")

        alert.setView(edittext)

        alert.setPositiveButton("Use this username") { dialog, whichButton -> //What ever you want to do with the value
            Toast.makeText(applicationContext, edittext.text.toString(), Toast.LENGTH_SHORT).show()
        }

        alert.setNegativeButton("Logout") { dialog, whichButton ->
            // what ever you want to do with No option.
            dialog.dismiss()
        }

        alert.show()*/

    }

}