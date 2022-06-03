package com.example.myhabittracker

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.MenuItemCompat
import androidx.core.view.MenuItemCompat.setActionView
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay


class ViewAllFragment : Fragment() {

    lateinit var recycler: RecyclerView
    lateinit var habitListAll : ArrayList<Habit>
    lateinit var adapter:ViewAllAdapter
    lateinit var vm: HabitVM
    lateinit var back: ImageView
    lateinit var loader: ProgressBar
    lateinit var scroller: NestedScrollView
    lateinit var search: ImageView
    lateinit var searchText: EditText
    lateinit var sort: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity.let {
            vm = ViewModelProvider(it!!).get(HabitVM::class.java)
        }

        recycler = view.findViewById<RecyclerView>(R.id.recycle_all)
        recycler.layoutManager = GridLayoutManager(requireContext(), 2)

        back = view.findViewById(R.id.back_home)
        loader = view.findViewById(R.id.progressAll)
        scroller = view.findViewById(R.id.scroller)
        search = view.findViewById(R.id.search)
        searchText = view.findViewById(R.id.search_bar)
        sort = view.findViewById(R.id.sort)

        back.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, HomeFragment(), "ViewAllFragment")
                .addToBackStack(null)
                .commit()
        }

        habitListAll = ArrayList()

        vm.allHabits?.observe(viewLifecycleOwner, Observer { habitListAll ->
            getHabits(habitListAll)
        })

        search.setOnClickListener{
            if(searchText.text.toString() == ""){
                vm.allHabits?.observe(viewLifecycleOwner, Observer { habitListAll ->
                    getHabits(habitListAll)

                })
            }else{
                vm.searchIn(searchText.text.toString())
                vm.searchHabit.observe(viewLifecycleOwner, Observer { habitListAll ->
                    getHabits(habitListAll)
                })
            }
        }

        sort.setOnClickListener{
            val popup = PopupMenu(context, sort)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.popup, popup.menu)

            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->

                if (item.itemId == R.id.sortByTitle) {
                    vm.sortByTitle?.observe(viewLifecycleOwner, Observer { habitListAll ->
                        getHabits(habitListAll)
                    })
                } else if (item.itemId == R.id.sortByDate){
                    vm.sortByDate?.observe(viewLifecycleOwner, Observer { habitListAll ->
                        getHabits(habitListAll)
                    })
                }else {
                    vm.allHabits?.observe(viewLifecycleOwner, Observer { habitListAll ->
                        getHabits(habitListAll)
                    })
                }
                true
            })
            popup.show()
        }


        adapter = ViewAllAdapter(habitListAll)
        recycler.adapter = adapter

    }


    fun getHabits(habitList: List<Habit>) {
        this.habitListAll.clear()
        this.habitListAll.addAll(habitList)
        adapter.notifyDataSetChanged()
        loader.visibility = View.GONE
        recycler.visibility = View.VISIBLE
    }

}