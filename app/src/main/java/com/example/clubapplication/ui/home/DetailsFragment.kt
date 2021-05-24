package com.example.clubapplication.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.clubapplication.R
import com.example.clubapplication.viewmodel.loginViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase


class DetailsFragment : Fragment() {
    val db = Firebase.firestore

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_nav_club_details, container, false)
        val club_id = arguments?.getString("club_id").toString()
        val joinButton = root.findViewById<Button>(R.id.cd_requestJoin)
        val joinPop : ConstraintLayout = root.findViewById(R.id.joinBarLayout)
        val cd_loading : ConstraintLayout = root.findViewById(R.id.cd_loadingLayout)
        // get current user username
        var viewModel = ViewModelProvider(requireActivity()).get(loginViewModel::class.java)

        // get detail from db
        db.collection("list club").document(club_id)
            .get()
            .addOnSuccessListener {
                root.findViewById<TextView>(R.id.de_title).text = it.getField<String>("title").toString()
                root.findViewById<TextView>(R.id.de_description).text = it.getField<String>("description").toString()
            }
        // when button join pressed
        joinButton.setOnClickListener {
            val data = hashMapOf(
                "club_id" to club_id
            )
            db.collection("request").document(viewModel.id.toString())
                .set(data)
            it.findNavController().navigate(R.id.action_nav_club_details_to_nav_payment2)
        }

        // check if user already join or not
        // if join hide join Pop else show join Pop
        db.collection("user").document(viewModel.id.toString()).collection("club")
            .get()
            .addOnSuccessListener { results ->
                for (result in results) {
                    val title = result.getField<String>("title").toString()
                    val cl_id = result.id
                    if (cl_id == club_id) {
                        joinPop.isVisible = false
                    }
                }
            }

        // if status requesting
        db.collection("request").document(viewModel.id.toString())
            .get()
            .addOnSuccessListener {
                val title = it.getField<String>("club_id").toString()
                val uid = it.id
                if (uid == viewModel.id.toString()) {
                    joinButton.isEnabled = false
                    joinButton.text = "Waiting Approval"
                }
                cd_loading.isGone = true
            }

        return root
    }
}