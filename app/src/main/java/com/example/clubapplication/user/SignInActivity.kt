package com.example.clubapplication.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.clubapplication.MainActivity
import com.example.clubapplication.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity(){
    val db = Firebase.firestore
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setContentView(R.layout.signin_main)

        val buttonSignin: Button = findViewById(R.id.buttonSignin)
        val username: TextView = findViewById(R.id.si_username)
        val password: TextView = findViewById(R.id.si_password)
        val si_register: TextView = findViewById(R.id.si_register)
        val lay: ConstraintLayout = findViewById(R.id.layoutsignin)

        buttonSignin.setOnClickListener { v ->
            Log.d("bomoh", "signin pressed")
            if (valid()) {
                db.collection("user").document(username.text.toString())
                    .get()
                    .addOnSuccessListener {
                        Log.d("bomoh", "success")
                        val uid = it.getField<String>("username").toString()
                        if (uid == username.text.toString()) {
                            val pass = it.getField<String>("password").toString()
                            if (pass == password.text.toString()) {
                                Log.d("bomoh", "start")
                                val intent = Intent(this, MainActivity::class.java)
                                intent.putExtra("username", username.text.toString())
                                startActivity(intent)
                                finish()
                            } else {
                                Snackbar.make(v, "Wrong Password or Username", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                        Snackbar.make(v, "Wrong Password or Username", Snackbar.LENGTH_SHORT).show()
                    }
            }
        }

        si_register.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        lay.setOnClickListener {
            closeKeyBoard(it)
        }
    }

    private fun valid(): Boolean {
        var valid = true
        val username: TextView = findViewById(R.id.si_username)
        val password: TextView = findViewById(R.id.si_password)

        if (username.text.toString().isEmpty()) {
            username.error = "Required"
            valid = false
        } else {
            username.error = null
        }

        if (password.text.toString().isEmpty()) {
            password.error = "Required"
            valid = false
        } else {
            password.error = null
        }

        return valid
    }

    private fun closeKeyBoard(v : View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }
}