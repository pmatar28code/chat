package com.lionmgtllcagencyp.chat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lionmgtllcagencyp.chat.R
import com.lionmgtllcagencyp.chat.databinding.ItemChatBinding
import com.lionmgtllcagencyp.chat.listeners.ChatClickListener
import com.lionmgtllcagencyp.chat.utilities.*

class ChatsAdapter(var chats:ArrayList<String>):RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {
    private var clickLister: ChatClickListener?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChatBinding.inflate(inflater,parent,false)
        return ChatsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.onBind(chats[position],clickLister)

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
                binding.progressLayout.visibility = View.VISIBLE
                var partnerParticipantUserObject:User ?= null
                var partnerParticipantUserName = ""
                var partnerParticipantImageUrl = ""
                var partnerId = ""
                val firebaseDatabase = FirebaseFirestore.getInstance()
                var currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                    firebaseDatabase.collection(DATA_CHATS).document(chatId)
                    .get().addOnSuccessListener {
                        val chatParticipants = it[DATA_CHAT_PARTICIPANTS]
                            if(chatParticipants != null){
                                for(participant in chatParticipants as ArrayList<String>){
                                    if(participant != currentUserId){
                                        partnerId = participant
                                        firebaseDatabase.collection(DATA_USERS).document(partnerId).get()
                                            .addOnSuccessListener { partnerDocument ->
                                                partnerParticipantUserObject = partnerDocument.toObject(User::class.java)
                                                partnerParticipantUserName =
                                                    partnerParticipantUserObject?.name.toString()
                                                partnerParticipantImageUrl = partnerParticipantUserObject?.imageUrl.toString()
                                                populateImage(binding.chatImage.context,partnerParticipantUserObject?.imageUrl!!,binding.chatImage,R.drawable.default_user)
                                                binding.chatTextView.text = partnerParticipantUserName
                                                binding.progressLayout.visibility = View.GONE
                                            }
                                            .addOnFailureListener { e ->
                                                e.printStackTrace()
                                                binding.progressLayout.visibility = View.GONE
                                            }
                                    }
                                }
                            }

                    }
                        .addOnFailureListener {
                            it.printStackTrace()
                        }
                binding.itemChatsLayout.setOnClickListener {
                    listener?.onChatClicked(
                        chatId,
                        partnerId,
                        partnerParticipantImageUrl,
                        partnerParticipantUserName
                    )

                }

            }

    }
}