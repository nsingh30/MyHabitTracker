package com.example.myhabittracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(@PrimaryKey(autoGenerate = true)var id: Int?,
                 @ColumnInfo(name = "habitTitle")var habitTitle: String?,
                 @ColumnInfo(name = "Details")var details: String?,
                 @ColumnInfo(name = "startDate")var startDate: String?,
                 @ColumnInfo(name = "endDate")var endDate: String?,
                 @ColumnInfo(name = "totalDays")var totalDays: Int?,
                 @ColumnInfo(name = "doneDays")var doneDays: Int?,
                 @ColumnInfo(name = "status")var status: Int?)
