package com.lionmgtllcagencyp.chat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    companion object{
        fun newIntent(context: Context) = Intent(context,LoginActivity::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onLogIn(view: View){
        Toast.makeText(this,"Login Clicked",Toast.LENGTH_SHORT).show()
    }
    fun onSignUp(view: View){
        startActivity(SignupActivity.newIntent(this))
        finish()
    }


}