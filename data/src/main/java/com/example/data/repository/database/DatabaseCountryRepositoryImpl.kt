package com.example.data.repository.database

import com.example.data.ext.convertCountryListDtoToEntity
import com.example.data.ext.convertListCountryEntityToDto
import com.example.data.room.DatabaseInfo
import com.example.domain.dto.room.RoomCountryDescriptionItemDto
import com.example.domain.repository.DatabaseCountryRepository
import io.reactivex.rxjava3.core.Flowable

class DatabaseCountryRepositoryImpl(private val db: DatabaseInfo) : DatabaseCountryRepository {

    override fun getAllInfo(): Flowable<List<RoomCountryDescriptionItemDto>> =
        db.getCountryInfoDAO().getAllInfo()
            .map { it.convertListCountryEntityToDto() }

    override fun addAll(list: List<RoomCountryDescriptionItemDto>) {
        db.getCountryInfoDAO().addAll(list.convertCountryListDtoToEntity())
    }

}