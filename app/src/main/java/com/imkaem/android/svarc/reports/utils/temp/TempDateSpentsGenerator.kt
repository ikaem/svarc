package com.imkaem.android.svarc.reports.utils.temp

import com.imkaem.android.svarc.costs.utils.values.DateSpentValue
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.ZonedDateTime

class TempDateSpentsGenerator {


    companion object {

        fun fillMonthDateSpentsGaps(

            dateSpents: List<DateSpentValue>,
            month: Int,
            year: Int,
        ): List<DateSpentValue> {

            /* ok, how to do this
            * we create a map of existing dateSpents by day of month
            * lets createa a list of all days in a month, based on year and month,
            * then then we map over this new list, and check if we have each day in the map
            * if do, we return existing date spent
            * if we dont, we create a new one with 0 amount
            *
            *
            * */

            val map = dateSpents.associateBy { it ->
                val zonedDateTime = it.date.atZone(ZoneOffset.UTC)
                val day = zonedDateTime.dayOfMonth

                day
            }

            val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
            val fullMonthDateSpends = (1..daysInMonth).map { day ->

                val existing = map[day]
                if (existing != null) {
                    return@map existing
                }

                val zonedDateTime = ZonedDateTime.of(
                    year,
                    month,
                    day,
                    0, 0, 0, 0, ZoneOffset.UTC,
                )

                val newDateSpent = DateSpentValue(
                    amount = 0,
                    date = zonedDateTime.toInstant(),
                )

                newDateSpent
            }

            return fullMonthDateSpends
        }

        fun getTempMonthDateSpents(
            year: Int,
            month: Int,
        ): List<DateSpentValue> {
//            val now = Instant.now().atZone(ZoneOffset.UTC)
//            val year = now.year
//            val month = now.monthValue
            val daysList = mutableListOf<Int>(
                1,
                3,
                4,
                5,
                8,
                11,
                12,
                13,
                15,
                16,
                18,
                20,
                21,
                22,
                25,
                26,
                28,
            )

            val daysInMonth = YearMonth.of(year, month).lengthOfMonth()

            if (daysInMonth >= 29) {
                daysList.add(29)
            }

            if (daysInMonth >= 31) {
                daysList.add(31)
            }

            val dateSpents = daysList.map { day ->

                val randomNumber = (1..10).random()
                val amount = randomNumber * 100

                val zonedDateTime: ZonedDateTime = ZonedDateTime.of(
                    year,
                    month,
                    day,
                    0, 0, 0, 0, ZoneOffset.UTC,
                )

                val spent = DateSpentValue(
                    amount,
                    date = zonedDateTime.toInstant(),
                )

                spent
            }

            return dateSpents
        }
    }
}