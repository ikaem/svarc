package com.imkaem.android.svarc.core.utils.helpers

import com.imkaem.android.svarc.core.utils.values.ExpenseReportValue
import com.imkaem.android.svarc.expenses.utils.values.DateSpentValue
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime


/* This class might be removed. It is helping with calculation of report value for a specific period
* it is meant for the home screen quick reports
*  */
class ExpensesReportValueCalculationHelper {
    companion object {

        fun getTodaySpentExpenseReportValue(
            monthSpentValues: List<DateSpentValue>
        ): ExpenseReportValue {

            /* this one is easy - we just find value that has date same as today */

            val nowInstant = Instant.now()

            /* get now year, month, and day */
            val nowZonedDateTime = nowInstant.atZone(java.time.ZoneOffset.UTC)
            val nowYear = nowZonedDateTime.year
            val nowMonth = nowZonedDateTime.monthValue
            val nowDay = nowZonedDateTime.dayOfMonth


            val normalizedNowInstant = Instant.from(
                ZonedDateTime.of(
                    nowYear,
                    nowMonth,
                    nowDay,
                    0,0,0,0,
                    ZoneOffset.UTC,
                )
            )

            val oneDayMillis = 24 * 60 * 60 * 1000L
            val normalizedTomorrowInstant = normalizedNowInstant.plusMillis(oneDayMillis)

            /* now we search through all other pšrovided values to find one that is higher or equal than today, or lower thqan tomrorow */

            val todayDateSpent = monthSpentValues.single { it ->

                val isMatch = it.date >= normalizedNowInstant && it.date < normalizedTomorrowInstant

                return@single isMatch
            }

            val todayExpenseReportValue = ExpenseReportValue(
                date = todayDateSpent.date,
                amount = todayDateSpent.amount,
            )

            return todayExpenseReportValue
        }

        fun getThisMonthSpentExpenseReportValue(
            monthSpentValues: List<DateSpentValue>
        ): ExpenseReportValue {


            /* ok, lets do this
            * need to get spending for entire month - do it by actually accumulating all values that are in this month
            *
            *
            * */
            val total = monthSpentValues.sumOf { it.amount }
            val date = monthSpentValues.last().date

            return ExpenseReportValue(
                date = date,
                amount = total,
            )
        }
    }


}