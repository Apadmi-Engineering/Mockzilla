package com.apadmi.mockzilla.demo.engine.network

sealed class DataResult<out T, out E> {
    fun dataOrNull() = when (this) {
        is Success -> ok
        is Failure -> null
    }

    fun errorOrNull() = when (this) {
        is Success -> null
        is Failure -> error
    }

    fun isSuccess() = this is Success

    /**
     * @property error
     */
    data class Failure<E>(val error: E) : DataResult<Nothing, E>()

    /**
     * @property ok
     */
    data class Success<T>(val ok: T) : DataResult<T, Nothing>()

    companion object
}
