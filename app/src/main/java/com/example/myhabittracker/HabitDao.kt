package com.example.myhabittracker

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.room.*
import java.util.concurrent.Flow

@Dao
interface HabitDao {

    @Insert
    fun insertHabit(habit: Habit)

    @Delete
    fun deleteHabit(habit: Habit)

    @Update
    fun updateHabit(habit: Habit)

    @Query("select * from habits")
    fun getAllHabits(): LiveData<List<Habit>>

    @Query("select * from habits where id = :id")
    fun getHabit(id: Int):LiveData<Habit>

    @Query("select * from habits where startDate<=:current and endDate>=:current")
    fun getTodaysHabit(current: String):LiveData<List<Habit>>

    @Query("UPDATE habits set doneDays=doneDays+1")
    fun updateDoneDays()

    @Query("select * from habits where UPPER(habitTitle) LIKE UPPER('%'||:searchQuery||'%')")
    fun searchDatabase(searchQuery: String): LiveData<List<Habit>>

    @Query("select * from habits order by UPPER(habitTitle) ASC")
    fun sortByTitle():LiveData<List<Habit>>

    @Query("select * from habits order by startDate ASC")
    fun sortByDate():LiveData<List<Habit>>

}