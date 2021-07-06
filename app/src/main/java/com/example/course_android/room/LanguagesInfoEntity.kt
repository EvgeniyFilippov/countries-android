package com.example.course_android.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "languages_table")
class LanguagesInfoEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "auto_id") val id: Int,
    @ColumnInfo val countryName: String,
    @ColumnInfo val language: String
)