package com.example.myhabittracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.NonDisposableHandle.parent
import java.util.*


class HabitAdapter(private var habitList: List<Habit>): RecyclerView.Adapter<ViewHolder>() {

    private lateinit var view: View
    private lateinit var mListener : onItemClickListener

    private var removedPosition: Int = 0
    private lateinit var removedItem: Habit

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


//    fun removeItem(position: Int, viewHolder: RecyclerView.ViewHolder) {
//
//        removedItem = habitList[position]
//        removedPosition = position
//
//        (habitList as MutableList<Habit>).removeAt(position)
//        notifyDataSetChanged()
//
//        Snackbar.make(viewHolder.itemView, "${removedItem.habitTitle} removed", Snackbar.LENGTH_LONG).setAction("UNDO") {
//            (habitList as MutableList<Habit>).add(removedPosition,removedItem)
//
//
//            notifyDataSetChanged()
//
//        }.show()
//
//
//    }

}
class ViewHolder(view: View, listener: HabitAdapter.onItemClickListener): RecyclerView.ViewHolder(view){

    val title: TextView = view.findViewById(R.id.title)
    val details: TextView = view.findViewById(R.id.details)


    init {
        view.setOnClickListener{
            listener.onItemClick(adapterPosition )
        }
    }
}

