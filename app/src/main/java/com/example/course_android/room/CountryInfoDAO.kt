package com.example.course_android.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CountryInfoDAO {

    @Query("SELECT * FROM countries_base_info_table")
    fun getAllInfo(): LiveData<List<CountryBaseInfoEntity>>

    @Query("SELECT * FROM countries_base_info_table WHERE name = :name")
    fun getInfoByName(name: String): LiveData<List<CountryBaseInfoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(entity: CountryBaseInfoEntity)
}