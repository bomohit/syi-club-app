package com.example.clubapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isGone
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.time.LocalDateTime

@Suppress("DEPRECATION")
class NewsPostFragment : Fragment(), AdapterView.OnItemSelectedListener {
    val db = Firebase.firestore
    private val pickImage = 100
    private var imageUri : Uri? = null
    var sel_club : String = ""
    var sel_name : String = ""
    private val storage = Firebase.storage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_news_post, container, false)
        val club_list: MutableList<String> = ArrayList()
        val spinner : Spinner = root.findViewById(R.id.spinnerSelectClub)
        val lay: FrameLayout = root.findViewById(R.id.nw_layout)

        lay.setOnClickListener { closeKeyBoard(it) }

        spinner.onItemSelectedListener = this
        club_list.add("Select Club ..")
        val adapter = ArrayAdapter (
                root.context,
                android.R.layout.simple_spinner_item,
                club_list
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        db.collection("list club")
            .get()
            .addOnSuccessListener { results ->
                for (result in results) {
                    val club_id = result.id
                    val title = result.getField<String>("title").toString()
                    club_list.add(title)
                    spinner.adapter = adapter
                }
            }


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.nw_imageSelect).setOnClickListener {
            val imgPick = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(imgPick, pickImage)
        }

        view.findViewById<Button>(R.id.nw_send).setOnClickListener {
            val post = view.findViewById<TextView>(R.id.nw_post)

            if (valid(post)) {
                //upload image
                val tms = LocalDateTime.now().toString()
                val storageRef = storage.reference.child(tms)
                if (imageUri != null) {
                    val uploadImg = storageRef.putFile(imageUri!!)
                    val urlTask = uploadImg.continueWithTask{ task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        storageRef.downloadUrl
                    }.addOnCompleteListener { task ->
                        val data = hashMapOf(
                            "post" to post.text.toString(),
                            "image" to task.result.toString(),
                            "club name" to sel_name
                        )
                        db.collection("posts").document(tms)
                            .set(data)
                        Snackbar.make(requireView(), "Post Success", Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    val data = hashMapOf(
                        "post" to post.text.toString(),
                        "club name" to sel_name
                    )
                    db.collection("posts").document(tms)
                        .set(data)
                    Snackbar.make(requireView(), "Post Success", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun valid(post: TextView): Boolean {
        var valid = true
        if (post.toString().isEmpty()) {
            post.error = "Required"
            valid = false
            Snackbar.make(requireView(), "Please Enter Post", Snackbar.LENGTH_SHORT).show()
        } else {
            post.error = null
        }

        if (sel_club.isEmpty()) {
            Snackbar.make(requireView(), "Please Select Club", Snackbar.LENGTH_SHORT).show()
            valid = false
        }

        return valid
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
         if (parent != null && position > 0) {
            d("bomoh", "selected: ${parent.getItemAtPosition(position)}")
            val c_name = parent.getItemAtPosition(position).toString()
            db.collection("list club")
                .get()
                .addOnSuccessListener { results ->
                    for (result in results) {
                        val club_id = result.getField<String>("title").toString()
                        if (club_id == parent.getItemAtPosition(position)) {
                            sel_club = result.id
                            sel_name = c_name
                        }
                    }
                }

        } else {
             sel_club = ""
             sel_name = ""
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImage) {
            val img = view?.findViewById<ImageView>(R.id.nw_imageUpload)
            imageUri = data?.data
            img?.setImageURI(imageUri)
            img!!.isGone = false
        }
    }

    private fun closeKeyBoard(v : View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}