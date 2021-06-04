package com.example.clubapplication.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.clubapplication.R
import com.example.clubapplication.viewmodel.loginViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    val db = Firebase.firestore
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val fname = root.findViewById<TextView>(R.id.pr_name)
        val phone = root.findViewById<TextView>(R.id.pr_phone)
        val email = root.findViewById<TextView>(R.id.pr_email)
        val matrix = root.findViewById<TextView>(R.id.pr_id)

        // get current user username
        var viewModel = ViewModelProvider(requireActivity()).get(loginViewModel::class.java)

        db.collection("user").document(viewModel.id.toString())
            .get()
            .addOnSuccessListener {
                fname.text = it.getField<String>("full name").toString()
                phone.text = it.getField<String>("phone no").toString()
                email.text = it.getField<String>("email").toString()
                matrix.text = it.getField<String>("matrixId").toString()
            }


        return root
    }
}