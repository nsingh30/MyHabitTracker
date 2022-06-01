package com.example.myhabittracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewAllAdapter(private var habitListAll: List<Habit>):RecyclerView.Adapter<ViewHolder2>(){

    lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2 {
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_all, parent, false)
        return ViewHolder2(view)
    }



    override fun onBindViewHolder(holder: ViewHolder2, position: Int) {
        val itemVM = habitListAll[position]

        holder.habit_name.text = itemVM.habitTitle
        holder.habit_details.text = itemVM.details
    }

    override fun getItemCount(): Int {

        if (habitListAll != null) {
            return habitListAll.size;
        }
        else return 0;
    }
}

class ViewHolder2(view: View): RecyclerView.ViewHolder(view) {

    var habit_name: TextView = view.findViewById(R.id.habit_name)
    val habit_details: TextView = view.findViewById(R.id.habit_details)
}