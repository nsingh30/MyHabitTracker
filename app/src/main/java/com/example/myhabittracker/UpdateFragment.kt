package com.example.myhabittracker

import android.app.Application
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.makeText
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Transaction
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class UpdateFragment : Fragment() {

    lateinit var back: ImageView
    lateinit var delete: ImageView
    lateinit var vm: HabitVM
    lateinit var submit: Button
    lateinit var input_1: TextInputLayout
    lateinit var input_3: TextInputLayout
    lateinit var input_4: TextInputLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_update, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        activity.let {
            vm = ViewModelProvider(it!!).get(HabitVM::class.java)
        }

        var habitList = vm.getAllHabits()


        var id = arguments?.getInt("id")
        var title= arguments?.getString("title")
        var details= arguments?.getString("details")
        var start= arguments?.getString("start")
        var end= arguments?.getString("end")

//        setFragmentResultListener("edit_entry"){key, result ->
//            id = result.getInt("id")
//            title = result.getString("title")
//            details = result.getString("details")
//            start = result.getString("start")
//            end = result.getString("end")
//        }


        back = view.findViewById(R.id.back)
        back.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, HomeFragment(), "updateFragment")
                .addToBackStack(null)
                .commit()
        }

        var editTitle = view.findViewById<EditText>(R.id.edit_title)
        var editDetails = view.findViewById<EditText>(R.id.edit_details)
        var editEndDate = view.findViewById<EditText>(R.id.edit_end)
        var editStartDate = view.findViewById<EditText>(R.id.edit_start)

        editTitle.setText(title)
        editDetails.setText(details)
        editStartDate.setText(start)
        editEndDate.setText(end)

        editStartDate.setOnClickListener {
            val datePicker = SelectDate()
            val supportFragmentManager = requireActivity().supportFragmentManager
            datePicker.show(supportFragmentManager, "datePicker")
            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY", viewLifecycleOwner
            ) { resultKey, bundle ->
                if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    editStartDate.setText(date)
                }
            }
        }

        editEndDate.setOnClickListener {
            val datePicker = SelectDate()
            val supportFragmentManager = requireActivity().supportFragmentManager
            datePicker.show(supportFragmentManager, "datePicker")
            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY", viewLifecycleOwner
            ) { resultKey, bundle ->
                if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    editEndDate.setText(date)
                }
            }
        }

        submit = view.findViewById(R.id.onUpdate)

        input_1 = view.findViewById(R.id.input1)
        input_3 = view.findViewById(R.id.input3)
        input_4 = view.findViewById(R.id.input4)

        submit.setOnClickListener{

            var name: String? = editTitle.text.toString()
            var start: String? =  editStartDate.text.toString()
            var end: String? = editEndDate.text.toString()

            var nullValidation: Boolean
            if (name != "" && start != "" && end != "") {
                nullValidation = true
            } else {
                if (name == "") {
                    input_1.helperText = "Field cannot be empty"
                }
                if (start == "") {
                    input_3.helperText = "Field cannot be empty"
                }
                if (end == "") {
                    input_4.helperText = "Field cannot be empty"
                }
                nullValidation = false
            }

            var dateFormatCheck: Boolean = true

            if (!start.toString().matches(("\\d{4}-\\d{2}-\\d{2}").toRegex())){
                input_3.helperText = "Enter date in yyyy-mm-dd format"
                dateFormatCheck = false
            }

            if(!end.toString().matches(("\\d{4}-\\d{2}-\\d{2}").toRegex())) {
                input_4.helperText = "Enter date in yyyy-mm-dd format"
                dateFormatCheck = false
            }

            if (nullValidation && dateFormatCheck){
                val habit = Habit(id, editTitle.text.toString(), editDetails.text.toString(),
                    editStartDate.text.toString(), editEndDate.text.toString(), null, null)
                vm.updateHabit(habit)
                Snackbar.make(view, "You updated $title record", Toast.LENGTH_SHORT).show()

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, HomeFragment(), "upFragment")
                    .addToBackStack(null)
                    .commit()
            }else{
                var snack = Snackbar.make(view, "Please enter all fields", Snackbar.LENGTH_SHORT)
                snack.show()
            }

        }

        delete = view.findViewById(R.id.delete_icon)

        delete.setOnClickListener {
            val habit = Habit(id, editTitle.text.toString(), editDetails.text.toString(),
                editStartDate.text.toString(), editEndDate.text.toString(), null, null)
//
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Alert")
                .setMessage("This will permanently delete $title. Do you want to continue?")
                .setPositiveButton("Cancel", null)
                .setNegativeButton("Delete") {dialog, which ->
                    vm.deleteHabit(habit)
                    Snackbar.make(view, "You delete $title record", Snackbar.LENGTH_SHORT).show()
                    }
                .show()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, HomeFragment(), "upFragment")
                .addToBackStack(null)
                .commit()
        }


    }

}




