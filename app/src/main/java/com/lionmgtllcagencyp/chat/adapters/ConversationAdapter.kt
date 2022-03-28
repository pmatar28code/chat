package com.lionmgtllcagencyp.chat.adapters

import android.renderscript.ScriptGroup
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lionmgtllcagencyp.chat.R
import com.lionmgtllcagencyp.chat.databinding.ActivityConversationBinding
import com.lionmgtllcagencyp.chat.databinding.ItemCurrentUserMessageBinding
import com.lionmgtllcagencyp.chat.databinding.ItemOtherUserMessageBinding
import com.lionmgtllcagencyp.chat.utilities.Message

class ConversationAdapter(
    private var messages:ArrayList<Message>,
    val userId:String?
    ):RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {

    companion object {
        val MESSAGE_CURRENT_USER = 1
        val MESSAGE_OTHER_USER = 2
    }
    var bindingCurrentUser: ItemCurrentUserMessageBinding ?= null
    var bindingOtherUser: ItemOtherUserMessageBinding ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        return if(viewType == MESSAGE_CURRENT_USER){
            val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_current_user_message,parent,false)
            ConversationViewHolder(inflater)
        }else{
            val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_other_user_message,parent,false)
            ConversationViewHolder(inflater)
        }
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.onBind(messages[position])

    }

    override fun getItemCount(): Int {
        return messages.size

    }

    override fun getItemViewType(position: Int): Int {
        return if(messages[position].sentBy == userId){
             MESSAGE_CURRENT_USER
        }else{
             MESSAGE_OTHER_USER
        }
    }

    class ConversationViewHolder(
        val view:View,
        ):RecyclerView.ViewHolder(view){
        fun onBind(message:Message){
            view.findViewById<TextView>(R.id.message_text_view).text = message.message
        }
    }

    fun addMessage(message: Message){
        messages.add(message)
        notifyDataSetChanged()
    }
}