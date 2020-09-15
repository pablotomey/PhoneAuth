package com.kona.phoneauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class CodeNumberActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var loadBar: ProgressBar
    //private var mVerificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_number)

        val codeTxt = findViewById<EditText>(R.id.codeTxt)
        val codeBtn = findViewById<Button>(R.id.codeSend)
        loadBar = findViewById(R.id.load_bar_code)

        val mVerificationId = intent.getStringExtra("AuthCredential")

        mAuth = FirebaseAuth.getInstance()

        codeBtn.setOnClickListener {
            val code = codeTxt.text.toString()
            if (code.isEmpty()){
                Toast.makeText(this,"Favor ingrese el codigo de verificación", Toast.LENGTH_SHORT).show()
            } else {
                loadBar.visibility = View.VISIBLE

                val credential = PhoneAuthProvider.getCredential(mVerificationId!!,code)
                signInWithPhoneAuthCredential(credential)
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                sendHome()
            } else if (task.exception is FirebaseAuthInvalidCredentialsException){
                Toast.makeText(this,"Código invalido", Toast.LENGTH_SHORT).show()
            }
            loadBar.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()

        if (mAuth.currentUser != null){
            sendHome()
        }
    }

    private fun sendHome(){
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}