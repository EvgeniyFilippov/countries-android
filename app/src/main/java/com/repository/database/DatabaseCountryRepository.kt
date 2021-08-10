package com.repository.database

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.course_android.room.CountryBaseInfoEntity
import io.reactivex.rxjava3.core.Flowable

interface DatabaseCountryRepository {

    fun getAllInfo(): Flowable<List<CountryBaseInfoEntity>>

    fun getInfoByCountry(name: String): LiveData<List<CountryBaseInfoEntity>>

    fun add(entity: CountryBaseInfoEntity)

    fun addAll(list: List<CountryBaseInfoEntity>)
}