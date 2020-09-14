package com.kona.phoneauth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneNumberActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var mcallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)

        val phoneNumberTxt = findViewById<EditText>(R.id.phone_number_txt)
        val checkBtn = findViewById<Button>(R.id.phoneCheck)

        checkBtn.setOnClickListener {
            val phoneNum = phoneNumberTxt.text.toString()

            if (phoneNum.isEmpty()){
                Toast.makeText(this,"No ha ingresado un número",Toast.LENGTH_SHORT).show()
            }else {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNum,
                    60,
                    TimeUnit.SECONDS,
                    this,
                    mcallback
                )
            }
        }

        mcallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

                Toast.makeText(applicationContext,"Error en la verificación",Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verificationId, token)
                val intent = Intent(applicationContext,CodeNumberActivity::class.java)
                intent.putExtra("AuthCredential", verificationId)
                startActivity(intent)
            }

        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                sendHome()
            }else if (task.exception is FirebaseAuthInvalidCredentialsException){
                Toast.makeText(this,"Error en la verificación",Toast.LENGTH_SHORT).show()
            }
            // progress bar off
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