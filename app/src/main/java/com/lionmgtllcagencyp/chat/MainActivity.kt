package com.lionmgtllcagencyp.chat

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lionmgtllcagencyp.chat.databinding.ActivityMainBinding
import com.lionmgtllcagencyp.chat.fragments.ChatsFragment
import com.lionmgtllcagencyp.chat.fragments.StatusFragment
import com.lionmgtllcagencyp.chat.fragments.StatusUpdateFragment
import com.lionmgtllcagencyp.chat.listeners.FailureCallback
import com.lionmgtllcagencyp.chat.utilities.DATA_USERS
import com.lionmgtllcagencyp.chat.utilities.DATA_USER_PHONE
import com.lionmgtllcagencyp.chat.utilities.READ_CONTACTS_REQUEST_CODE
import com.lionmgtllcagencyp.chat.utilities.REQUEST_NEW_CHAT


class MainActivity : AppCompatActivity(),FailureCallback {
    companion object{
        var paramName = "param_name"
        var paramPhone = "parame_phone"
        fun newIntent(context: Context) = Intent(context,MainActivity::class.java)
    }

    var sectionPagerAdapter: SectionPagerAdapter ?= null
    private val firebaseAuth = FirebaseAuth.getInstance()
    lateinit var binding: ActivityMainBinding
    private val chatsFragment = ChatsFragment()
    private val statusFragment = StatusFragment()
    private val StatusUpdateFragment = StatusUpdateFragment()
    private val firebaseDatabase = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivityMainBinding.inflate(inflater)
        setContentView(binding.root)

        chatsFragment.setFailureCallbackListener(this)

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


        binding.fab.setOnClickListener {
            //Snackbar.make(view,"this is a simple snackBar",Snackbar.LENGTH_SHORT)
              // .setAction("Action",null) .show()
            onNewChat()


        }
    }

    private fun onNewChat(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
                AlertDialog.Builder(this)
                    .setTitle("Contacts Permission")
                    .setMessage("This app requires access to your contacts to initiate a conversation")
                    .setPositiveButton("Ask Me") {_,_ ->
                        requestContactsPermission()
                    }
                    .setNegativeButton("Cancel"){_,_ ->

                    }

                    .show()
            }else{
                requestContactsPermission()
            }
        }else{
            startNewActivity()
        }
    }

    private fun requestContactsPermission(){
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_CONTACTS),
            READ_CONTACTS_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
           READ_CONTACTS_REQUEST_CODE -> {
               if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   startNewActivity()
               }
           }
       }
    }
    fun startNewActivity(){
        startActivityForResult(ContactsActivity.newIntent(this), REQUEST_NEW_CHAT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_NEW_CHAT -> {
                    val name = data?.getStringExtra(paramName)?:""
                    val phone = data?.getStringExtra(paramPhone)?:""
                    checkNewChatUser(name,phone)


                }
            }
        }
    }
    private fun checkNewChatUser(name:String,phone:String){
        if(name.isNotEmpty() && phone.isNotEmpty()){
            firebaseDatabase.collection(DATA_USERS)
                .whereEqualTo(DATA_USER_PHONE,phone)
                .get().addOnSuccessListener {
                    if(it.documents.size > 0 ){
                        chatsFragment.newChat(it.documents[0].id)
                    }else{
                        AlertDialog.Builder(this)
                            .setTitle("User not found")
                            .setMessage("$name does not have an account. Send them an sms to install this app?")
                            .setPositiveButton("Ok"){_,_ ->
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse("sms:$phone")
                                intent.putExtra("sms_body",
                                    "Hi, im using this new whatsapp clone app, you should install it" +
                                        "so you can chat there."
                                )
                                startActivity(intent)

                            }
                            .setNegativeButton("Cancel"){_,_ ->

                            }
                            .show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this,"An Error has occured, please try again later",Toast.LENGTH_SHORT).show()
                    it.printStackTrace()
                }
        }

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

    override fun onUserError() {
        Toast.makeText(this,"User not found",Toast.LENGTH_SHORT).show()
        startActivity(LoginActivity.newIntent(this))
    }
}