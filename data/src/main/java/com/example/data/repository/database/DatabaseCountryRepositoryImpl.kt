package com.example.data.repository.database

import com.example.domain.dto.room.RoomCountryDescriptionItemDto
import com.example.data.ext.convertCountryEntityToDto
import com.example.domain.repository.DatabaseCountryRepository
import com.example.data.room.DatabaseInfo
import io.reactivex.rxjava3.core.Flowable

class DatabaseCountryRepositoryImpl(private val db: DatabaseInfo) :
    DatabaseCountryRepository {
    override fun getAllInfo(): Flowable<List<RoomCountryDescriptionItemDto>> =
        db.getCountryInfoDAO().getAllInfo()
            .map { it.convertCountryEntityToDto() }

//    override fun add(entity: RoomCountryDescriptionItemDto) = db.getCountryInfoDAO().add(entity)

//    override fun addAll(list: List<RoomCountryDescriptionItemDto>) = db.getCountryInfoDAO().addAll(list)

}