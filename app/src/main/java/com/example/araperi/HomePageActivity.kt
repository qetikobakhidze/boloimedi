package com.example.araperi

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth


class HomePageActivity : AppCompatActivity() {

    private lateinit var addButton: FloatingActionButton
    private lateinit var logoutButton: Button
    private lateinit var inputNote: EditText
    private lateinit var textVieW: TextView
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        mAuth = FirebaseAuth.getInstance()
        inputNote = findViewById(R.id.editTextName)
        textVieW = findViewById(R.id.noteTextView)

        val sharedPref = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)

        val savedText = sharedPref.getString("NOTE","")
        textVieW.text = savedText
        addButton = findViewById(R.id.addButton)
        logoutButton = findViewById(R.id.logoutButton)


        logoutButton.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        addButton.setOnClickListener {
            val input = inputNote.text.toString()
            if (!TextUtils.isEmpty(input)) {

                val text = textVieW.text.toString()

                val resultText = input + "\n" + text

                textVieW.text = resultText

                inputNote.setText("")

                sharedPref.edit().putString("NOTE", resultText).apply()
            }
        }


    }
}