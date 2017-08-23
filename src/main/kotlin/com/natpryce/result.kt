package com.natpryce


sealed class Result<out T, out E>
data class Ok<out T>(val value: T) : Result<T, Nothing>()
data class Err<out E>(val reason: E) : Result<Nothing, E>()

inline fun <T> resultFrom(block: () -> T): Result<T, Exception> =
    try {
        Ok(block())
    }
    catch (x: Exception) {
        Err(x)
    }
    catch (t: Throwable) {
        throw t
    }

inline fun <T, U, E> Result<T, E>.map(f: (T) -> U): Result<U, E> = when (this) {
    is Ok<T> -> Ok(f(value))
    is Err<E> -> this
}

inline fun <T, U, E> Result<T, E>.flatMap(f: (T) -> Result<U, E>): Result<U, E> = when (this) {
    is Ok<T> -> f(value)
    is Err<E> -> this
}

inline fun <T, E, F> Result<T, E>.mapError(f: (E) -> F): Result<T, F> = when (this) {
    is Ok<T> -> this
    is Err<E> -> Err(f(reason))
}

fun <T> Result<T, T>.get() = when (this) {
    is Ok<T> -> value
    is Err<T> -> reason
}

inline fun <T, E> Result<T, E>.onError(block: (Err<E>) -> Nothing): T = when (this) {
    is Ok<T> -> value
    is Err<E> -> block(this)
}

inline fun <T, E> Result<T, E>.peek(f: (T) -> Unit) =
    apply { if (this is Ok<T>) f(value) }

inline fun <T, E> Result<T, E>.peekError(f: (E) -> Unit) =
    apply { if (this is Err<E>) f(reason) }


/*
 * Translate between the Result and Nullable/Optional/Maybe monads
 */

inline fun <T, E> T?.asResultOr(errorSupplier: () -> E) =
    if (this != null) Ok(this) else Err(errorSupplier())

fun <T, E> Result<T, E>.valueOrNull() = when (this) {
    is Ok<T> -> value
    is Err<E> -> null
}

fun <T, E> Result<T, E>.errorOrNull() = when (this) {
    is Ok<T> -> null
    is Err<E> -> reason
}
