package com.example.clubapplication.ui.gallery

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.clubapplication.R
import com.squareup.picasso.Picasso

class PostNews(private val postList: MutableList<PostList>) :
    RecyclerView.Adapter<PostNews.ViewHolder>() {
    class ViewHolder (itemView : View): RecyclerView.ViewHolder(itemView) {
        val post : TextView = itemView.findViewById(R.id.vn_post)
        val club : TextView = itemView.findViewById(R.id.vn_clubName)
        val image : ImageView = itemView.findViewById(R.id.vn_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_post_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loc = postList[position]
        holder.post.text = loc.post
        holder.club.text = loc.club
        if (loc.image != "null") {
            d("bomoh", "got image")
            Picasso.get().load(loc.image).into(holder.image)
            holder.image.isGone = false
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

}
