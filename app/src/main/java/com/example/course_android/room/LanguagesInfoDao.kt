package com.example.course_android.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface LanguagesInfoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(entity: LanguagesInfoEntity)
}