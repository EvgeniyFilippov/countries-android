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
//
//    @Query("SELECT * FROM countries_base_info_table WHERE name = :name")
//    fun getInfoByName(name: String)

//    @Query("INSERT INTO countries_base_info_table (name, capital, area) VALUES (name = :name, capital = :capital, area = :area)")
//    fun addCountry(name: String, capital: String, area: String)

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun add(entity: CountryBaseInfoEntity)
}