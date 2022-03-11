package com.lionmgtllcagencyp.chat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lionmgtllcagencyp.chat.R
import com.lionmgtllcagencyp.chat.databinding.FragmentChatsBinding


class ChatsFragment : Fragment(R.layout.fragment_chats) {
    private lateinit var binding: FragmentChatsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatsBinding.bind(view)

    }




}