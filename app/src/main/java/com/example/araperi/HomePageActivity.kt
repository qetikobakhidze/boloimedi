package com.example.araperi

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class HomePageActivity : AppCompatActivity() {

    private lateinit var addButton: FloatingActionButton
    private lateinit var logoutButton: Button
    private lateinit var inputNote: EditText
    private lateinit var textVieW: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("UserInfo")
        inputNote = findViewById(R.id.nameEditText)
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


            val name = inputNote.text.toString()
            val personInfo = PersonInfo(name)
            if (mAuth.currentUser?.uid != null) {
                db.child(mAuth.currentUser?.uid!!).setValue(personInfo).addOnCompleteListener{ task ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
                        inputNote.text = null
                    }else {
                        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            if (mAuth.currentUser?.uid != null) {
                db.child(mAuth.currentUser?.uid!!)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@HomePageActivity, "Error!", Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val p = snapshot.getValue(PersonInfo::class.java)
                            if (p != null) {
                                textVieW.text = p.name
                            }

                        }


                    })
            }
        }


    }
}