package com.example.myhabittracker

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.paging.PagedList
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HabitVM(app: Application): AndroidViewModel(app) {

    @RequiresApi(Build.VERSION_CODES.O)
    val current = LocalDateTime.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    @RequiresApi(Build.VERSION_CODES.O)
    val formatted = current.format(formatter)

    private val repo: HabitRepo

    private var isSelected: Boolean = false


    private val search =MutableLiveData<String>()
    val allHabits: LiveData<List<Habit>>?
    val todaysHabit : LiveData<List<Habit>>?
    val sortByTitle: LiveData<List<Habit>>?
    val sortByDate: LiveData<List<Habit>>?

    init{
        repo = HabitRepo(app)
        allHabits = repo.getAllHabits()
        todaysHabit = repo.getTodaysHabit(formatted)
        search.value = ""
        sortByDate = repo.sortByDate()
        sortByTitle = repo.sortByTitle()
    }

    val updateDays = repo.updateDoneDays()

    fun getAllHabits() = viewModelScope.launch {
        repo.getAllHabits()
    }
    fun insertHabit(habit: Habit) = viewModelScope.launch{
        repo.insertHabit(habit)
    }
    fun updateHabit(habit: Habit)= viewModelScope.launch {
        repo.updateHabit(habit)
    }
    fun deleteHabit(habit: Habit)= viewModelScope.launch  {
        repo.deleteHabit(habit)
    }
    fun getHabit(id: Int) = viewModelScope.launch {
        repo.getHabit(id)
    }

    fun updateStatus(newStatus: Int, id: Int) = viewModelScope.launch {
        repo.updateStatus(newStatus, id)
    }

    fun updateDoneDays() = viewModelScope.launch{
        repo.updateDoneDays()
    }

    fun getTodaysHabits() = viewModelScope.launch {
        repo.getAllHabits()
    }

    val searchHabit = Transformations.switchMap(search){ habit ->
        if(habit!= "") {
            repo.searchDataBase(habit)
        }else{
            repo.getAllHabits()
        }

    }
    fun searchIn(text: String) =viewModelScope.launch{
        search.value = text
    }

}