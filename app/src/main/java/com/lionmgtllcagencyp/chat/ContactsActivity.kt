package com.lionmgtllcagencyp.chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lionmgtllcagencyp.chat.adapters.ContactsAdapter
import com.lionmgtllcagencyp.chat.databinding.ActivityContactsBinding
import com.lionmgtllcagencyp.chat.listeners.ContactsClickListener
import com.lionmgtllcagencyp.chat.utilities.Contacts

class ContactsActivity : AppCompatActivity(),ContactsClickListener {
    private lateinit var binding:  ActivityContactsBinding
    private val contactsList = arrayListOf<Contacts>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivityContactsBinding.inflate(inflater)
        setContentView(binding.root)

        getContacts()
    }

    private fun getContacts(){
        binding.progressLayout.visibility = View.VISIBLE
        contactsList.clear()
        val newList = arrayListOf<Contacts>()
        val phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)
        while(phones!!.moveToNext()){
            val name = phones.getString(phones.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber = phones.getString(phones.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
            newList.add(Contacts(name,phoneNumber))
        }
        contactsList.addAll(newList)
        phones.close()
        setupRecyclerViewList()
    }

    fun setupRecyclerViewList(){
        binding.progressLayout.visibility = View.GONE
        val contactsAdapter = ContactsAdapter(contactsList)
        contactsAdapter.setOnItemClickListener(this)
        binding.contactsRecyclerView.apply {
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(this@ContactsActivity)
            addItemDecoration(DividerItemDecoration(this@ContactsActivity,DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)

        }
    }

    companion object{
        fun newIntent(context: Context):Intent{
            return Intent(context,ContactsActivity::class.java)
        }
    }

    override fun onContactClicked(name: String, phone: String) {
        var intent = Intent()
        intent.putExtra(MainActivity.paramName,name)
        intent.putExtra(MainActivity.paramPhone,phone)
        setResult(Activity.RESULT_OK,intent)
        finish()

    }
}