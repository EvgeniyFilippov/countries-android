package com.example.course_android.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable

@Dao
interface CountryInfoDAO {

    @Query("SELECT * FROM countries_base_info_table")
    fun getAllInfo(): Flowable<List<CountryBaseInfoEntity>>

    @Query("SELECT * FROM countries_base_info_table WHERE name = :name")
    fun getInfoByCountry(name: String): LiveData<List<CountryBaseInfoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(entity: CountryBaseInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(list: List<CountryBaseInfoEntity>)
}