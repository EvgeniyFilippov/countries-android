package com.repository.database

import com.example.course_android.dto.model.LanguageOfOneCountryDto
import com.example.course_android.room.DatabaseInfo
import com.example.course_android.room.LanguagesInfoEntity
import io.reactivex.rxjava3.core.Flowable

class DatabaseLanguageRepositoryImpl(private val db: DatabaseInfo) : DatabaseLanguageRepository {

    override fun add(entity: LanguagesInfoEntity) = db.getLanguageInfoDAO().add(entity)

    override fun getAllInfo(): Flowable<List<LanguagesInfoEntity>> =
        db.getLanguageInfoDAO().getAllInfo()

    override fun getLanguageByCountry(name: String): List<String> =
        db.getLanguageInfoDAO().getLanguageByCountry(name)

    override fun addAll(list: List<LanguagesInfoEntity>) = db.getLanguageInfoDAO().addAll(list)

}
