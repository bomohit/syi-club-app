package com.example.clubapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.clubapplication.viewmodel.loginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.time.LocalDateTime

@Suppress("DEPRECATION")
class nav_payment : Fragment() {

    val db = Firebase.firestore
    private val pickImage = 100
    private var imageUri : Uri? = null
    var sel_club : String = ""
    var sel_name : String = ""
    var userid : String = ""

    private val storage = Firebase.storage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_nav_payment, container, false)
        val club_id = arguments?.getString("club_id").toString()
        val pay = root.findViewById<Button>(R.id.pay_upload)
        // get current user username
        var viewModel = ViewModelProvider(requireActivity()).get(loginViewModel::class.java)
        userid = viewModel.id.toString()
        pay.isEnabled = false

        pay.setOnClickListener {
            val imgPick = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(imgPick, pickImage)
        }
        db.collection("request").document(userid)
            .get()
            .addOnSuccessListener {
                val img = it.getField<String>("image").toString()
                pay.isEnabled = img.isNullOrEmpty()
                if (pay.isEnabled) {
                    pay.text = "Payment Submitted"
                }
            }

        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImage) {
            val progressBar : ProgressBar = requireView().findViewById(R.id.progressBar2)
            val pay : Button = requireView().findViewById(R.id.pay_upload)
            progressBar.isVisible = true
//            val img = view?.findViewById<ImageView>(R.id.pay_upload)
            imageUri = data?.data
//            img?.setImageURI(imageUri)
//            img!!.isGone = false

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
                        "image" to task.result.toString()
                    )
                    db.collection("request").document(userid)
                        .set(data, SetOptions.merge())
                    Snackbar.make(requireView(), "Payment Uploaded", Snackbar.LENGTH_SHORT).show()
                    progressBar.isVisible = false
                    pay.isEnabled = false
                    pay.text = "Payment Submitted"
                }
            }
        }
    }

}