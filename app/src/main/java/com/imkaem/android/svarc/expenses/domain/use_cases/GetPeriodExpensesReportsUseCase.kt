package com.imkaem.android.svarc.expenses.domain.use_cases

import com.imkaem.android.svarc.core.utils.converters.PeriodAmountValueConverters
import com.imkaem.android.svarc.core.utils.values.CurrentPeriodsAmountValues
import com.imkaem.android.svarc.core.utils.values.PeriodAmountValue
import com.imkaem.android.svarc.core.utils.values.ThisMonthPeriodAmountValues
import com.imkaem.android.svarc.core.utils.values.TodayPeriodAmountValues
import com.imkaem.android.svarc.expenses.data.repositories.ExpensesRepository
import com.imkaem.android.svarc.expenses.domain.models.ExpenseModel
import com.imkaem.android.svarc.reports.utils.ExpensesDatesGapsFiller
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import javax.inject.Inject

class GetPeriodExpensesReportsUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {

    /* TODO this should be called GetCurrentExpensesReportsUseCase */
    /* TODO and this should be tested as well
    *   and it should return a flow */

    /* TODO we should test this as well*/

    /* TODO ok, here we have to create milliseconds from start of month, and milliseconds for start of next month - because start of next month will be exclusive */


    operator fun invoke(
        dailyBudget: Long,
        date: Instant,
    ): Flow<CurrentPeriodsAmountValues> {
        val dateZoned = date.atZone(ZoneOffset.UTC)


        val (fromMillisInclusive, toMillisInclusive) = getDateMonthMillis(date)

        val modelsFlow = expensesRepository.getExpensesFlow(
            fromMillisInclusive = fromMillisInclusive,
            toMillisExclusive = toMillisInclusive
        )

        val reportsFlow = modelsFlow.map { expenses ->
            /* now here we want to create all expenses */

            val todayPeriodAmountSpentValue =
                PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountSpentValue(
                    expenses,
                    date
                )
            val todayPeriodAmountRemainderValue =
                PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountRemainderValue(
                    expenses,
                    date,
                    dailyBudget,
                )
            val todayPeriodAmountAccumulatedRemainderValue =
                PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountAccumulatedRemainderValue(
                    expenses,
                    date,
                    dailyBudget,
                )
            val thisMonthPeriodAmountSpentValue =
                PeriodAmountValueConverters.fromMonthExpensesToMonthPeriodAmountSpentValue(
                    dateZoned.year,
                    dateZoned.monthValue,
                    expenses,
                )
            val thisMonthPeriodAmountBudgetValue =
                PeriodAmountValueConverters.fromDailyBudgetToMonthPeriodAmountBudgetValue(
                    dateZoned.year,
                    dateZoned.monthValue,
                    dailyBudget,
                )
            val thisMonthPeriodAmountRemainderValue =
                PeriodAmountValueConverters.fromMonthExpensesToMonthPeriodAmountRemainderValue(
                    dateZoned.year,
                    dateZoned.monthValue,
                    expenses,
                    dailyBudget,
                )

            val currentPeriodsAmountValues = CurrentPeriodsAmountValues(
                today = TodayPeriodAmountValues(
                    spent = todayPeriodAmountSpentValue,
                    remainder = todayPeriodAmountRemainderValue,
                    accumulatedRemainder = todayPeriodAmountAccumulatedRemainderValue,
                ),
                thisMonth = ThisMonthPeriodAmountValues(
                    spent = thisMonthPeriodAmountSpentValue,
                    budget = thisMonthPeriodAmountBudgetValue,
                    remainder = thisMonthPeriodAmountRemainderValue,
                )
            )

            currentPeriodsAmountValues
        }

        return reportsFlow

        /* TODO this is dummy for now */
//        val dummyCurrentPeriodsAmountValues = CurrentPeriodsAmountValues(
//            today = TodayPeriodAmountValues(
//                spent = PeriodAmountValue(
////                    startDate = Instant.now(),
//                    startDate = ZonedDateTime.of(
//                        2025,
//                        11,
//                        26,
//                        0, 0, 0, 0,
//                        ZoneOffset.UTC,
//                    ).toInstant(),
//                    endDate = ZonedDateTime.of(
//                        2025,
//                        11,
//                        26,
//                        23, 59, 59, 999_999_999,
//                        ZoneOffset.UTC,
//                    ).toInstant(),
//                    amount = 1500L,
//                ),
//                remainder = PeriodAmountValue(
//                    startDate = ZonedDateTime.of(
//                        2025,
//                        11,
//                        26,
//                        0, 0, 0, 0,
//                        ZoneOffset.UTC,
//                    ).toInstant(),
//                    endDate = ZonedDateTime.of(
//                        2025,
//                        11,
//                        26,
//                        23, 59, 59, 999_999_999,
//                        ZoneOffset.UTC,
//                    ).toInstant(),
//                    amount = 1500L,
//                ),
//                accumulatedRemainder = PeriodAmountValue(
//                    startDate = ZonedDateTime.of(
//                        2025,
//                        11,
//                        26,
//                        0, 0, 0, 0,
//                        ZoneOffset.UTC,
//                    ).toInstant(),
//                    endDate = ZonedDateTime.of(
//                        2025,
//                        11,
//                        26,
//                        23, 59, 59, 999_999_999,
//                        ZoneOffset.UTC,
//                    ).toInstant(),
//                    amount = 1500L,
//                ),
//            ),
//            thisMonth = ThisMonthPeriodAmountValues(
//                spent = PeriodAmountValue(
//                    startDate = ZonedDateTime.of(
//                        2025,
//                        11,
//                        1,
//                        0, 0, 0, 0,
//                        ZoneOffset.UTC,
//                    ).toInstant(),
//                    endDate = ZonedDateTime.of(
//                        2025,
//                        11,
//                        30,
//                        23, 59, 59, 999_999_999,
//                        ZoneOffset.UTC,
//                    ).toInstant(),
//                    amount = 60000L,
//                ),
//                budget = PeriodAmountValue(
//                    startDate = ZonedDateTime.of(
//                        2025,
//                        11,
//                        1,
//                        0, 0, 0, 0,
//                        ZoneOffset.UTC,
//                    ).toInstant(),
//                    endDate = ZonedDateTime.of(
//                        2025,
//                        11,
//                        30,
//                        23, 59, 59, 999_999_999,
//                        ZoneOffset.UTC,
//                    ).toInstant(),
//                    amount = 100000L,
//                ),
//                remainder = PeriodAmountValue(
//                    startDate = ZonedDateTime.of(
//                        2025,
//                        11,
//                        1,
//                        0, 0, 0, 0,
//                        ZoneOffset.UTC,
//                    ).toInstant(),
//                    endDate = ZonedDateTime.of(
//                        2025,
//                        11,
//                        30,
//                        23, 59, 59, 999_999_999,
//                        ZoneOffset.UTC,
//                    ).toInstant(),
//                    amount = 40000L,
//                ),
//            )
//        )
//
//        val valuesFlow = flowOf(dummyCurrentPeriodsAmountValues)
//
//        return valuesFlow
    }
}

//private fun

/* TODO probably move this to some converters, or similar...*/
private fun convertExpenseModelsToTodayPeriodsAmountValuesSpent(
    expenses: List<ExpenseModel>
): PeriodAmountValue {

    val expensesByDay = expenses.groupBy { expense ->

        /* need to group by day - so need to get day */
        val zonedDateTime = expense.dateTime.atZone(ZoneOffset.UTC)
        val day = zonedDateTime.dayOfMonth

        day
    }

    /* TODO here we want to create expenses by day without gaps */
    val now = Instant.now()
    val nowZoned = now.atZone(ZoneOffset.UTC)
    val currentYear = nowZoned.year
    val currentMonth = nowZoned.monthValue

    val gaplessExpensesByDay = ExpensesDatesGapsFiller.fillMonthExpensesMapGaps(
        expenses = expensesByDay,
        year = currentYear,
        month = currentMonth,
    )

    /* now we have expenses by day */

    /* now we want to create today period amount values:*/
    /* lets create spent by day */

    /* we we need to map over expenses by day, and accumulate all day expenses */
    val spentByDay = gaplessExpensesByDay.map { dayEntry ->

        val day = dayEntry.key
        val expensesForDay = dayEntry.value

        val totalSpentForDay = expensesForDay.sumOf { it.amount }
        val date = expensesForDay.first().dateTime

        /* calculate date start */
        val zonedDateTime = date.atZone(ZoneOffset.UTC)

        val dateYear = zonedDateTime.year
        val dateMonth = zonedDateTime.monthValue
        val dateDay = zonedDateTime.dayOfMonth

        val dateStartZoned =
            ZonedDateTime.of(
                dateYear,
                dateMonth,
                dateDay,
                0,
                0,
                0,
                0,
                ZoneOffset.UTC
            )

        val dateEndZoned = ZonedDateTime.of(
            dateYear,
            dateMonth,
            dateDay,
            23,
            59,
            59,
            999_999_999,
            ZoneOffset.UTC
        )

        val dateStartInstant = Instant.from(dateStartZoned)
        val dateEndInstant = Instant.from(dateEndZoned)


        val dayPeriodAmountValueSpent = PeriodAmountValue(
            startDate = dateStartInstant,
            endDate = dateEndInstant,
            amount = totalSpentForDay
        )

        dayPeriodAmountValueSpent


    }

    /* TODO this could have been done in a way more simple way - just initiall find today day, and calculate for that */
    val nowInstant = Instant.now()
    val nowZonedDateTime = nowInstant.atZone(ZoneOffset.UTC)

    val todayDay = nowZonedDateTime.dayOfMonth

    val todayPeriodAmountValueSpent = spentByDay.first { it ->
        val dayStartZoned = it.startDate.atZone(ZoneOffset.UTC)
        val day = dayStartZoned.dayOfMonth

        day == todayDay
    }

    return todayPeriodAmountValueSpent
}


private fun getDateMonthMillis(
    date: Instant
): Pair<Long, Long> {
    val dateZonedDateTime = date.atZone(ZoneOffset.UTC)
    val dateYear = dateZonedDateTime.year
    val dateMonth = dateZonedDateTime.monthValue

    val firstMomentOfMonthInstant = Instant.from(
        ZonedDateTime.of(
            dateYear,
            dateMonth,
            1,
            0,
            0,
            0,
            0,
            ZoneOffset.UTC
        )
    )

    val daysInMonth = dateZonedDateTime.toLocalDate().lengthOfMonth()
    val monthMillis = daysInMonth * 24 * 60 * 60 * 1000L

    val firstMomentOfNextMonth = firstMomentOfMonthInstant.plusMillis(monthMillis)

    val fromDateMilliseconds = firstMomentOfMonthInstant.toEpochMilli()
    val toDateMilliseconds = firstMomentOfNextMonth.toEpochMilli()

    return Pair(fromDateMilliseconds, toDateMilliseconds)

}