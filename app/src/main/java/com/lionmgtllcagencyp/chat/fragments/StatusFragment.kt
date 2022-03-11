package com.lionmgtllcagencyp.chat.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lionmgtllcagencyp.chat.R
import com.lionmgtllcagencyp.chat.databinding.FragmentStatusBinding


class StatusFragment : Fragment(R.layout.fragment_status) {
    private lateinit var binding: FragmentStatusBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStatusBinding.bind(view)

    }
}