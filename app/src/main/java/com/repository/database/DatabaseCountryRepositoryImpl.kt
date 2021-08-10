package com.repository.database

import androidx.lifecycle.LiveData
import com.example.course_android.room.CountryBaseInfoEntity
import com.example.course_android.room.DatabaseInfo
import io.reactivex.rxjava3.core.Flowable

class DatabaseCountryRepositoryImpl(private val db: DatabaseInfo) : DatabaseCountryRepository {
    override fun getAllInfo(): Flowable<List<CountryBaseInfoEntity>> = db.getCountryInfoDAO().getAllInfo()

    override fun getInfoByCountry(name: String): LiveData<List<CountryBaseInfoEntity>> = db.getCountryInfoDAO().getInfoByCountry(name)

    override fun add(entity: CountryBaseInfoEntity) = db.getCountryInfoDAO().add(entity)

    override fun addAll(list: List<CountryBaseInfoEntity>) = db.getCountryInfoDAO().addAll(list)

}