package com.example.data.repository.database

import com.example.domain.dto.room.RoomLanguageOfOneCountryDto
import com.example.course_android.room.DatabaseInfo
import com.example.course_android.room.LanguagesInfoEntity
import com.example.course_android.utils.convertLanguageEntityToDto
import io.reactivex.rxjava3.core.Flowable

class DatabaseLanguageRepositoryImpl(private val db: DatabaseInfo) :
    com.example.domain.repository.DatabaseLanguageRepository {

    override fun add(entity: LanguagesInfoEntity) = db.getLanguageInfoDAO().add(entity)

    override fun getAllInfo(): Flowable<List<RoomLanguageOfOneCountryDto>> =
        db.getLanguageInfoDAO().getAllInfo().map { it.convertLanguageEntityToDto() }

    override fun getLanguageByCountry(name: String): List<String> =
        db.getLanguageInfoDAO().getLanguageByCountry(name)

    override fun addAll(list: List<LanguagesInfoEntity>) = db.getLanguageInfoDAO().addAll(list)

}