package com.example.clubapplication.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.clubapplication.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    val db = Firebase.firestore
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setContentView(R.layout.signup_main)

        val buttonRegister: Button = findViewById(R.id.buttonSignin)
        val username: TextView = findViewById(R.id.si_username)
        val password: TextView = findViewById(R.id.si_password)
        val fname = findViewById<TextView>(R.id.re_fullName)
        val email = findViewById<TextView>(R.id.re_Email)
        val matrixId = findViewById<TextView>(R.id.re_matrixId)
        val phoneNo = findViewById<TextView>(R.id.re_phone)
        val lay: ConstraintLayout = findViewById(R.id.layoutsignup)
        buttonRegister.setOnClickListener {
            val v = it
            if (!valid()) {
                return@setOnClickListener
            } else {
                // Submit register profile to db
                val data = hashMapOf(
                    "username" to username.text.toString(),
                    "password" to password.text.toString(),
                    "full name" to fname.text.toString(),
                    "email" to email.text.toString(),
                    "matrixId" to matrixId.text.toString(),
                    "phone no" to phoneNo.text.toString()
                )

                db.collection("user").document(username.text.toString())
                    .set(data)
                    .addOnSuccessListener {
                        Log.d("bomoh", "Register Complete")
                        Toast.makeText(applicationContext, "Register Complete", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                    .addOnFailureListener { e ->
                        Snackbar.make(v, "Register Failed $e", Snackbar.LENGTH_SHORT)
                    }
            }
        }
        lay.setOnClickListener {
            closeKeyBoard(it)
        }
    }

    private fun valid(): Boolean {
        var valid = true
        val username = findViewById<TextView>(R.id.si_username)
        val password = findViewById<TextView>(R.id.si_password)
        val confirmPassword = findViewById<TextView>(R.id.re_confirmPassword)
        val fname = findViewById<TextView>(R.id.re_fullName)
        val email = findViewById<TextView>(R.id.re_Email)
        val matrixId = findViewById<TextView>(R.id.re_matrixId)
        val phoneNo = findViewById<TextView>(R.id.re_phone)

        if (username.text.toString().isEmpty()) {
            username.error = "enter username"
            valid = false
        } else {
            username.error = null
        }

        if (password.text.toString().isEmpty()) {
            password.error = "enter password"
            valid = false
        } else {
            if (password.text.toString().length < 5) {
                password.error = "password must be more than 5"
                valid = false
            }
            password.error = null
        }

        if (confirmPassword.text.toString().isEmpty()) {
            confirmPassword.error = "re-enter password"
            valid = false
        } else {
            confirmPassword.error = null
            // check if the password == confirm password or not
            if (password.text.toString() != confirmPassword.text.toString()) {
                confirmPassword.error = "enter the same password"
                valid = false
            } else {
                confirmPassword.error = null
            }
        }

        if (fname.text.toString().isEmpty()) {
            fname.error = "enter full name"
            valid = false
        } else {
            fname.error = null
        }

        if (email.text.toString().isEmpty()) {
            email.error = "enter email"
            valid = false
        } else {
            email.error = null
        }

        if (matrixId.text.toString().isEmpty()) {
            matrixId.error = "enter matrix id"
            valid = false
        } else {
            if (matrixId.text.toString().length < 12) {
                matrixId.error = "enter valid matrix id"
                valid = false
            } else {
                matrixId.error = null
            }
        }

        if (phoneNo.text.toString().isEmpty()) {
            phoneNo.error = "enter phone no"
            valid = false
        } else {
            if (phoneNo.text.toString().length < 10) {
                phoneNo.error = "enter valid phone no"
                valid = false
            } else {
                phoneNo.error = null
            }
        }

        return valid

    }

    private fun closeKeyBoard(v : View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

}
