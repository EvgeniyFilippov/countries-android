package com.example.domain.usecase.impl

import com.example.domain.dto.room.RoomCountryDescriptionItemDto
import com.example.domain.repository.DatabaseCountryRepository
import com.example.domain.usecase.UseCase
import io.reactivex.rxjava3.core.Flowable

class GetListCountriesFromDbUseCase(private val databaseCountryRepository: DatabaseCountryRepository) :
    UseCase<Unit, List<RoomCountryDescriptionItemDto>>() {
    override fun buildFlowable(params: Unit?): Flowable<List<RoomCountryDescriptionItemDto>> =
        databaseCountryRepository.getAllInfo()

    override val mIsParamsRequired: Boolean
        get() = false

}