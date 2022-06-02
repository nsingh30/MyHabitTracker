package com.example.myhabittracker

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class HomeFragment : Fragment(){

    lateinit var rv: RecyclerView
    lateinit var habitList : ArrayList<Habit>
    lateinit var adapter:HabitAdapter
    lateinit var vm: HabitVM
    lateinit var fab: ExtendedFloatingActionButton
    lateinit var progressHome: ProgressBar
    lateinit var homeLayout : LinearLayout
    lateinit var relative : RelativeLayout
    lateinit var sub: TextView
    var itemCount: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity.let {
            vm = ViewModelProvider(it!!).get(HabitVM::class.java)
        }

        habitList = ArrayList()

        fab = view.findViewById(R.id.fab)
        progressHome = view. findViewById(R.id.progressHome)
        homeLayout = view.findViewById(R.id.home_layout)
        relative = view.findViewById(R.id.relative)
        sub = view.findViewById(R.id.sub_hi)

        rv = view.findViewById<RecyclerView>(R.id.recycler)
        rv.layoutManager = LinearLayoutManager(requireContext())


        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = current.format(formatter)
        var checkDate = formatted
        var dateToCheck = checkDate
//        var date1 = LocalDate.parse(habitList.text, formatter)
//        var between = ChronoUnit.DAYS.between(date1, date2)
//


        vm.todaysHabit?.observe(viewLifecycleOwner, Observer{
                habitList -> getHabits(habitList)
            itemCount= adapter.itemCount
            if(itemCount == 0){
                sub.text = "There is no activity scheduled for today."
            }else if (itemCount == 1){
                sub.text = "You have $itemCount activity today"
            }else{
                sub.text = "You have $itemCount activities today"
            }
        })

        if(formatted>checkDate){
            vm.updateDays
        }

        fab.setOnClickListener{

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, AddHabit(), "homeFragment")
                .addToBackStack(null)
                .commit()
        }

        adapter = HabitAdapter(habitList)

        rv.adapter = adapter
        adapter.setOnItemClickListener(object: HabitAdapter.onItemClickListener{
            override fun onItemClick(position: Int){
//                Toast.makeText(requireContext(), "You clicked", Toast.LENGTH_SHORT).show()

                val bundle =Bundle()
                bundle.putInt("id", habitList[position].id!!)
                bundle.putString("title", habitList[position].habitTitle!!)
                bundle.putString("details", habitList[position].details!!)
                bundle.putString("start", habitList[position].startDate!!)
                bundle.putString("end", habitList[position].endDate!!)
                val updateFragment = UpdateFragment()
                updateFragment.arguments = bundle
//                setFragmentResult("edit_entry", bundleOf("extra" to bundle))
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, updateFragment, "homeFragment")
                    .addToBackStack(null)
                    .commit()
            }
        })

//        colorDrawableBackground = ColorDrawable(Color.parseColor("#ff0000"))
//        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete)!!

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder2: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDirection: Int) {
                removeItem(viewHolder.adapterPosition, viewHolder)
            }

        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rv)
    }

    fun removeItem(position: Int, viewHolder: RecyclerView.ViewHolder) {

        var removedItem = habitList[viewHolder.adapterPosition]
        var removedPosition = position
        val id = habitList[viewHolder.adapterPosition].id

        (habitList as MutableList<Habit>).removeAt(position)
        if (id != null) {
            vm.updateStatus(0, id)
        }

        adapter.notifyDataSetChanged()
//        vm.todaysHabit?.observe(viewLifecycleOwner, Observer{
//                habitList -> getHabits(habitList)
//
//            itemCount= adapter.itemCount
//            if(itemCount == 0){
//                sub.text = "There is no activity scheduled for today."
//            }else if (itemCount == 1){
//                sub.text = "You have $itemCount activity today"
//            }else{
//                sub.text = "You have $itemCount activities today"
//            }
//        })


        Snackbar.make(viewHolder.itemView, "'${removedItem.habitTitle}' removed", Snackbar.LENGTH_LONG).setAction("UNDO") {

            if (id != null) {
                vm.updateStatus(1, id)
            }
            (habitList as MutableList<Habit>).add(removedPosition,removedItem)
            adapter.notifyDataSetChanged()

        }.show()


    }

    fun getHabits(habitList: List<Habit>) {
        this.habitList.clear()
        this.habitList.addAll(habitList)
        adapter.notifyDataSetChanged()
        rv.isVisible = true
        progressHome.isGone = true
    }

}