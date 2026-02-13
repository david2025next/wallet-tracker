package com.next.wallettracker.ui.utils

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Long.humanReadableDateMonth(
    zoneId: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.FRENCH
): String {

    val date = Instant.ofEpochMilli(this)
        .atZone(zoneId)
        .toLocalDate()
    val today = LocalDate.now()
    return when {
        date.isEqual(today.minusDays(1)) -> "Hier"
        date.isEqual(today) -> "Aujourd'hui"
        else -> date.format(DateTimeFormatter.ofPattern("d MMMM", locale))
    }
}


fun Long.toHumanDate(
    locale: Locale = Locale.FRENCH,
    zoneId: ZoneId = ZoneId.systemDefault()
): String {
    val date = Instant.ofEpochMilli(this)
        .atZone(zoneId)
        .toLocalDate()

    return DateTimeFormatter.ofPattern("d MMM yyyy", locale).format(date)

}

fun LocalDate.toMillis(zoneId: ZoneId = ZoneId.systemDefault()): Long {
    return this
        .atTime(LocalTime.now())
        .atZone(zoneId)
        .toInstant()
        .toEpochMilli()
}

data class DateRange(
    val start: Long,
    val end: Long
)

fun monthRange(zoneId: ZoneId = ZoneId.systemDefault()): DateRange {
    val today = LocalDate.now(zoneId)
    val start = today.withDayOfMonth(1)
    return DateRange(
        start.atStartOfDay(zoneId).toInstant().toEpochMilli(),
        start.plusMonths(1).atStartOfDay(zoneId).toInstant().toEpochMilli() - 1
    )
}

fun todayRange(zoneId: ZoneId = ZoneId.systemDefault()): DateRange {
    val today = LocalDate.now(zoneId)
    return DateRange(
        today.atStartOfDay(zoneId).toInstant().toEpochMilli(),
        today.plusDays(2).atStartOfDay(zoneId).toInstant().toEpochMilli() - 1
    )
}

fun weekRange(zoneId: ZoneId = ZoneId.systemDefault()): DateRange {
    val today = LocalDate.now(zoneId)
    val start = today.with(DayOfWeek.MONDAY)
    return DateRange(
        start.atStartOfDay(zoneId).toInstant().toEpochMilli(),
        start.plusDays(7).atStartOfDay(zoneId).toInstant().toEpochMilli() - 1
    )
}