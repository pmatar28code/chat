package com.lionmgtllcagencyp.chat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lionmgtllcagencyp.chat.R
import com.lionmgtllcagencyp.chat.databinding.FragmentStatusUpdateBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class StatusUpdateFragment : Fragment(R.layout.fragment_status_update) {
    private lateinit var binding:FragmentStatusUpdateBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStatusUpdateBinding.bind(view)

        binding.statusUpdateTextHello
    }

}


