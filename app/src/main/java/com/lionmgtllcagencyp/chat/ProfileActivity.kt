package com.lionmgtllcagencyp.chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.lionmgtllcagencyp.chat.databinding.ActivityProfileBinding
import com.lionmgtllcagencyp.chat.utilities.*
import java.lang.Exception

class ProfileActivity : AppCompatActivity() {
    companion object{
        fun newIntent(context: Context) = Intent(context,ProfileActivity::class.java)
    }
    private val firebaseDatabase = FirebaseFirestore.getInstance()

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val firebaseStorage = FirebaseStorage.getInstance().reference
    private var imageUrl:String ?= null
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = LayoutInflater.from(this)
        binding = ActivityProfileBinding.inflate(inflater)
        setContentView(binding.root)

        if(userId.isNullOrEmpty()){
            finish()
        }

        binding.apply {

            progressLayout.setOnTouchListener { view, motionEvent ->  true }
            profileImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE_PHOTO)
            }

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PHOTO){
            storeImage(data?.data.toString().toUri())
        }
    }

    private fun storeImage(imageUri: Uri){
        Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show()
        binding.progressLayout.visibility = View.VISIBLE
        val filePath = firebaseStorage.child(DATA_IMAGES).child(userId!!)
        filePath.putFile(imageUri)
            .addOnSuccessListener {
                filePath.downloadUrl
                    .addOnSuccessListener { uri ->
                        val url = uri.toString()
                        firebaseDatabase.collection(DATA_USERS).document(userId).update(
                            DATA_USER_IMAGE_URL,
                            url
                        )
                            .addOnSuccessListener {
                                imageUrl = url
                                populateImage(this, imageUrl!!,binding.profileImage,R.drawable.default_user)
                            }
                        binding.progressLayout.visibility = View.GONE
                    }
                    .addOnFailureListener {
                        uploadFailure(it)
                    }

            }
            .addOnFailureListener {
                uploadFailure(it)
            }
    }

    private fun uploadFailure(e: Exception){
        Toast.makeText(this,"Image upload failed, please try again later $e",Toast.LENGTH_LONG).show()
        Log.e("FIREBASE IMAGE STORAGE ERROR","$e")
        binding.progressLayout.visibility = View.GONE
    }

    private fun populateInfo(){
        binding.apply {
            progressLayout.visibility = View.VISIBLE
            if (userId != null) {
                firebaseDatabase.collection(DATA_USERS)
                    .document(userId).get()
                    .addOnSuccessListener {
                        val user =  it.toObject(User::class.java)
                        nameProfileTextInputLayout.editText?.setText(user?.name,TextView.BufferType.EDITABLE)
                        emailProfileTextInputLayout.editText?.setText(user?.email,TextView.BufferType.EDITABLE)
                        phoneProfileTextInputLayout.editText?.setText(user?.phone,TextView.BufferType.EDITABLE)
                        imageUrl = user?.imageUrl
                        if(imageUrl != null){
                            populateImage(this@ProfileActivity,user?.imageUrl!!,binding.profileImage,R.drawable.default_user)
                        }
                        progressLayout.visibility = View.GONE
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                        finish()
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            progressLayout.visibility = View.VISIBLE
            populateInfo()
        }
    }

    fun onApply(view: View){
        binding.apply {
            progressLayout.visibility = View.VISIBLE

            val name =  nameProfileTextInputLayout.editText?.text.toString()
            val email = emailProfileTextInputLayout.editText?.text.toString()
            val phone = phoneProfileTextInputLayout.editText?.text.toString()

            val user = User(
                name = name,
                email = email,
                phone = phone,
                imageUrl = "",
                status = "",
                statusUrl = "",
                statusTime = ""
            )

            val map = HashMap<String,Any>()
            map[DATA_USER_NAME] = name
            map[DATA_USER_EMAIL] = email
            map[DATA_USER_PHONE] = phone

            if (userId != null) {
                firebaseDatabase.collection(DATA_USERS).document(userId).update(map)
                    .addOnSuccessListener {
                        Toast.makeText(this@ProfileActivity,"Update Successful",Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this@ProfileActivity,"Update Failed $e",Toast.LENGTH_LONG).show()
                        progressLayout.visibility = View.GONE
                    }
            }
        }

    }

    fun onDelete(view:View){
        binding.apply {
            progressLayout.visibility = View.VISIBLE
            AlertDialog.Builder(this@ProfileActivity)
                .setTitle("Delete Account")
                .setMessage("This will delete your profile information. Are you sure?")
                .setPositiveButton("Yes") {dialog,which ->
                    Toast.makeText(this@ProfileActivity,"Profile Deleted",Toast.LENGTH_SHORT).show()
                    if (userId != null) {
                        firebaseDatabase.collection(DATA_USERS).document(userId).delete()
                    }
                    finish()
                }
                .setNegativeButton("No"){dialog,which->
                    binding.progressLayout.visibility = View.GONE
                }
                .show()

        }

    }
}