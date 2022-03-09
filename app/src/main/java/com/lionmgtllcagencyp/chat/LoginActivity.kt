package com.lionmgtllcagencyp.chat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.lionmgtllcagencyp.chat.databinding.ActivityLoginBinding
import com.lionmgtllcagencyp.chat.databinding.ActivityMainBinding
import com.lionmgtllcagencyp.chat.databinding.FragmentMainBinding

class LoginActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    private lateinit var binding: ActivityLoginBinding
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseAuthStateListener = FirebaseAuth.AuthStateListener {
        val user = it.currentUser?.uid
        if(user != null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = LayoutInflater.from(this)
        binding = ActivityLoginBinding.inflate(inflater)
        setContentView(binding.root)

        binding.apply {
            setTextChangeListener(emailTextInputLayout)
            setTextChangeListener(passwordTextInputLayout)
            progressLayout.setOnTouchListener { view, motionEvent -> true }
        }
    }

    fun setTextChangeListener(textInputLayout: TextInputLayout){
        textInputLayout.editText?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                textInputLayout.isErrorEnabled = false
            }

        })
    }

    fun onLogIn(view: View) {
        binding.apply {
            var proceed = true
            if (emailTextInputLayout.editText?.text.isNullOrEmpty() ) {
                emailTextInputLayout.error="Email is required"
                emailTextInputLayout.isErrorEnabled = true
                proceed = false
            }

            if(passwordTextInputLayout.editText?.text.isNullOrEmpty()){
                passwordTextInputLayout.error = "Password is required"
                emailTextInputLayout.isErrorEnabled = true
                proceed = false
            }

            if(proceed){
                progressLayout.visibility = View.VISIBLE
                firebaseAuth.signInWithEmailAndPassword(
                    emailTextInputLayout.editText?.text.toString(),
                    passwordTextInputLayout.editText?.text.toString()
                )
                    .addOnCompleteListener {
                    if(!it.isSuccessful){
                        progressLayout.visibility = View.GONE
                        Toast.makeText(
                            this@LoginActivity,
                            "SignUp Error ${it.exception?.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                    .addOnFailureListener { e ->
                        progressLayout.visibility = View.GONE
                        e.printStackTrace()
                    }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthStateListener)
    }

    fun onSignUp(view: View) {
        startActivity(SignupActivity.newIntent(this))
        finish()
    }


}