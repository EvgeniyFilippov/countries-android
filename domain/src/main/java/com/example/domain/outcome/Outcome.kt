package com.example.domain.outcome

import java.io.Serializable

sealed class Outcome<T> : Serializable {

	data class Progress<T>(var loading: Boolean) : Outcome<T>()
	data class Success<T>(var data: T) : Outcome<T>()
	data class Next<T>(var data: T) : Outcome<T>()
	data class Failure<T>(val e: Throwable) : Outcome<T>()

	companion object {
		fun <T> loading(isLoading: Boolean): Outcome<T> =
			Progress(isLoading)

		fun <T> success(data: T): Outcome<T> =
			Success(data)

		fun <T> failure(e: Throwable): Outcome<T> =
			Failure(e)

		fun <T> next(data: T): Outcome<T> =
			Next(data)
	}
}