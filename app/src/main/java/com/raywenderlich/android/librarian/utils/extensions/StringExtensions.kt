@file:Suppress("UNUSED_PARAMETER")

package com.raywenderlich.android.librarian.utils
import java.util.*

fun String.isDigitsOnly(): Boolean {
    this.toCharArray().forEach {
        if (it.isDigit().not()) {
            return false
        }
    }

    return true
}

fun String.trimLeadingZeros(): String {
    return trimStart { it == '0' }
}

fun String.removeAllSpaces(): String {
    return this.replace("\\s".toRegex(), "")
}

fun String?.wordCaps(): String? {
    this?.let { source ->
        val locale = Locale("tr", "TR")
        val words = source.lowercase(locale).split(' ')
        return words.joinToString(" ") { it1 -> it1.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() } }
    }

    return null
}

fun String?.toHashTag(): String {
    return "#$this"
}
fun String.formatHtmlNewLine(): String {
    return this.replace("(\r\n|\n)".toRegex(), "<br />")
}

fun java.lang.StringBuilder.addHtmlNewLine(): java.lang.StringBuilder {
    return this.append("<br/>")
}

fun String.clearDotAndComma(): String {
    return this.replace(",", "").replace(".", "")
}

fun String.clearNonDecimal(): String {
    return this.clearDotAndComma().removeAllSpaces().replace("TL", "")
}

fun String.clearFloatingSuffix(): String {
    return this.substringBefore(',')
}

fun String.getCVVFromDecryptedString(): String {
    return this.substringAfterLast(";")
}

fun String.clearCurrencyText(currency: String?): String {
    var rawData = this.replace(" ", "")
    currency?.let {
        it.forEachIndexed { _, element ->  rawData = rawData.replace(element.toString(), "")}
    }
    return rawData
}

fun String?.takeIfIsNotNullOrEmpty(): String? {
    return this.takeIf { !it.isNullOrEmpty() }
}

fun String.lastIndexOrNull(string: String, ignoreCase: Boolean = false): Int? {
    return this.lastIndexOf(string, ignoreCase = ignoreCase).takeIf { it != -1 }
}

fun String.indexOfOrZero(string: String, ignoreCase: Boolean = false): Int {
    return this.indexOf(string, ignoreCase = ignoreCase).takeIf { it != -1 } ?: 0
}

fun String.removeEmptyFieldsAndConvertToLowerCase(): String {
    return this.trim().lowercase(Locale.getDefault())
}

fun emptyString(): String {
    return ""
}