package com.natpryce


data class Error<out E>
    @PublishedApi internal constructor(val reason: E)

/**
 * A result of a computation that can succeed or fail.
 */
inline class Result<out T, out E>(val repr: Any?)

fun <T> Success(value: T) = Result<T,Nothing>(value)
fun <E> Failure(reason: E) = Result<Nothing,E>(Error(reason))

fun <T,E> Result<T,E>.isSuccess(): Boolean {
    return !isFailure()
}

fun <T,E> Result<T,E>.isFailure(): Boolean {
    return repr is Error<*>
}

/**
 * Call a function and wrap the result in a Result, catching any Exception and returning it as Failure.
 */
inline fun <T> resultFrom(block: () -> T): Result<T, Exception> =
    try {
        Success(block())
    }
    catch (x: Exception) {
        Failure(x)
    }

/**
 * Flat-map a function over the _value_ of a successful Result.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T, Tʹ, E> Result<T, E>.flatMap(f: (T) -> Result<Tʹ, E>): Result<Tʹ, E> =
    when(repr) {
        is Error<*> -> this as Result<Nothing,E>
        else -> f(repr as T)
    }

/**
 * Map a function over the _value_ of a successful Result.
 */
inline fun <T, Tʹ, E> Result<T, E>.map(f: (T) -> Tʹ): Result<Tʹ, E> =
    flatMap { value -> Success(f(value)) }

/**
 * Flat-map a function over the _reason_ of a unsuccessful Result.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T, E, Eʹ> Result<T, E>.flatMapFailure(f: (E) -> Result<T, Eʹ>): Result<T, Eʹ> =
    when (repr) {
    is Error<*> -> f(repr.reason as E)
    else -> this as Result<T,Nothing>
}

/**
 * Map a function over the _reason_ of an unsuccessful Result.
 */
inline fun <T, E, Eʹ> Result<T, E>.mapFailure(f: (E) -> Eʹ): Result<T, Eʹ> =
    flatMapFailure { reason -> Failure(f(reason)) }

/**
 * Unwrap a Result in which both the success and failure values have the same type, returning a plain value.
 */
@Suppress("UNCHECKED_CAST")
fun <T> Result<T, T>.get(): T = when (repr) {
    is Error<*> -> repr.reason
    else -> repr
} as T

/**
 * Unwrap a Result, by returning the success value or calling _block_ on failure to abort from the current function.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T, E> Result<T, E>.onFailure(block: (Result<Nothing,E>) -> Nothing): T = when (repr) {
    is Error<*> -> block(this as Result<Nothing,E>)
    else -> repr as T
}

/**
 * Unwrap a Result by returning the success value or calling _failureToValue_ to mapping the failure reason to a plain value.
 */
inline fun <S, T : S, U : S, E> Result<T, E>.recover(errorToValue: (E) -> U): S =
    mapFailure(errorToValue).get()

/**
 * Perform a side effect with the success value.
 */
inline fun <T, E> Result<T, E>.peek(f: (T) -> Unit) =
    apply {
        if (repr !is Error<*>) {
            @Suppress("UNCHECKED_CAST")
            f(repr as T)
        }
    }

/**
 * Perform a side effect with the failure reason.
 */
inline fun <T, E> Result<T, E>.peekFailure(f: (E) -> Unit) =
    apply {
        if (repr is Error<*>) {
            @Suppress("UNCHECKED_CAST")
            f(repr.reason as E)
        }
    }
