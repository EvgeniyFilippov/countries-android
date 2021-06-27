package com.example.course_android

data class NewsRootObject(
    val status: String,
    val totalResults: Int,
    val articles: List<Articles>
)

data class Articles(
    val sourse: Source,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String
)

data class Source(
    val id: String,
    val name: String
)