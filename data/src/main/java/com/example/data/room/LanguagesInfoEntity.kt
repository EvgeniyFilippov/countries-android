package com.example.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "languages_table", primaryKeys = ["countryName", "language"])
class LanguagesInfoEntity(
    @ColumnInfo var countryName: String,
    @ColumnInfo var language: String
)

