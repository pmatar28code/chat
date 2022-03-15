package com.lionmgtllcagencyp.chat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lionmgtllcagencyp.chat.R
import com.lionmgtllcagencyp.chat.adapters.ChatsAdapter
import com.lionmgtllcagencyp.chat.databinding.FragmentChatsBinding
import com.lionmgtllcagencyp.chat.listeners.ChatClickListener


class ChatsFragment : Fragment(R.layout.fragment_chats),ChatClickListener {
    private lateinit var binding: FragmentChatsBinding
    private lateinit var chatsAdapter:ChatsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatsBinding.bind(view)
        val chatIdsList = arrayListOf(
            "Chat 1","Chat 2","Chat 3",
            "Chat 4","Chat 5","Chat 6",
            "Chat 7","Chat 8","Chat 9"
        )

        chatsAdapter = ChatsAdapter(arrayListOf())
        chatsAdapter.setOnItemClickListener(this)

        binding.chatsRecyclerView.apply {
            setHasFixedSize(true)
            adapter = chatsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )

        }

        chatsAdapter.updateChats(chatIdsList)

    }

    override fun onChatClicked(
        name: String,
        otherUserId: String,
        chatImageUrl: String,
        chatName: String
    ) {
        Toast.makeText(requireContext(),"$name Clicked",Toast.LENGTH_SHORT).show()
    }


}