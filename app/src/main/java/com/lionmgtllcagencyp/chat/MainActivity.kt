package com.lionmgtllcagencyp.chat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TableLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.lionmgtllcagencyp.chat.databinding.ActivityMainBinding
import com.lionmgtllcagencyp.chat.databinding.FragmentMainBinding
import com.lionmgtllcagencyp.chat.fragments.ChatsFragment
import com.lionmgtllcagencyp.chat.fragments.StatusFragment
import com.lionmgtllcagencyp.chat.fragments.StatusUpdateFragment


class MainActivity : AppCompatActivity() {
    companion object{
        fun newIntent(context: Context) = Intent(context,MainActivity::class.java)
    }

    var sectionPagerAdapter: SectionPagerAdapter ?= null
    private val firebaseAuth = FirebaseAuth.getInstance()
    lateinit var binding: ActivityMainBinding
    private val chatsFragment = ChatsFragment()
    private val statusFragment = StatusFragment()
    private val StatusUpdateFragment = StatusUpdateFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivityMainBinding.inflate(inflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        sectionPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        binding.container.adapter = sectionPagerAdapter
        binding.container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabsLayout))
        binding.tabsLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(binding.container))
        resizeTabs()
        binding.tabsLayout.getTabAt(1)?.select()

        binding.tabsLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> binding.fab.visibility = View.GONE
                    1 -> binding.fab.visibility = View.VISIBLE
                    2 -> binding.fab.visibility = View.GONE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


        binding.fab.setOnClickListener { view ->
            Snackbar.make(view,"this is a simple snackBar",Snackbar.LENGTH_SHORT)
                .setAction("Action",null) .show()
        }
    }

    private fun onNewChat(view:View){

    }

    private fun resizeTabs(){
        val layout = (binding.tabsLayout.getChildAt(0) as LinearLayout).getChildAt(0) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0.4f
        layout.layoutParams = layoutParams

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_log_out -> onLogOut()
            R.id.action_profile -> onProfile()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onProfile(){
        startActivity(ProfileActivity.newIntent(this))
    }

    private fun onLogOut(){
        firebaseAuth.signOut()
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    inner class SectionPagerAdapter(fragmentManager:FragmentManager): FragmentPagerAdapter(fragmentManager) {
        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {
            return when(position){
                0 -> StatusUpdateFragment
                1 -> chatsFragment
                else -> statusFragment
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if(firebaseAuth.currentUser == null){
            startActivity(LoginActivity.newIntent(this))
            finish()
        }
    }
}