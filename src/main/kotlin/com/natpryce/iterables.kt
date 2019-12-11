package com.natpryce

fun <T, E> Iterable<Result<T, E>>.allValues(): Result<List<T>, E> =
    Success(map { r -> r.onFailure { return it } })

fun <T: Any,E> Iterable<Result<T,E>>.anyValues(): List<T> =
    mapNotNull { it.valueOrNull() }

fun <T, E> Iterable<Result<T, E>>.partition(): Pair<List<T>, List<E>> {
    val oks = mutableListOf<T>()
    val errs = mutableListOf<E>()
    forEach {
        @Suppress("UNCHECKED_CAST")
        when (it.repr) {
            is Error<*> -> errs.add(it.repr.reason as E)
            else -> oks.add(it.repr as T)
        }
    }
    return Pair(oks, errs)
}
