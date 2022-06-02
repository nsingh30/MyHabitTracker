package com.example.myhabittracker

import android.R.attr.text
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


class AddHabit : Fragment() {

    lateinit var startDate: EditText
    lateinit var endDate: EditText
    lateinit var submit: Button
    lateinit var title: EditText
    lateinit var details: EditText
    lateinit var backImage: ImageView
    lateinit var input_1: TextInputLayout
    lateinit var input_3: TextInputLayout
    lateinit var input_4: TextInputLayout
    lateinit var progress: ProgressBar
    lateinit var main: LinearLayout

    lateinit var vm: HabitVM


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_add_habit, container, false)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity.let {
            vm = ViewModelProvider(it!!).get(HabitVM::class.java)
        }

        progress = view. findViewById(R.id.progress)
        main = view.findViewById(R.id.main_content)


        progress.isVisible = false
        main.isVisible = true

        title = view.findViewById<EditText>(R.id.name)
        details = view.findViewById<EditText>(R.id.details)
        startDate = view.findViewById(R.id.start_date)
        endDate = view.findViewById(R.id.end_date)
        backImage = view.findViewById(R.id.back_add)
        submit = view.findViewById(R.id.onSubmit)
        input_1 = view.findViewById(R.id.input_1)
        input_3 = view.findViewById(R.id.input_3)
        input_4 = view.findViewById(R.id.input_4)

        backImage.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, HomeFragment(), "addFragment")
                .addToBackStack(null)
                .commit()
        }

        startDate.setOnClickListener {
            val datePicker = SelectDate()
            val supportFragmentManager = requireActivity().supportFragmentManager
            datePicker.show(supportFragmentManager, "datePicker")
            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY", viewLifecycleOwner
            ) { resultKey, bundle ->
                if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    startDate.setText(date)
                }
            }
        }

        endDate.setOnClickListener {
            val datePicker = SelectDate()
            val supportFragmentManager = requireActivity().supportFragmentManager
            datePicker.show(supportFragmentManager, "datePicker")
            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY", viewLifecycleOwner
            ) { resultKey, bundle ->
                if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    endDate.setText(date)
                }
            }
        }



        submit.setOnClickListener {

            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            lateinit var date2 : LocalDate
            lateinit var date1: LocalDate
            var between: Long = 0
            var done: Long = 0
            if(endDate.text.toString() != "" && startDate.text.toString() != ""){
                date2 = LocalDate.parse(endDate.text, formatter)
                date1 = LocalDate.parse(startDate.text, formatter)
                between = ChronoUnit.DAYS.between(date1, date2)
                var current = LocalDateTime.now()
                val formatted = current.format(formatter)
                var updated = LocalDate.parse(formatted, formatter)
                done = ChronoUnit.DAYS.between(date1, updated)
            }


            var name: String? = title.text.toString()
            var start: String? =  startDate.text.toString()
            var end: String? = endDate.text.toString()

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
                vm.insertHabit(Habit(null,name, details.text.toString(),
                    start, end, between.toInt(), done.toInt(), 1))
                Snackbar.make(view, "New Habit added", Snackbar.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, HomeFragment(), "addFragment")
                    .addToBackStack(null)
                    .commit()
            }else{
                    Snackbar.make(view, "Please enter correct values", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
