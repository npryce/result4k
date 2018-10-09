package com.natpryce

/*
 * Translate between the Result and Nullable/Optional/Maybe monads
 */

/**
 * Convert a nullable value to a Result, using the result of the _failureSupplier_ as the failure reason if the value is null.
 */
inline fun <T, E> T?.asResultOr(failureSupplier: () -> E) =
    if (this != null) Success(this) else Failure(failureSupplier())

/**
 * Returns the success value, or null if the Result is a failure.
 */
fun <T, E> Result<T, E>.valueOrNull() = when (this) {
    is Success<T> -> value
    is Failure<E> -> null
}

/**
 * Returns the failure reason, or null if the Result is a success.
 */
fun <T, E> Result<T, E>.failureOrNull() = when (this) {
    is Success<T> -> null
    is Failure<E> -> reason
}
