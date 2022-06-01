package com.example.myhabittracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Habit::class], version = 1, exportSchema = false)
abstract class HabitDatabase: RoomDatabase() {

    abstract fun habitDao(): HabitDao


    companion object {
        var instance: HabitDatabase? = null
        fun getInstance(context: Context): HabitDatabase? {
            if (instance == null) {
                synchronized(HabitDatabase::class) {

                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HabitDatabase::class.java, "habits.db"
                    )
                        .allowMainThreadQueries().build()
                }
            }
            return instance
        }

    }
}
//if (instance == null) {
//                synchronized(HabitDatabase::class){
//                    instance = Room.databaseBuilder(context.applicationContext,
//                        HabitDatabase::class.java, "habits.db")
//                        .allowMainThreadQueries().build()
//                }
//            }

//var INSTANCE: HabitDatabase? = null
//        fun getInstance(context: Context): HabitDatabase? {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    HabitDatabase::class.java, "habits.db"
//                )
//                    .allowMainThreadQueries().build()
//                INSTANCE = instance
//                return instance