package com.imkaem.android.svarc.core.utils.converters

import com.imkaem.android.svarc.core.utils.values.PeriodAmountValue
import com.imkaem.android.svarc.expenses.domain.models.ExpenseModel
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Locale

class PeriodAmountValueConverters {
    companion object {

        fun fromMonthExpensesToDatePeriodAmountRemainderValue(
            expenses: List<ExpenseModel>,
            date: Instant,
            dailyBudget: Long,
        ): PeriodAmountValue {
            val todaySpentValue = fromMonthExpensesToDatePeriodAmountSpentValue(
                expenses,
                date,
            )

            val remainder = dailyBudget - todaySpentValue.amount

            val remainderValue = todaySpentValue.copy(
                amount = remainder
            )

            return remainderValue
        }

        fun fromMonthExpensesToDatePeriodAmountSpentValue(
            expenses: List<ExpenseModel>,
            date: Instant,
        ): PeriodAmountValue {
            /* create map of day, month, year (probably just format it, need to create formatter */
            val expensesByDateString = expenses.groupBy { expense ->
                /* need to create date with begining of day */
                val zonedDateTime = expense.dateTime.atZone(ZoneOffset.UTC)
                val year = zonedDateTime.year
                val month = zonedDateTime.monthValue
                val day = zonedDateTime.dayOfMonth

                val formattedDate =
                    String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day)

                formattedDate
            }


            /* find today - format it like aboive */
//            val now = Instant.now()
            val dateZoned = date.atZone(ZoneOffset.UTC)
            val dateYear = dateZoned.year
            val dateMonth = dateZoned.monthValue
            val dateDay = dateZoned.dayOfMonth

            val todayFormattedDate =
                String.format(
                    Locale.getDefault(),
                    "%04d-%02d-%02d",
                    dateYear,
                    dateMonth,
                    dateDay
                )

            val todayStartDay = ZonedDateTime.of(
                dateYear,
                dateMonth,
                dateDay,
                0,
                0,
                0,
                0,
                ZoneOffset.UTC
            ).toInstant()

            val todayEndDay = ZonedDateTime.of(
                dateYear,
                dateMonth,
                dateDay,
                23,
                59,
                59,
                999_999_999,
                ZoneOffset.UTC
            ).toInstant()

            val todayExpenses = expensesByDateString[todayFormattedDate]
            if (todayExpenses == null) {
                val value = PeriodAmountValue(
                    startDate = todayStartDay,
                    endDate = todayEndDay,
                    amount = 0L,
                )

                return value
            }

            val totalSpent = todayExpenses.sumOf { it.amount }

            val value = PeriodAmountValue(
                startDate = todayStartDay,
                endDate = todayEndDay,
                amount = totalSpent,
            )

            return value
        }
    }
}