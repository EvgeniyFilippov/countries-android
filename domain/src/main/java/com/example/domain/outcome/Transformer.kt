package com.example.domain.outcome

interface Transformer<InputType, OutputType> {
    var convert: (InputType) -> OutputType
}