package com.example.course_android.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries_base_info_table")
class CountryBaseInfoEntity(
    @PrimaryKey @ColumnInfo val name: String,
    @ColumnInfo val capital: String,
    @ColumnInfo val area: Double
)

