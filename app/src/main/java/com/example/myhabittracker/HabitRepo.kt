package com.example.myhabittracker

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList

class HabitRepo(context: Context) {

    var db: HabitDao? = HabitDatabase.getInstance(context)?.habitDao()

    fun getAllHabits(): LiveData<List<Habit>>? {
        return db?.getAllHabits()
    }
    fun insertHabit(habit: Habit){
        db?.insertHabit(habit)
    }
    fun updateHabit(habit: Habit){
        db?.updateHabit(habit)
    }
    fun deleteHabit(habit: Habit){
        db?.deleteHabit(habit)
    }

    fun getHabit(id: Int): LiveData<Habit>? {
        return db?.getHabit(id)
    }

    fun getTodaysHabit(current: String): LiveData<List<Habit>>? {
        return db?.getTodaysHabit(current)
    }

    fun updateDoneDays():Unit? {
        return db?.updateDoneDays()
    }

    fun updateNewStatus(newStatus: Int, current: String): Unit?{
        return db?.updateNewStatus(newStatus, current)
    }

    fun updateStatus(newStatus: Int, id: Int): Unit?{
        return db?.updateStatus(newStatus, id)
    }

    fun searchDataBase(searchQuery: String): LiveData<List<Habit>>? {
        return db?.searchDatabase(searchQuery)
    }

    fun sortByTitle(): LiveData<List<Habit>>? {
        return db?.sortByTitle()
    }

    fun sortByDate(): LiveData<List<Habit>>? {
        return db?.sortByDate()
    }
}