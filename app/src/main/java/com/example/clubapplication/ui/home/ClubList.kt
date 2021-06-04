package com.example.clubapplication.ui.home

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.clubapplication.R

class ClubList(private val listClub: MutableList<ListClub>) : RecyclerView.Adapter<ClubList.ViewHolder>() {
    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.lc_title)
        val layout: ConstraintLayout = itemView.findViewById(R.id.clubListLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.club_list_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loc = listClub[position]
        holder.title.text = loc.name
        holder.layout.setOnClickListener {
            d("bomoh", "clicked ${loc.club_id}")
            val bundle = bundleOf("club_id" to loc.club_id)
            it.findNavController().navigate(R.id.action_nav_home_to_nav_club_details, bundle)
        }
    }

    override fun getItemCount(): Int {
        return listClub.size
    }

}
