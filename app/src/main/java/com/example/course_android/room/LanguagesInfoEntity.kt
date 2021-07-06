package com.example.course_android.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "languages_table")
class LanguagesInfoEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo val id: Int,
    @ColumnInfo val language: String,
    @ColumnInfo val countryName: String
)