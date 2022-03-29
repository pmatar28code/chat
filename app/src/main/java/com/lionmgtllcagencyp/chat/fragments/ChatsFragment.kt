package com.lionmgtllcagencyp.chat.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lionmgtllcagencyp.chat.ConversationActivity
import com.lionmgtllcagencyp.chat.R
import com.lionmgtllcagencyp.chat.adapters.ChatsAdapter
import com.lionmgtllcagencyp.chat.databinding.FragmentChatsBinding
import com.lionmgtllcagencyp.chat.listeners.ChatClickListener
import com.lionmgtllcagencyp.chat.listeners.FailureCallback
import com.lionmgtllcagencyp.chat.utilities.Chat
import com.lionmgtllcagencyp.chat.utilities.DATA_CHATS
import com.lionmgtllcagencyp.chat.utilities.DATA_USERS
import com.lionmgtllcagencyp.chat.utilities.DATA_USER_CHATS


class ChatsFragment : Fragment(R.layout.fragment_chats),ChatClickListener {
    private lateinit var binding: FragmentChatsBinding
    private lateinit var chatsAdapter:ChatsAdapter
    private var firebaseDatabase = FirebaseFirestore.getInstance()
    private var userId = FirebaseAuth.getInstance().currentUser?.uid
    private var failureCallback:FailureCallback ?= null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatsBinding.bind(view)


        if(userId.isNullOrEmpty()){
            failureCallback?.onUserError()
        }

        chatsAdapter = ChatsAdapter(arrayListOf())
        chatsAdapter.setOnItemClickListener(this)

        binding.chatsRecyclerView.apply {
            setHasFixedSize(false)
            adapter = chatsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )

        }

        firebaseDatabase.collection(DATA_USERS).document(userId!!).addSnapshotListener { value, error ->
            if(error == null){
                refreshChats()
            }
        }
    }

    fun setFailureCallbackListener(listener:FailureCallback){
        failureCallback = listener
    }

    private fun refreshChats(){
        firebaseDatabase.collection(DATA_USERS).document(userId!!)
            .get().addOnSuccessListener {
                if(it.contains(DATA_USER_CHATS)){
                    val partners = it[DATA_USER_CHATS]
                    val chats = arrayListOf<String>()
                    for(partner in (partners as HashMap<String, String>).keys){
                        if(partners[partner] != null) {
                            chats.add(partners[partner]!!)
                            Log.e("CHATS LIST TO SUBMIT","$chats")
                        }
                    }
                    Log.e("CHATS LIST TO SUBMIT","$chats")
                    chatsAdapter.updateChats(chats)
                }
            }
    }

    fun newChat(partnerId:String){
        firebaseDatabase.collection(DATA_USERS).document(userId!!).get()
            .addOnSuccessListener { userDocument ->
                val userChatPartners = hashMapOf<String,String>()
                if(userDocument[DATA_USER_CHATS] != null && userDocument[DATA_USER_CHATS] is HashMap<*,*>){
                    val userDocumentMap = userDocument[DATA_USER_CHATS] as HashMap<String, String>
                    if(userDocumentMap.contains(partnerId)){
                       return@addOnSuccessListener
                    }else{
                        userChatPartners.putAll(userDocumentMap)
                    }
                }
                firebaseDatabase.collection(DATA_USERS).document(partnerId).get()
                    .addOnSuccessListener { partnerDocument ->
                        val partnerChatsPartners = hashMapOf<String,String>()
                        if(partnerDocument[DATA_USER_CHATS] != null && partnerDocument[DATA_USER_CHATS] is HashMap<*,*>){
                            val partnerDocumentMap = partnerDocument[DATA_USER_CHATS] as HashMap<String,String>
                            partnerChatsPartners.putAll(partnerDocumentMap)
                        }
                        val chatParticipants = arrayListOf(userId,partnerId)
                        val chat = Chat(chatParticipants)
                        val chatRef = firebaseDatabase.collection(DATA_CHATS).document()
                        val userRef = firebaseDatabase.collection(DATA_USERS).document(userId!!)
                        val partnerRef = firebaseDatabase.collection(DATA_USERS).document(partnerId)

                        userChatPartners[partnerId] = chatRef.id
                        partnerChatsPartners[userId!!] = chatRef.id

                        val batch = firebaseDatabase.batch()
                        batch.set(chatRef,chat)
                        batch.update(userRef, DATA_USER_CHATS,userChatPartners)
                        batch.update(partnerRef, DATA_USER_CHATS,partnerChatsPartners)
                        batch.commit()
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                    }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    override fun onChatClicked(
        chatId: String,
        otherUserId: String,
        chatImageUrl: String,
        chatName: String
    ) {
        startActivity(ConversationActivity.newIntent(requireContext(),chatId,chatImageUrl,otherUserId,chatName))
    }


}