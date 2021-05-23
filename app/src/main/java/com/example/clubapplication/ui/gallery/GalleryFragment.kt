package com.example.clubapplication.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clubapplication.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    val db = Firebase.firestore
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProvider(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
//        val textView: TextView = root.findViewById(R.id.text_gallery)
//        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        val postList = mutableListOf<PostList>()

        val recyclerView : RecyclerView = root.findViewById(R.id.vn_recyclerView)
        fun rv() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@GalleryFragment.context)
                adapter = PostNews(postList)
            }
        }


        db.collection("posts")
            .get()
            .addOnSuccessListener { results ->
                for (result in results) {
                    val db_post = result.getField<String>("post").toString()
                    val db_image = result.getField<String>("image").toString()
                    val db_club_name = result.getField<String>("club name").toString()
                    postList.add(PostList(db_club_name, db_image, db_post))

                }
                rv()
            }


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

data class PostList (
    val club : String,
    val image : String,
    val post : String
)
