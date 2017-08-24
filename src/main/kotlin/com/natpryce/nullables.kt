package com.natpryce

/*
 * Translate between the Result and Nullable/Optional/Maybe monads
 */

/**
 * Convert a nullable value to a Result, using the result of the errorSupplier as the error reason if the value is null.
 */
inline fun <T, E> T?.asResultOr(errorSupplier: () -> E) =
    if (this != null) Ok(this) else Err(errorSupplier())

/**
 * Returns the success value, or null if the Result is an error.
 */
fun <T, E> Result<T, E>.valueOrNull() = when (this) {
    is Ok<T> -> value
    is Err<E> -> null
}

/**
 * Returns the error reason, or null if the Result is a success.
 */
fun <T, E> Result<T, E>.errorOrNull() = when (this) {
    is Ok<T> -> null
    is Err<E> -> reason
}
