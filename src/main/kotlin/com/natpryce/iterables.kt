package com.natpryce


fun <T, E> Iterable<Result<T, E>>.allValues(): Result<List<T>, E> =
    Ok(map { r -> r.onError { return it } })

fun <T,E> Iterable<Result<T,E>>.anyValues(): List<T> =
    filterIsInstance<Ok<T>>().map { it.value }

fun <T, E> Iterable<Result<T, E>>.partition(): Pair<List<T>, List<E>> {
    val oks = mutableListOf<T>()
    val errs = mutableListOf<E>()
    forEach {
        when (it) {
            is Ok<T> -> oks.add(it.value)
            is Err<E> -> errs.add(it.reason)
        }
    }
    return Pair(oks, errs)
}
