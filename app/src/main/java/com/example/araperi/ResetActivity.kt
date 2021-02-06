package com.example.araperi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ResetActivity : AppCompatActivity() {

    private lateinit var resetEmail: EditText
    private lateinit var sendEmail: Button
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)

        mAuth = FirebaseAuth.getInstance()
        resetEmail = findViewById(R.id.resetEmailEditText)
        sendEmail = findViewById(R.id.sendEmailButton)

        sendEmail.setOnClickListener {

            val RE = resetEmail.text.toString()

            if (RE.isEmpty()){

                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()

            } else {

                mAuth.sendPasswordResetEmail(RE).addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        mAuth.signOut()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }


    }
}

