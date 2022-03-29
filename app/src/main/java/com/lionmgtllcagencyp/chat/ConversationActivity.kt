package com.lionmgtllcagencyp.chat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.lionmgtllcagencyp.chat.adapters.ConversationAdapter
import com.lionmgtllcagencyp.chat.databinding.ActivityConversationBinding
import com.lionmgtllcagencyp.chat.utilities.*

class ConversationActivity : AppCompatActivity() {
    companion object {
        private val PARAM_CHAT_ID = "Chat id"
        private val PARAM_IMAGE_URL = "Image url"
        private val PARAM_OTHER_USER_ID = "Other user id"
        private val PARAM_CHAT_NAME = "Chat name"

        fun newIntent(context:Context,chatId:String?,imageUrl:String?,otherUserId:String?,chatName: String?): Intent {
            val intent = Intent(context,ConversationActivity::class.java)
            intent.putExtra(PARAM_CHAT_ID,chatId)
            intent.putExtra(PARAM_IMAGE_URL,imageUrl)
            intent.putExtra(PARAM_OTHER_USER_ID,otherUserId)
            intent.putExtra(PARAM_CHAT_NAME,chatName)
            return intent
        }
    }
    lateinit var binding:ActivityConversationBinding
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val firebaseDatabase = FirebaseFirestore.getInstance()
    private val conversationAdapter = ConversationAdapter(arrayListOf(),userId)

    lateinit var chatId: String
    lateinit var imageUrl:String
    lateinit var otherUserId:String
    lateinit var chatName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivityConversationBinding.inflate(inflater)
        setContentView(binding.root)

        chatId = intent.getStringExtra(PARAM_CHAT_ID).toString()
        imageUrl = intent.getStringExtra(PARAM_IMAGE_URL).toString()
        otherUserId = intent.getStringExtra(PARAM_OTHER_USER_ID).toString()
        chatName = intent.getStringExtra(PARAM_CHAT_NAME).toString()

        if(chatId.isNullOrEmpty() || userId.isNullOrEmpty()){
            finish()
        }

        binding.apply {

            topNameTextView.text = chatName
            populateImage(this@ConversationActivity,imageUrl,chatTopPhotoImage,R.drawable.default_user)


            messagesRecyclerView.apply {
                adapter = conversationAdapter
                layoutManager = LinearLayoutManager(this@ConversationActivity)

                setHasFixedSize(false)
            }
        }

        firebaseDatabase.collection(DATA_CHATS).document(chatId).collection(DATA_CHAT_MESSAGES).orderBy(
            DATA_CHAT_MESSAGE_TIME).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException != null){
                    firebaseFirestoreException.printStackTrace()
                    return@addSnapshotListener
                }else{
                    if(querySnapshot != null){
                        for(change in querySnapshot.documentChanges){
                            when(change.type){
                                DocumentChange.Type.ADDED -> {
                                    val message = change.document.toObject(Message::class.java)
                                    if(message != null) {
                                        conversationAdapter.addMessage(message)
                                        binding.apply {
                                            messagesRecyclerView.post {
                                                messagesRecyclerView.smoothScrollToPosition(
                                                    conversationAdapter.itemCount - 1
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    fun onSend(view: View){
        if(!binding.messageEditText.text.isNullOrEmpty()){
            val message = Message(userId,binding.messageEditText.text.toString(),System.currentTimeMillis())
            firebaseDatabase.collection(DATA_CHATS)
                .document(chatId)
                .collection(DATA_CHAT_MESSAGES)
                .document()
                .set(message)

            binding.messageEditText.setText("",TextView.BufferType.EDITABLE)
        }
    }
}