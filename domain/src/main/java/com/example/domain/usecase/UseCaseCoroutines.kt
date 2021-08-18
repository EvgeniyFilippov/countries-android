package com.example.domain.usecase

import java.io.Serializable

abstract class UseCaseCoroutines<Params : Any, Result> : Serializable {

    private var mParams: Params? = null

    protected abstract fun buildResult(params: Params?): Result

    abstract val mIsParamsRequired: Boolean

    fun setParams(params: Params): UseCaseCoroutines<Params, Result> {
        mParams = params
        return this
    }

    fun execute(): Result {
        require(!(mIsParamsRequired && mParams == null)) { "Params are required" }
        return buildResult(mParams)
    }

}