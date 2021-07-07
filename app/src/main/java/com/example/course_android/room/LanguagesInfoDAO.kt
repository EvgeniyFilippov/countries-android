package com.example.course_android.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LanguagesInfoDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(entity: LanguagesInfoEntity)

    @Query("SELECT * FROM languages_table")
    fun getAllInfo(): List<LanguagesInfoEntity>

    @Query("SELECT language FROM languages_table WHERE countryName = :name")
    fun getLanguageByCountry(name: String): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAll(list: List<LanguagesInfoEntity>)
}