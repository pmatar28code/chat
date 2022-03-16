package com.lionmgtllcagencyp.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.lionmgtllcagencyp.chat.databinding.ActivityConversationBinding

class ConversationActivity : AppCompatActivity() {
    lateinit var binding:ActivityConversationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivityConversationBinding.inflate(inflater)
        setContentView(binding.root)


    }

    fun onSend(view: View){

    }
}