package com.hex.chattie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyCharacterMap.load
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.bumptech.glide.Glide
import com.hex.chattie.common.GeneralClass
import com.hex.chattie.common.Utils
import com.hex.chattie.databinding.ActivityMainBinding
import com.hex.chattie.ui.login.LoginActivity
import com.hex.chattie.ui_seperator.viewmodels.LoginViewModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.PicassoProvider
import java.lang.System.load
import java.util.ServiceLoader.load

class MainActivity : AppCompatActivity()
{
    //    private lateinit var navController : NavController
    companion object
    {
        fun newIntent(context : Context) : Intent
        {
            return Intent(context, MainActivity::class.java)
        }
    }

    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var binding : ActivityMainBinding
    private val mViewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        if (Utils().getPreference(applicationContext, GeneralClass.USER_ID) == null)
        {
            val intent = LoginActivity.newIntent(applicationContext)
            startActivity(intent)
        }

        val nav = findNavController(R.id.nav_host_fragment_content_main)
        //        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        //        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, nav, binding.drawerLayout)

        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_contact, R.id.nav_group, R.id.nav_channels), binding.drawerLayout)
        setupActionBarWithNavController(nav, appBarConfiguration)
        binding.navViewBase.setupWithNavController(nav)
        binding.bottomNavView.setupWithNavController(nav)

        mViewModel.me(Utils().getPreference(applicationContext, GeneralClass.ACCESS_TOKEN).toString(), Utils().getPreference(applicationContext, GeneralClass.USER_ID).toString())

        mViewModel.meLiveData.observe(this, Observer {
            if (it.data != null)
            {
                Toast.makeText(applicationContext, it.data.username, Toast.LENGTH_SHORT).show()

                Utils().savePreference(applicationContext, GeneralClass.USER_NAME, it.data.username)

                val header = binding.navViewBase.getHeaderView(0)
                val name = header.findViewById<TextView>(R.id.tvProfileName)
                val profileImg = header.findViewById<ImageView>(R.id.imageView)


                name.text = it.data.username
                it.data.avatarUrl
                Picasso.get().load("http://192.168.1.6:3000/avatar/user55").into(profileImg)


            }
            else
            {
                Toast.makeText(applicationContext, it.errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
        binding.navViewBase.setNavigationItemSelectedListener {
            when (it.itemId)
            {
                R.id.menu_log_out ->
                {
                    Toast.makeText(applicationContext, "logout!", Toast.LENGTH_SHORT).show()
                    Utils().deletePreference(applicationContext, GeneralClass.ACCESS_TOKEN)
                    Utils().deletePreference(applicationContext, GeneralClass.USER_ID)
                    startActivity(LoginActivity.newIntent(applicationContext))
                    return@setNavigationItemSelectedListener true
                }

                else ->
                {
                    false
                }
            }
        }

    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean
    {
        when (item.itemId)
        {

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawerLayout()
    {

    }

    override fun onSupportNavigateUp() : Boolean
    {
        val nav = findNavController(R.id.nav_host_fragment_content_main)

        return nav.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}