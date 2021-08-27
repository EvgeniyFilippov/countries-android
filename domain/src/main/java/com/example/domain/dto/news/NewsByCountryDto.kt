package com.example.domain.dto.news

data class NewsByCountryDto(
    val newsItems: List<NewsItemDto> = listOf(),
//    val status: String = "",
//    val totalResults: Int?
)