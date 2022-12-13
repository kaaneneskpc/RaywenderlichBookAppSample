package com.raywenderlich.android.librarian.utils

inline fun <E: Any, T: Collection<E>> T?.withNotNullNorEmptyChain(func: T.() -> Unit): T? {
    if (this != null && this.isNotEmpty()) {
        with (this) { func() }
    }
    return this
}

inline fun  <E: Any, T: Collection<E>, R: Any> T?.whenNotNullNorEmptyChain(func: (T) -> R?): R? {
    if (this != null && this.isNotEmpty()) {
        return func(this)
    }
    return null
}

inline fun <E: Any, T: Collection<E>> T?.withNullOrEmptyChain(func: () -> Unit): T? {
    if (this == null || this.isEmpty()) {
        func()
    }
    return this
}

inline fun <E: Any, T: Collection<E>, R: Any> T?.whenNullOrEmptyChain(func: () -> R?): R?  {
    if (this == null || this.isEmpty()) {
        return func()
    }
    return null
}

inline fun <E : Any, T : Collection<E>> T?.withNotNullNorEmpty(func: T.() -> Unit): Otherwise {
    return if (this != null && this.isNotEmpty()) {
        with (this) { func() }
        OtherwiseIgnore
    } else {
        OtherwiseInvoke
    }
}

inline fun  <E : Any, T : Collection<E>> T?.whenNotNullNorEmpty(func: (T) -> Unit): Otherwise {
    return if (this != null && this.isNotEmpty()) {
        func(this)
        OtherwiseIgnore
    } else {
        OtherwiseInvoke
    }
}

inline fun <E : Any, T : Collection<E>> T?.withNullOrEmpty(func: () -> Unit): OtherwiseWithValue<T> {
    return if (this == null || this.isEmpty()) {
        func()
        OtherwiseWithValueIgnore()
    } else {
        OtherwiseWithValueInvoke(this)
    }
}

inline fun <E : Any, T : Collection<E>> T?.whenNullOrEmpty(func: () -> Unit): OtherwiseWhenValue<T> {
    return if (this == null || this.isEmpty()) {
        func()
        OtherwiseWhenValueIgnore()
    } else {
        OtherwiseWhenValueInvoke(this)
    }
}

interface Otherwise {
    fun otherwise(func: () -> Unit)
}

object OtherwiseInvoke : Otherwise {
    override fun otherwise(func: () -> Unit) {
        func()
    }
}

object OtherwiseIgnore : Otherwise {
    override fun otherwise(func: () -> Unit) = Unit
}

interface OtherwiseWithValue<T> {
    fun otherwise(func: T.() -> Unit)
}

class OtherwiseWithValueInvoke<T>(val value: T) : OtherwiseWithValue<T> {
    override fun otherwise(func: T.() -> Unit) {
        with (value) { func() }
    }
}

class OtherwiseWithValueIgnore<T> : OtherwiseWithValue<T> {
    override fun otherwise(func: T.() -> Unit) = Unit
}

interface OtherwiseWhenValue<T> {
    fun otherwise(func: (T) -> Unit)
}

class OtherwiseWhenValueInvoke<T>(val value: T) : OtherwiseWhenValue<T> {
    override fun otherwise(func: (T) -> Unit) {
        func(value)
    }
}

class OtherwiseWhenValueIgnore<T> : OtherwiseWhenValue<T> {
    override fun otherwise(func: (T) -> Unit) = Unit
}

inline fun <T: Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
    if (elements.all { it != null }) {
        closure(elements.filterNotNull())
    }
}