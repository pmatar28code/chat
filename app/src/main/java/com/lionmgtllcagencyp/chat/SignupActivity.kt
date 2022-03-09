package com.lionmgtllcagencyp.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lionmgtllcagencyp.chat.databinding.ActivitySignupBinding
import com.lionmgtllcagencyp.chat.utilities.DATA_USERS
import com.lionmgtllcagencyp.chat.utilities.User
import java.time.LocalTime

class SignupActivity : AppCompatActivity() {
    companion object{
        fun newIntent(context: Context) = Intent(context,SignupActivity::class.java)
    }
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = it.currentUser?.uid
        if(user != null){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private val firebaseDatabase = FirebaseFirestore.getInstance()
    private lateinit var binding:ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = LayoutInflater.from(this)
        binding = ActivitySignupBinding.inflate(inflater)
        setContentView(binding.root)

        binding.apply {
            setTextChangeListener(nameTextInputLayout)
            setTextChangeListener(phoneTextInputLayout)
            setTextChangeListener(emailSignUpTextInputLayout)
            setTextChangeListener(passwordSignUpTextInputLayout)
            progressLayout.setOnTouchListener { view, motionEvent -> true }
        }

    }

    fun setTextChangeListener(textInputLayout: TextInputLayout){
        textInputLayout.editText?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                textInputLayout.isErrorEnabled = false
            }

        })
    }

    fun onSignUp(view: View){
        binding.apply {
            var proceed = true

            if (nameTextInputLayout.editText?.text.isNullOrEmpty() ) {
                nameTextInputLayout.error="Name is required"
                nameTextInputLayout.isErrorEnabled = true
                proceed = false
            }
            if (phoneTextInputLayout.editText?.text.isNullOrEmpty() ) {
                phoneTextInputLayout.error="Phone is required"
                phoneTextInputLayout.isErrorEnabled = true
                proceed = false
            }
            if (emailSignUpTextInputLayout.editText?.text.isNullOrEmpty() ) {
                emailSignUpTextInputLayout.error="Email is required"
                emailSignUpTextInputLayout.isErrorEnabled = true
                proceed = false
            }

            if(passwordSignUpTextInputLayout.editText?.text.isNullOrEmpty()){
                passwordSignUpTextInputLayout.error = "Password is required"
                passwordSignUpTextInputLayout.isErrorEnabled = true
                proceed = false
            }

            if(proceed){
                progressLayout.visibility = View.VISIBLE
                firebaseAuth.createUserWithEmailAndPassword(
                    emailSignUpTextInputLayout.editText?.text.toString(),
                    passwordSignUpTextInputLayout.editText?.text.toString()
                )
                    .addOnCompleteListener {
                        if(!it.isSuccessful){
                            progressLayout.visibility = View.GONE
                            Toast.makeText(
                                this@SignupActivity,
                                "Login Error ${it.exception?.localizedMessage}",
                                Toast.LENGTH_LONG
                            ).show()
                        }else if(firebaseAuth.uid != null){
                            val user = User(
                                email = emailSignUpTextInputLayout.editText?.text.toString(),
                                phone =phoneTextInputLayout.editText?.text.toString(),
                                name = nameTextInputLayout.editText?.text.toString(),
                                imageUrl ="",
                                status = "Hello, i'm new",
                                statusUrl = "",
                                statusTime = "",
                            )
                            firebaseDatabase.collection(
                                DATA_USERS
                            ).document(
                                firebaseAuth.uid!!
                            ).set(user)
                        }
                        progressLayout.visibility = View.GONE
                    }
                    .addOnFailureListener { e ->
                        progressLayout.visibility = View.GONE
                        e.printStackTrace()
                    }
            }
        }
    //startActivity(MainActivity.newIntent(this))
    }

    fun onLogIn(view: View){
        startActivity(LoginActivity.newIntent(this))
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

}