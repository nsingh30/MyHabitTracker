package com.example.myhabittracker

import android.os.Build
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HabitAdapter(private var habitList: List<Habit>): RecyclerView.Adapter<ViewHolder>() {

    private lateinit var view: View
    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position :Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.habit_list, parent, false)
        return ViewHolder(view, mListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemVM = habitList[position]

        holder.title.text = itemVM.habitTitle
        holder.details.text = itemVM.details

        holder.checkbox.setOnClickListener {

            if (holder.checkbox.isChecked) {
                holder.checkbox.isChecked = false

            } else if (!holder.checkbox.isChecked) {
                holder.checkbox.isChecked = true
            }
        }
    }


    override fun getItemCount(): Int {
        if (habitList != null) {
            return habitList.size;
        }
        else return 0;
    }

    fun setItems(habitList: List<Habit>){
        this.habitList = habitList
        notifyDataSetChanged()
    }

    fun check(current: String){


    }

}
class ViewHolder(view: View, listener: HabitAdapter.onItemClickListener): RecyclerView.ViewHolder(view){

    val title: TextView = view.findViewById(R.id.title)
    val details: TextView = view.findViewById(R.id.details)
    val checkbox: CheckBox = view.findViewById(R.id.check)

//    val endDate: TextView = view.findViewById(R.id.end_date)
//    val optionsMenu: ImageView = view.findViewById(R.id.optionsMenu)
    init {
        view.setOnClickListener{
            listener.onItemClick(adapterPosition )
        }
    }
}

