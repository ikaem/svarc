package com.imkaem.android.svarc.core.utils.extensions

import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

// filename: InstantExtensions.kt

fun Instant.toDayStart(zoneOffset: ZoneOffset = ZoneOffset.UTC): Instant {
//    val zonedDateTime = this.atZone(zoneOffset)
//    val startOfDay = ZonedDateTime.of(
//        zonedDateTime.year,
//        zonedDateTime.monthValue,
//        zonedDateTime.dayOfMonth,
//        0, 0, 0, 0,
//        zoneOffset,
//    ).toInstant()
//
//    return startOfDay

    return atZone(zoneOffset).toLocalDate().atStartOfDay(zoneOffset).toInstant()

}

fun Instant.toDayEnd(zoneOffset: ZoneOffset = ZoneOffset.UTC): Instant {
//    val zonedDateTime = this.atZone(zoneOffset)
//    val endOfDay = ZonedDateTime.of(
//        zonedDateTime.year,
//        zonedDateTime.monthValue,
//        zonedDateTime.dayOfMonth,
//        23, 59, 59, 999_999_999,
//        zoneOffset,
//    ).toInstant()
//
//    return endOfDay

    return atZone(zoneOffset).toLocalDate().atTime(23, 59, 59, 999_999_999).atZone(zoneOffset)
        .toInstant()
}