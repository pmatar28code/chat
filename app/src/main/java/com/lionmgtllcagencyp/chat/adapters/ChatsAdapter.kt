package com.lionmgtllcagencyp.chat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lionmgtllcagencyp.chat.R
import com.lionmgtllcagencyp.chat.databinding.ItemChatBinding
import com.lionmgtllcagencyp.chat.listeners.ChatClickListener
import com.lionmgtllcagencyp.chat.utilities.populateImage

class ChatsAdapter(var chats:ArrayList<String>):RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {
    private var clickLister: ChatClickListener?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChatBinding.inflate(inflater,parent,false)
        return ChatsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.onBind(chats[position],clickLister)
        holder.itemView.setOnClickListener {
            clickLister?.onChatClicked(chatName = chats[position], name = "HERE GOES NAME", chatImageUrl = "IMAGE URL", otherUserId = "OTHER USER")
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    fun setOnItemClickListener(listener:ChatClickListener){
        clickLister = listener
        notifyDataSetChanged()
    }

    fun updateChats(updatedChatsList:ArrayList<String>){
        chats.clear()
        chats.addAll(updatedChatsList)
        notifyDataSetChanged()
    }

    class ChatsViewHolder(
        val binding: ItemChatBinding
        ):RecyclerView.ViewHolder(binding.root){
            fun onBind(chatId:String,listener: ChatClickListener?){
                binding.apply {
                    chatImage.setImageResource(R.drawable.default_user)
                    //populateImage(chatImage.context,"",chatImage, R.drawable.default_user)
                    chatTextView.text = chatId
                }
            }

    }
}