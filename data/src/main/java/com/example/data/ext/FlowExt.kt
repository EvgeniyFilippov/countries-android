package com.example.data.ext

import com.example.domain.outcome.Outcome
import com.example.domain.outcome.Transformer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

fun <InputType, OutputType> modifyFlow(
    data: Flow<InputType>,
    transformer: Transformer<InputType, OutputType>
): Flow<Outcome<OutputType>> {
    return data.execute(transformer.convert)
}

fun <InputType, OutputType> Flow<InputType>.execute(convert: (InputType) -> OutputType): Flow<Outcome<OutputType>> =
    this.flowOn(Dispatchers.IO)
        .map { dto -> convert(dto) }
        .map { list -> Outcome.success(list) }
        .onStart { emit(Outcome.loading(true)) }
        .onCompletion { emit(Outcome.loading(false)) }
        .catch { ex -> emit(Outcome.failure(ex)) }

fun <InputType, OutputType> modifyFlow2(
    data: Flow<InputType>,
    transformer: Transformer<InputType, OutputType>
): Flow<OutputType> {
    return data.execute2(transformer.convert)
}

fun <InputType, OutputType> Flow<InputType>.execute2(convert: (InputType) -> OutputType): Flow<OutputType> =
    this.flowOn(Dispatchers.IO)
        .map { dto -> convert(dto) }
