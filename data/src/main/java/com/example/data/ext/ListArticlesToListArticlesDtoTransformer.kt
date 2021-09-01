package com.example.data.ext

import com.example.data.NetConstants.DEFAULT_STRING
import com.example.data.model.newsByCountry.Article
import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Transformer

class ListArticlesToListArticlesDtoTransformer : Transformer<List<Article>, List<NewsItemDto>> {

    override var convert: (List<Article>) -> List<NewsItemDto> =
        { data ->
            data.map {
                NewsItemDto(
                    author = it.author ?: DEFAULT_STRING,
                    description = it.description ?: DEFAULT_STRING,
                    publishedAt = it.publishedAt ?: DEFAULT_STRING,
                    title = it.title ?: DEFAULT_STRING,
                    url = it.url ?: DEFAULT_STRING,
                    urlToImage = it.urlToImage ?: DEFAULT_STRING
                )
            }
        }

}