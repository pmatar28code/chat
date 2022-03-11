package com.lionmgtllcagencyp.chat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lionmgtllcagencyp.chat.databinding.ItemContactBinding
import com.lionmgtllcagencyp.chat.listeners.ContactsClickListener
import com.lionmgtllcagencyp.chat.utilities.Contacts

class ContactsAdapter(
    val contacts:ArrayList<Contacts>
    ):RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    private var clickListener:ContactsClickListener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(inflater,parent,false)
        return ContactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.onBind(contacts[position],clickListener)
        holder.itemView.setOnClickListener {
            clickListener?.onContactClicked(contacts[position].name,contacts[position].phone)
        }
    }

    fun setOnItemClickListener(listener: ContactsClickListener){
        clickListener = listener
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    class ContactsViewHolder(private val binding:ItemContactBinding):RecyclerView.ViewHolder(binding.root){
        fun onBind (contact:Contacts,listener:ContactsClickListener?){
            binding.contactName.text  = contact.name
            binding.contactNumber.text = contact.phone
        }
    }
}