package com.repository.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.course_android.room.LanguagesInfoEntity
import io.reactivex.rxjava3.core.Flowable

interface DatabaseRepository {

    /**language*/
    fun add(entity: LanguagesInfoEntity)

    fun getAllInfo(): Flowable<List<LanguagesInfoEntity>>

    fun getLanguageByCountry(name: String): List<String>

    fun addAll(list: List<LanguagesInfoEntity>)

}