package com.example.clubapplication.ui.home

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clubapplication.R
import com.example.clubapplication.viewmodel.loginViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    val db = Firebase.firestore
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val listClub = mutableListOf<ListClub>()
        // get current user username
        var viewModel = ViewModelProvider(requireActivity()).get(loginViewModel::class.java)

        val recyclerView : RecyclerView = root.findViewById(R.id.clubListRecyclerView)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        fun rv() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@HomeFragment.context)
                adapter = ClubList(listClub)
            }
        }

        db.collection("list club")
            .get()
            .addOnSuccessListener { result ->
                for (results in result) {
                    val title = results.getField<String>("title").toString()
                    val club_id = results.id
                    d("bomoh", title + club_id)
                    listClub.add(ListClub(title, club_id))
                }
                rv()
            }

        return root
    }
}

data class ListClub (
    val name : String,
    val club_id : String
)
