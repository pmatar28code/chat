package com.lionmgtllcagencyp.chat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.lionmgtllcagencyp.chat.databinding.ActivityMainBinding
import com.lionmgtllcagencyp.chat.databinding.FragmentMainBinding


class MainActivity : AppCompatActivity() {
    companion object{
        fun newIntent(context: Context) = Intent(context,MainActivity::class.java)
    }
    var sectionPagerAdapter: SectionPagerAdapter ?= null
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this@MainActivity)
        binding = ActivityMainBinding.inflate(inflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        sectionPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        binding.container.adapter = sectionPagerAdapter

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view,"this is a simple snackBar",Snackbar.LENGTH_SHORT)
                .setAction("Action",null) .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.action_settings){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    inner class SectionPagerAdapter(fragmentManager:FragmentManager): FragmentPagerAdapter(fragmentManager) {
        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {
            return  PlaceHolderFragment.newIntent(position + 1)
        }

    }
    class PlaceHolderFragment: Fragment(R.layout.fragment_main){
        companion object{
            private val ARG_SECTION_NUMBER = "Section Number"
            fun newIntent(sectionNumber:Int):PlaceHolderFragment{
                val fragment = PlaceHolderFragment()
                val args = Bundle()
                    args.putInt(ARG_SECTION_NUMBER,sectionNumber)
                fragment.arguments = args
                return fragment

            }
        }
        lateinit var bindingFrag: FragmentMainBinding
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            bindingFrag = FragmentMainBinding.bind(view)
            super.onViewCreated(view, savedInstanceState)

            bindingFrag.sectionLabel.text = "Hello World from Section ${arguments?.getInt(ARG_SECTION_NUMBER)}"
        }

    }
}