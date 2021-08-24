package com.example.domain.dto.news

data class NewsByCountryDto(
    val articles: List<ArticleDto> = listOf(),
//    val status: String = "",
//    val totalResults: Int?
)