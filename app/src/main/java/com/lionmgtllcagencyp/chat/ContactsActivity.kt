package com.lionmgtllcagencyp.chat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import com.lionmgtllcagencyp.chat.databinding.ActivityContactsBinding
import com.lionmgtllcagencyp.chat.utilities.Contacts

class ContactsActivity : AppCompatActivity() {
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
    }

    companion object{
        fun newIntent(context: Context){
            Intent(context,ContactsActivity::class.java)

        }
    }
}