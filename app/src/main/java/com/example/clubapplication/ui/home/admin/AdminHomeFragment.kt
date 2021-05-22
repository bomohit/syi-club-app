package com.example.clubapplication.ui.home.admin

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clubapplication.R
import com.example.clubapplication.ui.home.ListClub
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class AdminHomeFragment : Fragment() {
    val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_admin_home, container, false)
        val recyclerView : RecyclerView = root.findViewById(R.id.requestRecyclerView)
        val listRequest = mutableListOf<ListRequest>()

        fun rv() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@AdminHomeFragment.context)
                adapter = RequestList(listRequest)
            }
        }

        // get request list from db
        fun getData() {
            db.collection("request")
                .get()
                .addOnSuccessListener { results ->
                    for (result in results) {
                        val uid = result.id
                        val club_id = result.getField<String>("club_id").toString()
                        db.collection("list club").document(club_id)
                            .get()
                            .addOnSuccessListener {
                                val club_name = it.getField<String>("title").toString()
                                db.collection("user").document(uid)
                                    .get()
                                    .addOnSuccessListener { t ->
                                        val name = t.getField<String>("full name").toString()
                                        val matrix = t.getField<String>("matrixId").toString()
                                        listRequest.add(ListRequest(name, club_name, matrix, club_id, uid))
                                        rv()
                                    }
                            }
                    }
                }
        }

        val docRef = db.collection("request")
        docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                listRequest.clear()
                getData()
            }
        }

        return root
    }
}

data class ListRequest (
    val name : String,
    val title : String,
    val matrix : String,
    val club_id : String,
    val uid: String
)
