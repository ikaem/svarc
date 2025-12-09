package com.imkaem.android.svarc.core.utils.converters

import com.imkaem.android.svarc.core.utils.values.PeriodAmountValue
import com.imkaem.android.svarc.expenses.domain.models.ExpenseModel
import java.time.Instant
import java.time.Month
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Locale


class PeriodAmountValueConverters {

    /* TODO this should be reworked */
    companion object {

        fun fromMonthExpensesToDatePeriodAmountAccumulatedRemainderValue(
            expenses: List<ExpenseModel>,
            date: Instant,
            dailyBudget: Long,
        ): PeriodAmountValue {

            /* actually, lets first sort expenses
            * i guess we should sort everywhere always - but this shit sould come sorted
            * */

//            so i guess here we need to check if date is within provided expenses range
            /* or, we need to check if expenses are within range of month of date
            * lets do that
            * */

            val dateZonedDateTime = date.atZone(ZoneOffset.UTC)
            val dateYear = dateZonedDateTime.year
            val dateMonth = dateZonedDateTime.monthValue

            val dateDay = dateZonedDateTime.dayOfMonth
            val daysInMonth = YearMonth.of(dateYear, dateMonth).lengthOfMonth()

            val monthStartZonedDateTime = ZonedDateTime.of(
                dateYear,
                dateMonth,
                1,
                0, 0, 0, 0,
                ZoneOffset.UTC,
            )
            val monthEndZonedDateTime = ZonedDateTime.of(
                dateYear,
                dateMonth,
                daysInMonth,
                23, 59, 59, 999_999_999,
                ZoneOffset.UTC
            )


//            val firstExpense = expenses.first()
//            val lastExpense = expenses.last()
//
//
//            val firstExpenseDateTime = firstExpense.dateTime
//            val lastExpenseDateTime = lastExpense.dateTime

            /* we filter to include only the date month's expenses */
            /* we should extract this logic */

            val onlyDateMonthExpenses = expenses.filter { it ->

                val expenseDate = it.dateTime

                val isInvalidDate =
                    expenseDate.isBefore(monthStartZonedDateTime.toInstant()) || expenseDate.isAfter(
                        monthEndZonedDateTime.toInstant()
                    )

                !isInvalidDate
            }


            /* if there is no expenses, then accumated budget is date day * budget */
            if (onlyDateMonthExpenses.isEmpty()) {
                val accumulatedRemainder = dateDay * dailyBudget
                val value = PeriodAmountValue(
                    startDate = ZonedDateTime.of(
                        dateYear,
                        dateMonth,
                        dateDay,
                        0, 0, 0, 0,
                        ZoneOffset.UTC
                    ).toInstant(),
                    endDate = ZonedDateTime.of(
                        dateYear,
                        dateMonth,
                        dateDay,
                        23, 59, 59, 999_999_999,
                        ZoneOffset.UTC
                    ).toInstant(),
                    amount = accumulatedRemainder
                )

                return value
            }


//            if (firstExpenseDateTime.isBefore(monthStartZonedDateTime.toInstant())) {
//                throw Exception("Expenses are out of range of month start")
//            }
//
//            if (lastExpenseDateTime.isAfter(monthEndZonedDateTime.toInstant())) {
//                throw Exception("Expenses are out of range of month end")
//            }

            /* now we know that:
            * expenses are not empty
            * first expense is after or equal month start
            * last expense is before or equal month end
            * */

            /* lets create map of expenses */

            val expensesByDateString = onlyDateMonthExpenses.groupBy { expense ->
                /* need to create date with begining of day */
                val zonedDateTime = expense.dateTime.atZone(ZoneOffset.UTC)
                val year = zonedDateTime.year
                val month = zonedDateTime.monthValue
                val day = zonedDateTime.dayOfMonth

                val formattedDate =
                    String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day)

                formattedDate
            }

            /* ok, now it can happen that we did not have expenses on every day.
            * lets make sure the map has entry for each day of the month
            * */

            /* lets loop over days in month range.
            * we will check if each day exists in map
            * if it does not exist, we will add it with empty list
            * for this, we have to start a new map? because i guess we cannot modify existing one while iterating it -> i mean, we cannot inject new keys between existing ones, to add missing days in correct place?
            *  */

            val completeExpensesByDateString = mutableMapOf<String, List<ExpenseModel>>()

            for (day in 1..daysInMonth) {
                /* TODO should create a function (and test it) for this ... */
                val formattedDate =
                    String.format(Locale.getDefault(), "%04d-%02d-%02d", dateYear, dateMonth, day)
                val existingExpensesForDate = expensesByDateString[formattedDate]
                if (existingExpensesForDate == null) {
                    completeExpensesByDateString[formattedDate] = emptyList()
                    continue
                }
                completeExpensesByDateString[formattedDate] = existingExpensesForDate
            }

            /* ok, now we have complete map */

            /* now we have to loop over it */
            val accumulatedRemaindersByDay = mutableMapOf<String, PeriodAmountValue>()

            /* how to loop over a map */
            for (key in completeExpensesByDateString.keys) {

                /* now, if the accumulated remainders map is empty, we know it is first one
                * so accumulated remainder is just a simple remainder for that day
                * */

                if (accumulatedRemaindersByDay.isEmpty()) {
                    val expensesForDay = completeExpensesByDateString[key]!!
                    val totalSpentForDay = expensesForDay.sumOf { it.amount }
                    val totalRemainderForDay = dailyBudget - totalSpentForDay

                    val zonedDateTimeStart = ZonedDateTime.of(
                        dateYear,
                        dateMonth,
                        key.takeLast(2).toInt(),
                        /* TODO we know we are first */
//                        accumulatedRemaindersByDay.size + 1,
                        0, 0, 0, 0,
                        ZoneOffset.UTC
                    )

                    val zonedDateTimeEnd = ZonedDateTime.of(
                        dateYear,
                        dateMonth,
                        key.takeLast(2).toInt(),
                        /* TODO we know we are first */
//                        accumulatedRemaindersByDay.size + 1,
                        23, 59, 59, 999_999_999,
                        ZoneOffset.UTC
                    )

                    val periodAmountValue = PeriodAmountValue(
                        startDate = zonedDateTimeStart.toInstant(),
                        endDate = zonedDateTimeEnd.toInstant(),
                        amount = totalRemainderForDay,
                    )

                    accumulatedRemaindersByDay[key] = periodAmountValue

                    continue
                }

                /* ok, we know we are not first anymore */

                /* lets grab accumulated remainder from day before */
//                val previousDayIndex = accumulatedRemaindersByDay.size
                val currentDayIndex = key.takeLast(2).toInt()
                val previousDayIndex = currentDayIndex - 1
                val previousDayKey =
                    String.format(
                        Locale.getDefault(),
                        "%04d-%02d-%02d",
                        dateYear,
                        dateMonth,
                        previousDayIndex
                    )

                val previousDayAccumulatedRemainderValue =
                    accumulatedRemaindersByDay[previousDayKey]!!

                val expensesForDay = completeExpensesByDateString[key]!!
                val totalSpentForDay = expensesForDay.sumOf { it.amount }
                val totalRemainderForDay = dailyBudget - totalSpentForDay
                val accumulatedRemainderForDay =
                    previousDayAccumulatedRemainderValue.amount + totalRemainderForDay

                val zonedDateTimeStart = ZonedDateTime.of(
                    dateYear,
                    dateMonth,
                    key.takeLast(2).toInt(),
                    /* TODO we know we are not first */
//                    accumulatedRemaindersByDay.size + 1,
                    0, 0, 0, 0,
                    ZoneOffset.UTC
                )

                val zonedDateTimeEnd = ZonedDateTime.of(
                    dateYear,
                    dateMonth,
                    key.takeLast(2).toInt(),
                    /* TODO we know we are not first */
//                    accumulatedRemaindersByDay.size + 1,
                    23, 59, 59, 999_999_999,
                    ZoneOffset.UTC
                )

                val periodAmountValue = PeriodAmountValue(
                    startDate = zonedDateTimeStart.toInstant(),
                    endDate = zonedDateTimeEnd.toInstant(),
                    amount = accumulatedRemainderForDay,
                )

                accumulatedRemaindersByDay[key] = periodAmountValue

//                accumulatedRemaindersByDay.put()

            }


            /* now we need formated provided date */
            val todayFormattedDate =
                String.format(
                    Locale.getDefault(),
                    "%04d-%02d-%02d",
                    dateYear,
                    dateMonth,
                    dateDay
                )

            val todayAccumulatedRemainderValue = accumulatedRemaindersByDay[todayFormattedDate]!!

            return todayAccumulatedRemainderValue


//            -----------------------------

            /* idea here is that we have to actually create accummulated expense for every day in the month */

            /* then we just find today */

            /* and we return today */
        }

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

            /* TODO i guess we should filter expenses to include only expenses in the date's month */

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

        /* --- month --- */
        fun fromMonthExpensesToMonthPeriodAmountSpentValue(
            year: Int,
            month: Int,
            expenses: List<ExpenseModel>
        ): PeriodAmountValue {

            val daysInMonth = YearMonth.of(year, month).lengthOfMonth()

            val monthStartDate = ZonedDateTime.of(
                year,
                month,
                1,
                0, 0, 0, 0,
                ZoneOffset.UTC
            ).toInstant()
            val monthEndDate = ZonedDateTime.of(
                year,
                month,
                daysInMonth,
                23, 59, 59, 999_999_999,
                ZoneOffset.UTC
            ).toInstant()


            val monthExpenses = filterExpensesToDateMonth(
                monthStartDate,
                expenses
            )

            /* now we calculcate all expenses */

            val totalSpent = monthExpenses.sumOf { it.amount }

            val value = PeriodAmountValue(
                startDate = monthStartDate,
                endDate = monthEndDate,
                amount = totalSpent,
            )

            return value

        }

        fun fromDailyBudgetToMonthPeriodAmountBudgetValue(
            year: Int,
            month: Int,
            dailyBudget: Long
        ): PeriodAmountValue {

            val daysInMonth = YearMonth.of(year, month).lengthOfMonth()

            val monthStartDate = ZonedDateTime.of(
                year,
                month,
                1,
                0, 0, 0, 0,
                ZoneOffset.UTC
            ).toInstant()
            val monthEndDate = ZonedDateTime.of(
                year,
                month,
                daysInMonth,
                23, 59, 59, 999_999_999,
                ZoneOffset.UTC
            ).toInstant()

            val totalBudget = daysInMonth * dailyBudget

            val value = PeriodAmountValue(
                startDate = monthStartDate,
                endDate = monthEndDate,
                amount = totalBudget,
            )

            return value
        }


        fun fromMonthExpensesToMonthPeriodAmountRemainderValue(
            year: Int,
            month: Int,
            expenses: List<ExpenseModel>,
            dailyBudget: Long
        ): PeriodAmountValue {
            val spentValue = fromMonthExpensesToMonthPeriodAmountSpentValue(
                year,
                month,
                expenses
            )

            val budgetValue = fromDailyBudgetToMonthPeriodAmountBudgetValue(
                year,
                month,
                dailyBudget
            )

            val remainderAmount = budgetValue.amount - spentValue.amount

            val remainderValue = PeriodAmountValue(
                startDate = budgetValue.startDate,
                endDate = budgetValue.endDate,
                amount = remainderAmount,
            )

            return remainderValue
        }


        /* --- helpers --- */
        private fun filterExpensesToDateMonth(
            date: Instant,
            expenses: List<ExpenseModel>
        ): List<ExpenseModel> {
            val dateZonedDateTime = date.atZone(ZoneOffset.UTC)
            val dateYear = dateZonedDateTime.year
            val dateMonth = dateZonedDateTime.monthValue
            val dateDay = dateZonedDateTime.dayOfMonth

            val daysInMonth = YearMonth.of(dateYear, dateMonth).lengthOfMonth()

            val monthStartZonedDateTime = ZonedDateTime.of(
                dateYear,
                dateMonth,
                1,
                0, 0, 0, 0,
                ZoneOffset.UTC,
            )
            val monthEndZonedDateTime = ZonedDateTime.of(
                dateYear,
                dateMonth,
                daysInMonth,
                23, 59, 59, 999_999_999,
                ZoneOffset.UTC
            )

            val onlyDateMonthExpenses = expenses.filter { it ->

                val expenseDate = it.dateTime

                val isInvalidDate =
                    expenseDate.isBefore(monthStartZonedDateTime.toInstant()) || expenseDate.isAfter(
                        monthEndZonedDateTime.toInstant()
                    )

                !isInvalidDate
            }

            return onlyDateMonthExpenses
        }
    }
}