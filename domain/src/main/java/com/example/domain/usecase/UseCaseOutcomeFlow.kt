package com.example.domain.usecase

import com.example.domain.outcome.Outcome
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow
import java.io.Serializable

abstract class UseCaseOutcomeFlow<Params : Any, Result> : Serializable {

    private var mParams: Params? = null

    protected abstract fun buildOutcomeFlow(params: Params?): Flow<Outcome<Result>>

    abstract val mIsParamsRequired: Boolean

    fun setParams(params: Params): UseCaseOutcomeFlow<Params, Result> {
        mParams = params
        return this
    }

    fun execute(): Flow<Outcome<Result>> {
        require(!(mIsParamsRequired && mParams == null)) { "Params are required" }
        return buildOutcomeFlow(mParams)
    }

}