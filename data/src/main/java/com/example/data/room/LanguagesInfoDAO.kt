package com.example.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable

@Dao
interface LanguagesInfoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(entity: LanguagesInfoEntity)

    @Query("SELECT * FROM languages_table")
    fun getAllInfo(): Flowable<List<LanguagesInfoEntity>>

    @Query("SELECT language FROM languages_table WHERE countryName = :name")
    fun getLanguageByCountry(name: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(list: MutableList<LanguagesInfoEntity>)
}