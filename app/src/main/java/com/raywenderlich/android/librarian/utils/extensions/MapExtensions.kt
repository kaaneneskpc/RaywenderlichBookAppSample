package com.raywenderlich.android.librarian.utils

fun <K, V> Map<K, V>.mergeReduce(
        other: Map<K, V>,
        reduce: (V, V) -> V = { _, b -> b }
): Map<K, V> {

    val result = LinkedHashMap<K, V>(this.size + other.size)
    result.putAll(this)
    for ((key, value) in other) {
        result[key] = result[key]?.let { reduce(it, value) } ?: value
    }
    return result
}