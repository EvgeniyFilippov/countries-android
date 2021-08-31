package com.example.domain.usecase

import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow
import java.io.Serializable

abstract class UseCaseFlow<Params : Any, Result> : Serializable {

    private var mParams: Params? = null

    protected abstract fun buildFlow(params: Params?): Flow<Result>

    abstract val mIsParamsRequired: Boolean

    fun setParams(params: Params): UseCaseFlow<Params, Result> {
        mParams = params
        return this
    }

    fun execute(): Flow<Result> {
        require(!(mIsParamsRequired && mParams == null)) { "Params are required" }
        return buildFlow(mParams)
    }

}