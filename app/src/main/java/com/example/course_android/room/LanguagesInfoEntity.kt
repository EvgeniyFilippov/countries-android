package com.example.course_android.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "languages_table")
class LanguagesInfoEntity (
    @PrimaryKey @ColumnInfo val name: String,
    @ColumnInfo val language: String
)