package com.example.course_android.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "languages_table", primaryKeys = ["countryName", "language"])
class LanguagesInfoEntity(
    @ColumnInfo val countryName: String,
    @ColumnInfo val language: String
)

