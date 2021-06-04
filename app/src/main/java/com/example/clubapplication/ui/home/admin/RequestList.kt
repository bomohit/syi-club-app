package com.example.clubapplication.ui.home.admin

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clubapplication.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class RequestList(private val listRequest: MutableList<ListRequest>) :
    RecyclerView.Adapter<RequestList.ViewHolder>() {
    val db = Firebase.firestore
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.rq_name)
        val club: TextView = itemView.findViewById(R.id.rq_club)
        val btn_approve : Button = itemView.findViewById(R.id.rq_approve)
        val btn_reject : Button = itemView.findViewById(R.id.rq_reject)
        val image : ImageView = itemView.findViewById(R.id.imageView3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.request_list_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loc = listRequest[position]
        holder.name.text = loc.name
        holder.club.text = loc.title

        holder.btn_approve.setOnClickListener {
            d("bomoh", "approve pressed")
            Snackbar.make(it, "Request Approved", Snackbar.LENGTH_SHORT).show()
            // add to user
            val data = hashMapOf(
                "title" to loc.title
            )
            db.collection("user").document(loc.uid).collection("club").document(loc.club_id)
                .set(data)
            // remove from request
            db.collection("request").document(loc.uid)
                .delete()
        }
        holder.btn_reject.setOnClickListener {
            d("bomoh", "reject pressed")
            Snackbar.make(it, "Request Rejected", Snackbar.LENGTH_SHORT).show()
            // remove from request
            db.collection("request").document(loc.uid)
                .delete()
        }

        Picasso.get().load(loc.image).into(holder.image)
        d("bomoh", "image : ${loc.image}")
    }

    override fun getItemCount(): Int {
        return listRequest.size
    }

}
