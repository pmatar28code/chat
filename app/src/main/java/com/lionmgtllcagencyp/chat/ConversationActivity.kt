package com.lionmgtllcagencyp.chat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lionmgtllcagencyp.chat.adapters.ConversationAdapter
import com.lionmgtllcagencyp.chat.databinding.ActivityConversationBinding
import com.lionmgtllcagencyp.chat.utilities.DATA_USERS
import com.lionmgtllcagencyp.chat.utilities.Message

class ConversationActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context:Context): Intent {
            val intent = Intent(context,ConversationActivity::class.java)
            return intent
        }
    }
    lateinit var binding:ActivityConversationBinding
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val firebaseDatabase = FirebaseFirestore.getInstance()
    private val conversationAdapter = ConversationAdapter(arrayListOf(),userId)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivityConversationBinding.inflate(inflater)
        setContentView(binding.root)

        binding.apply {
            

            messagesRecyclerView.apply {
                adapter = conversationAdapter
                layoutManager = LinearLayoutManager(this@ConversationActivity)

                setHasFixedSize(false)
            }
        }

        conversationAdapter.addMessage(Message(userId,"Hello this is a test pedro",1))
        conversationAdapter.addMessage(Message("otherUserID","Hi this is other user",2))
        conversationAdapter.addMessage(Message(userId,"Hi other user nice app",3))
        conversationAdapter.addMessage(Message("otherUserID","Hi pedro yes it is a nice app",4))
    }

    fun onSend(view: View){

    }
}