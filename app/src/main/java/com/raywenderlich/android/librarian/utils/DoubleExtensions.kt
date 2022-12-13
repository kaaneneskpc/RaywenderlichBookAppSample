package com.raywenderlich.android.librarian.utils

import java.text.NumberFormat
import java.util.*
import kotlin.math.round

/**
* Round `Double` number to certain number of decimal places.
*      - places: How many decimal places.
*      - roundingMode: How should number be rounded.
* Returns: The new rounded number
*/
fun Double.round(places: Int): Double {
    var multiplier = 1.0
    repeat(places) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

/**
 * Calculate changes between two `Double` as percentage
 *      -previousValue: Reference for previous double value.
 *      -fractionDigits: How many decimal places.
 *  Returns: The change as percentage.
 */
fun Double.percentageChange(previous: Double, fractionDigits: Int = 2): Double {
    val result = (((this - previous) / previous) * 100)
    return result.round(fractionDigits)
}

fun Double.roundWithLeadingZeros(places: Int): String? {
    val rounded = this.round(places)
    val nf = NumberFormat.getInstance(Locale.US)
    nf.minimumFractionDigits = places
    return nf.format(rounded)
}

fun Double?.toStringOrEmpty() : String {
    return this?.toString() ?: ""
}