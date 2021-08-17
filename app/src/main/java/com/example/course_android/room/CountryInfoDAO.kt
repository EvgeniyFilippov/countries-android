package com.example.course_android.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable

@Dao
interface CountryInfoDAO {

    @Query("SELECT * FROM countries_base_info_table")
    fun getAllInfo(): Flowable<List<CountryBaseInfoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(entity: CountryBaseInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(list: List<CountryBaseInfoEntity>)
}