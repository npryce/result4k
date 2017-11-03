package com.natpryce

/**
 * A result of a computation that can succeed or fail.
 */
sealed class Result<out T, out E>
data class Ok<out T>(val value: T) : Result<T, Nothing>()
data class Err<out E>(val reason: E) : Result<Nothing, E>()

/**
 * Call a function and wrap the result in a Result, catching any Exception and returning it as Err value.
 */
inline fun <T> resultFrom(block: () -> T): Result<T, Exception> =
    try {
        Ok(block())
    }
    catch (x: Exception) {
        Err(x)
    }

/**
 * Map a function over the _value_ of a successful Result.
 */
inline fun <T, U, E> Result<T, E>.map(f: (T) -> U): Result<U, E> = when (this) {
    is Ok<T> -> Ok(f(value))
    is Err<E> -> this
}

/**
 * Flat-map a function over the _value_ of a successful Result.
 */
inline fun <T, U, E> Result<T, E>.flatMap(f: (T) -> Result<U, E>): Result<U, E> = when (this) {
    is Ok<T> -> f(value)
    is Err<E> -> this
}

/**
 * Map a function over the _reason_ of an unsuccessful Result.
 */
inline fun <T, E, F> Result<T, E>.mapError(f: (E) -> F): Result<T, F> = when (this) {
    is Ok<T> -> this
    is Err<E> -> Err(f(reason))
}

/**
 * Flat-map a function over the _reason_ of a unsuccessful Result.
 */
inline fun <T, E, F> Result<T, E>.flatMapError(f: (E) -> Result<T, F>): Result<T, F> = when (this) {
    is Ok<T> -> this
    is Err<E> -> f(reason)
}

/**
 * Unwrap a Result in which both the success and error values have the same type, returning a plain value.
 */
fun <T> Result<T, T>.get() = when (this) {
    is Ok<T> -> value
    is Err<T> -> reason
}

/**
 * Unwrap a Result, by returning the success value or calling _block_ on error to abort from the current function.
 */
inline fun <T, E> Result<T, E>.onError(block: (Err<E>) -> Nothing): T = when (this) {
    is Ok<T> -> value
    is Err<E> -> block(this)
}

/**
 * Unwrap a Result by returning the success value or calling _errorToValue_ to mapping the error reason to a plain value.
 */
inline fun <T,E> Result<T,E>.recover(errorToValue: (E)->T) = when (this) {
    is Ok<T> -> value
    is Err<E> -> errorToValue(reason)
}

/**
 * Perform a side effect with the success value.
 */
inline fun <T, E> Result<T, E>.peek(f: (T) -> Unit) =
    apply { if (this is Ok<T>) f(value) }

/**
 * Perform a side effect with the error reason.
 */
inline fun <T, E> Result<T, E>.peekError(f: (E) -> Unit) =
    apply { if (this is Err<E>) f(reason) }

