package com.example.course_android.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CountryBaseInfoEntity::class, LanguagesInfoEntity::class], version = 1)
abstract class DatabaseInfo: RoomDatabase() {

    abstract fun getCountryInfoDAO(): CountryInfoDAO

    companion object {
        fun init(context: Context) =
            Room.databaseBuilder(context, DatabaseInfo::class.java, "database")
                .allowMainThreadQueries()
                .build()
    }
}