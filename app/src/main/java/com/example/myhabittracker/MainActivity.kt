package com.example.myhabittracker

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

//    lateinit var habitList: ArrayList<Habit>
//    lateinit var adapter:HabitAdapter
    lateinit var vm: HabitVM
    lateinit var searchView: SearchView


    lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
//        var actionBar: ActionBar? = supportActionBar
//        actionBar?.hide()
        

        vm = ViewModelProvider(this).get(HabitVM::class.java)

        bottomNav = findViewById(R.id.bottomNavigationView)
        fragmentManager= supportFragmentManager

        val onNavigation: NavigationBarView.OnItemSelectedListener = NavigationBarView.OnItemSelectedListener(){item ->
            when(item.itemId) {
                R.id.home -> {
                    // Respond to navigation item 1 click
                    fragmentManager.beginTransaction().replace(R.id.fragment_container_view, HomeFragment(), null).commit()
                    true
                }
                R.id.viewAll -> {
                    fragmentManager.beginTransaction().replace(R.id.fragment_container_view, ViewAllFragment(), null).commit()
                    // Respond to navigation item 2 click

//                    val intentNext = Intent(this, ListAll::class.java)
//                    startActivity(intentNext)
                    true
                }
                R.id.analysis -> {
                    // Respond to navigation item 2 click
                    //fragmentManager.beginTransaction().replace(R.id.fragment_container_view, AddHabit(), null).commit()
                    true
                }
                else -> false
            }
        }
        bottomNav.setOnItemSelectedListener(onNavigation)

//        habitList = ArrayList()
//        adapter = HabitAdapter(habitList)

//        vm.allHabits?.observe(this) { habitList ->
//            getHabits(habitList)
//            println(habitList)
//        }

        if (findViewById<FragmentContainerView>(R.id.fragment_container_view) != null){
            if(savedInstanceState != null){
                return
            }
            fragmentManager.beginTransaction().add(R.id.fragment_container_view, HomeFragment(), null).commit()
        }


    }


//    fun getHabits(habitList: List<Habit>) {
//        this.habitList.clear()
//        this.habitList.addAll(habitList)
//        adapter.notifyDataSetChanged()
//    }

}