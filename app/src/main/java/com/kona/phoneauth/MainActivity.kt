package com.kona.phoneauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthProvider

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        val signOutBtn = findViewById<Button>(R.id.logout)
        val userNumber = findViewById<TextView>(R.id.phone_number_txt)
        val idUser = findViewById<TextView>(R.id.uid_txt)

        val numberPhone = "Numero: " + mAuth.currentUser!!.phoneNumber
        val uid = "UID: " + mAuth.uid
        userNumber.text = numberPhone
        idUser.text = uid

        signOutBtn.setOnClickListener {

            mAuth.signOut()
            sendLogin()
        }

    }

    private fun sendLogin() {
        val intent = Intent(this,PhoneNumberActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

}