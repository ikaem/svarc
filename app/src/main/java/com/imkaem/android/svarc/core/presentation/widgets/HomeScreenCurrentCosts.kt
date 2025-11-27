package com.imkaem.android.svarc.core.presentation.widgets

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imkaem.android.svarc.core.utils.extensions.toDayEnd
import com.imkaem.android.svarc.core.utils.extensions.toDayStart
import com.imkaem.android.svarc.core.utils.values.PeriodAmountValue
import com.imkaem.android.svarc.expenses.domain.models.ExpenseWithCategoryModel
import com.imkaem.android.svarc.expenses.utils.values.DateSpentValue
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Composable
fun HomeScreenCurrentCosts(
    /* TODO this is temp, ideally this should return state */
    currentExpenses: List<ExpenseWithCategoryModel>,
    /* TODO all of this will be removed once we move this logic to view model */
    dailyBudget: Long,
) {

    val dateSpents = convertExpensesToDateSpents(
        monthExpenses = currentExpenses
    )

    val todayPeriodSpentValue = getTodayPeriodSpentValue(
        monthDateSpents = dateSpents
    )

    val currentMonthPeriodSpentValue = getCurrentMonthPeriodSpentValue(
        monthDateSpents = dateSpents
    )

    val todayPeriodRemainderAmountReportValue = getTodayPeriodRemainderAmountReportValue(
        monthDateSpents = dateSpents,
        dailyBudget = dailyBudget,
    )

    val thisMonthRemainderAmountReportValue = getThisMonthPeriodRemainderAmountReportValue(
        monthDateSpents = dateSpents,
        dailyBudget = dailyBudget,
    )

    val todayPeriodAccumulatedRemainderAmountReportValue = getTodayPeriodAccumulatedRemainderAmountReportValue(
        monthDateSpents = dateSpents,
        dailyBudget = dailyBudget,
    )


    /* TODO now we need to calculate all fields needed
    * TODO this calculaation will be delegated to view model later
    * */

    /* TODO lets try calculate week remainders just for the week - not to rely on the month for this data - or this maybe makes no sense? */

    /* BUT ACCUMULATED REMAINDER IS ALWAYS RELATIVE TO THE MONTH*/

    /* SO:
    * spent is for the period we are showing
    * remainder is for the period we are showing
    * accumulated remainder is always relative to the month
    *
    * */


    Column(
    ) {
        HomeScreenCurrentCostsToday(
            todayPeriodSpentValue = todayPeriodSpentValue,
            todayPeriodRemainderAmountReportValue = todayPeriodRemainderAmountReportValue,
            todayPeriodAccumulatedRemainderAmountReportValue = todayPeriodAccumulatedRemainderAmountReportValue,
            modifier = Modifier
                .padding(
                    top = 20.dp,
                    start = 10.dp,
                    end = 10.dp,
                    bottom = 40.dp
                )
                .fillMaxWidth()
        )
        /* TODO removing week because it makes no sense - it can leak into other months, a
        *   and if we just stikc to the month part of the week, it is no longer a week
        * */
//        HomeScreenCurrentCostsPeriod(
//            periodLabel = "THIS WEEK",
//            periodSpentValue = currentWeekPeriodSpentValue,
//            modifier = Modifier.fillMaxWidth()
//        )
        HomeScreenCurrentCostsPeriod(
            periodLabel = "THIS MONTH",
            periodSpentValue = currentMonthPeriodSpentValue,
            periodRemainderAmountReportValue = thisMonthRemainderAmountReportValue,
//            backgroundColor = ColorGreyLight,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/* TODO this should potentially be called differently - because it is not only spent value - it should be used for daily remainder and accumulated remainder */
/* TODO maybe PeriodAmountReportValue? */
//data class PeriodSpentValue(
//    /* this is both inclusive */
//    val startDate: Instant,
//    val endDate: Instant,
//    val amount: Long,
//)

/// accumulated remainder values
private fun getTodayPeriodAccumulatedRemainderAmountReportValue(
    monthDateSpents: List<DateSpentValue>,
    dailyBudget: Long,
): PeriodAmountValue? {
    if (monthDateSpents.isEmpty()) {
        return null
    }

    /* ok, what is the logic here */

    /* we first have to calculate accumulated remainder for each day
    * first day will have accumulated remainder of daily budget - spend that day
    * second day will have accumulated remainder of first day + daily budget - spend that day
    * third day will have accumulated remainder of second day + daily budget - spend that day
    * */


    val dailyPeriodAccumulatedRemainderAmountReportValues = mutableListOf<PeriodAmountValue>()

    for (dateSpent in monthDateSpents) {
        /* calculate start period instant */
        /* TODO we should create extension for this */

        /* calculate end period instant */


        /* calculate today daily remainder */
        val dailyRemainder = dailyBudget - dateSpent.amount

        /* now we have to calculate accumulted remiander */
        val yesterdayAccumulatedRemainder =
            dailyPeriodAccumulatedRemainderAmountReportValues.lastOrNull()?.amount ?: 0L

        val todayAccumulatedRemainder = yesterdayAccumulatedRemainder + dailyRemainder

        val normalizedDayStartInstant = dateSpent.date.toDayStart()
        val normalizedDayEndInstant = dateSpent.date.toDayEnd()

        val accumulatedRemainderAmountReportValue = PeriodAmountValue(
            startDate = normalizedDayStartInstant,
            endDate = normalizedDayEndInstant,
            amount = todayAccumulatedRemainder
        )

        dailyPeriodAccumulatedRemainderAmountReportValues.add(accumulatedRemainderAmountReportValue)
    }


    val now = Instant.now()
    val normalizedNowStartInstant = now.toDayStart()

    val todayAccumulatedRemainderAmountReportValue =
        dailyPeriodAccumulatedRemainderAmountReportValues.firstOrNull { it.startDate == normalizedNowStartInstant }

    if (todayAccumulatedRemainderAmountReportValue == null) {
        return null
    }

    return todayAccumulatedRemainderAmountReportValue


}


////////// daily remainder values
private fun getTodayPeriodRemainderAmountReportValue(
    monthDateSpents: List<DateSpentValue>,
    dailyBudget: Long,
): PeriodAmountValue? {


    if (monthDateSpents.isEmpty()) {
        /* we dont tolerate no entries */
        /* TODO in fact, we should have same number of entries as the current month has days.
        *   so maybe that can be validated as well */
        return null
    }

    val now = Instant.now()
    val zonedDateTime = now.atZone(ZoneOffset.UTC)

    val year = zonedDateTime.year
    val month = zonedDateTime.monthValue
    val day = zonedDateTime.dayOfMonth

    /* TODO this can potentially be extracted */
    val normalizedTodayStartInstant = ZonedDateTime.of(
        year,
        month,
        day,
        0, 0, 0, 0,
        ZoneOffset.UTC,
    ).toInstant()

    val normalizedTodayEndInstant = ZonedDateTime.of(
        year,
        month,
        day,
        23, 59, 59, 999_999_999,
        ZoneOffset.UTC
    ).toInstant()

    val todayDateSpent = monthDateSpents.firstOrNull { it.date == normalizedTodayStartInstant }
    if (todayDateSpent == null) {
        return null
    }

    val todaySpentAmount = todayDateSpent.amount
    val todayRemainderAmount = dailyBudget - todaySpentAmount

    val todayPeriodRemainderAmountReportValue = PeriodAmountValue(
        startDate = normalizedTodayStartInstant,
        endDate = normalizedTodayEndInstant,
        amount = todayRemainderAmount,
    )

    return todayPeriodRemainderAmountReportValue
}


private fun getThisMonthPeriodRemainderAmountReportValue(
    monthDateSpents: List<DateSpentValue>,
    dailyBudget: Long,
): PeriodAmountValue? {

    if (monthDateSpents.isEmpty()) {
        return null
    }

    /* we should also validate that current month days count and count of the list are same, and that it exists for each day - view model should be doing that */
    val daysCount = monthDateSpents.size
    val totalMonthBudget = daysCount * dailyBudget


    val monthSpentAmount = monthDateSpents.sumOf { it.amount }

    val monthRemainderAmount = totalMonthBudget - monthSpentAmount


    val monthStartDate = monthDateSpents.first().date
    val monthStartDateZonedDateTime = monthStartDate.atZone(ZoneOffset.UTC)
    val normalizedMonthStartInstant = ZonedDateTime.of(
        monthStartDateZonedDateTime.year,
        monthStartDateZonedDateTime.monthValue,
        monthStartDateZonedDateTime.dayOfMonth,
        0, 0, 0, 0,
        ZoneOffset.UTC
    ).toInstant()

    val monthEndDate = monthDateSpents.last().date
    val monthEndDateZonedDateTime = monthEndDate.atZone(ZoneOffset.UTC)
    val normalizedMonthEndInstant = ZonedDateTime.of(
        monthEndDateZonedDateTime.year,
        monthEndDateZonedDateTime.monthValue,
        monthEndDateZonedDateTime.dayOfMonth,
        23, 59, 59, 999_999_999,
        ZoneOffset.UTC
    ).toInstant()


    val thisMonthPeriodRemainderAmountReportValue = PeriodAmountValue(
        startDate = normalizedMonthStartInstant,
        endDate = normalizedMonthEndInstant,
        amount = monthRemainderAmount,
    )

    return thisMonthPeriodRemainderAmountReportValue

}

////////// spent values

private fun getCurrentMonthPeriodSpentValue(
    monthDateSpents: List<DateSpentValue>
): PeriodAmountValue? {

    if (monthDateSpents.isEmpty()) {
        return null
    }

    /* ok, we have to get first element, and normalize it to midnight */
    val monthStartDate = monthDateSpents.first().date
    val monthStartDateZonedDateTime = monthStartDate.atZone(ZoneOffset.UTC)
    val normalizedMonthStartInstant = ZonedDateTime.of(
        monthStartDateZonedDateTime.year,
        monthStartDateZonedDateTime.monthValue,
        monthStartDateZonedDateTime.dayOfMonth,
        0, 0, 0, 0,
        ZoneOffset.UTC
    ).toInstant()

    val monthEndDate = monthDateSpents.last().date
    val monthEndDateZonedDateTime = monthEndDate.atZone(ZoneOffset.UTC)
    val normalizedMonthEndInstant = ZonedDateTime.of(
        monthEndDateZonedDateTime.year,
        monthEndDateZonedDateTime.monthValue,
        monthEndDateZonedDateTime.dayOfMonth,
        0, 0, 0, 0,
        ZoneOffset.UTC
    ).toInstant()

    val monthSpentAmount = monthDateSpents.sumOf { it.amount }

    val monthPeriodSpentValue = PeriodAmountValue(
        startDate = normalizedMonthStartInstant,
        endDate = normalizedMonthEndInstant,
        amount = monthSpentAmount,
    )

    return monthPeriodSpentValue

}


private fun getTodayPeriodSpentValue(
    monthDateSpents: List<DateSpentValue>
): PeriodAmountValue? {

    if (monthDateSpents.isEmpty()) {
        return null
    }

    /* get normalized today */
    val now = Instant.now()
    val zonedDateTime = now.atZone(ZoneOffset.UTC)

    val year = zonedDateTime.year
    val month = zonedDateTime.monthValue
    val day = zonedDateTime.dayOfMonth

    val normalizedTodayStartInstant = ZonedDateTime.of(
        year,
        month,
        day,
        0, 0, 0, 0,
        ZoneOffset.UTC,
    ).toInstant()


    val normalizedTodayEndInstant = ZonedDateTime.of(
        year,
        month,
        day,
        23, 59, 59, 999_999_999,
        ZoneOffset.UTC,
    ).toInstant()


    val todayDateSpent = monthDateSpents.firstOrNull { it.date == normalizedTodayStartInstant }
    if (todayDateSpent == null) {
        return null
    }

    val todaySpentAmount = todayDateSpent.amount

    val todayPeriodSpentValue = PeriodAmountValue(
        startDate = normalizedTodayStartInstant,
        endDate = normalizedTodayEndInstant,
        amount = todaySpentAmount,
    )

    return todayPeriodSpentValue
}


private fun convertExpensesToDateSpents(
    monthExpenses: List<ExpenseWithCategoryModel>
    /* TODO there is a bug here, where if there is an empty list - no expenses, we will not be able to calculate spending and remainders later
    * so we will need to passy year and month here as well
    * it is future work
    * */
): List<DateSpentValue> {

    if (monthExpenses.isEmpty()) {
        return emptyList()
    }

    /* get map of expenses by day */
    val expensesByDay = monthExpenses.groupBy { it ->
        /* we have to group it by day, but normalized to midnight */

        /* TODO this should maybe becalled differently - not datetime because it is instant */
        val zonedDateTime = it.dateTime.atZone(ZoneOffset.UTC)
        val year = zonedDateTime.year
        val month = zonedDateTime.monthValue
        val day = zonedDateTime.dayOfMonth

        val normalizedInstant = ZonedDateTime.of(
            year,
            month,
            day,
            0, 0, 0, 0,
            ZoneOffset.UTC
        ).toInstant()

        normalizedInstant

//        val normalizedToMidnight = zonedDateTime.toLocalDate().atStartOfDay()

//        normalizedToMidnight
    }


    Log.d("expeses by day", "expensesByDay: $expensesByDay")

    /* now, we have to sum each day expenses */
    /* lets map over the map, and return sum of each date  */
    val dateSpents = expensesByDay.map { entry ->
        val costsSum = entry.value.sumOf { it.amount }

        DateSpentValue(
            amount = costsSum,
            date = entry.key,
        )
    }

    /* converting to map so it is easier to check later */
    val dateSpentsMap = dateSpents.associateBy { it.date }


    /* now we have to fill gaps */

    /* lets create a list of all dates for a particula motnh
    * get number of days in this month
    * */
    val firstExpense = monthExpenses.first()
    val firstExpenseZonedDateTime = firstExpense.dateTime.atZone(ZoneOffset.UTC)
    val year = firstExpenseZonedDateTime.year
    val month = firstExpenseZonedDateTime.monthValue

    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()

    /* now we can crete a list of all month date spents */
    val fullMonthDateSpents = (1..daysInMonth).map { dayInMonth ->
        /* lets construct normalized instant */
        val normalizedInstant = ZonedDateTime.of(
            year,
            month,
            dayInMonth,
            0, 0, 0, 0,
            ZoneOffset.UTC,
        ).toInstant()

        /* now we can check if this date exists in the map
        * if it exists, we return existing date spent
        * if it does not, we create a new one with 0 amount
        *  */

        val existing = dateSpentsMap[normalizedInstant]
        if (existing == null) {
            return@map DateSpentValue(
                amount = 0,
                date = normalizedInstant,
            )
        }

        return@map existing
    }

    return fullMonthDateSpents
}


/* not using week for now */

//private fun getCurrentWeekPeriodSpentValue(
//    monthDateSpents: List<DateSpentValue>
//): PeriodSpentValue? {
//
//    if (monthDateSpents.isEmpty()) {
//        return null
//    }
//
//    /* TODO we should do something in case this is empty list we receive as argument - because it can be empty because of initial state - or we should set loading, so this is never triggered? but this will be done in view model too...*/
//
//    /* get normalized today instant */
//    val now = Instant.now()
//    val zonedDateTime = now.atZone(ZoneOffset.UTC)
//
//    val year = zonedDateTime.year
//    val month = zonedDateTime.monthValue
//    val day = zonedDateTime.dayOfMonth
//
//    val normalizedTodayStartInstant = ZonedDateTime.of(
//        year,
//        month,
//        day,
//        0, 0, 0, 0,
//        ZoneOffset.UTC,
//    ).toInstant()
//
//    /* lets find index of today */
//    val todayIndex = monthDateSpents.indexOfFirst { it ->
//        it.date == normalizedTodayStartInstant
//    }
//
//    val dayOfWeek = zonedDateTime.dayOfWeek.value // 1 - monday, 7 - sunday
//
//
//    /* ok, how do we get other days */
//
//    /* lets get last day of the week */
//    /* we would take today inde*/
//    val remainingDaysInWeek =
//        7 - dayOfWeek // if today is tuesday, we have 5 remaining days in the week
//    val sundayIndex = todayIndex + remainingDaysInWeek
//    val saturdayIndex = sundayIndex - 1
//    val fridayIndex = saturdayIndex - 1
//    val thursdayIndex = fridayIndex - 1
//    val wednesdayIndex = thursdayIndex - 1
//    val tuesdayIndex = wednesdayIndex - 1
//    val mondayIndex = tuesdayIndex - 1
//
//    val mondaySpentAmount = monthDateSpents.getOrNull(mondayIndex)?.amount ?: 0L
//    val tuesdaySpentAmount = monthDateSpents.getOrNull(tuesdayIndex)?.amount ?: 0L
//    val wednesdaySpentAmount = monthDateSpents.getOrNull(wednesdayIndex)?.amount ?: 0L
//    val thursdaySpentAmount = monthDateSpents.getOrNull(thursdayIndex)?.amount ?: 0L
//    val fridaySpentAmount = monthDateSpents.getOrNull(fridayIndex)?.amount ?: 0L
//    val saturdaySpentAmount = monthDateSpents.getOrNull(saturdayIndex)?.amount ?: 0L
//    val sundaySpentAmount = monthDateSpents.getOrNull(sundayIndex)?.amount ?: 0L
//
//
//    val currentWeekSpentAmount = mondaySpentAmount +
//            tuesdaySpentAmount +
//            wednesdaySpentAmount +
//            thursdaySpentAmount +
//            fridaySpentAmount +
//            saturdaySpentAmount +
//            sundaySpentAmount
//
//
//    /* now we need to get both start and end of period */
//    /* start period is monday date, if it exists in the month. if it does not exist, week start is start of the month */
//    /* end period is sunday date, if it exists in the month. if it does not exist, week end is end of the month*/
//    val weekStartDate = monthDateSpents.getOrNull(mondayIndex)?.date ?: monthDateSpents.first().date
//    val weekEndDate = monthDateSpents.getOrNull(sundayIndex)?.date ?: monthDateSpents.last().date
//
//
//    /* now we need to normalize those into instants */
//    val weekStartDateZonedDateTime = weekStartDate.atZone(ZoneOffset.UTC)
//    val normalizedWeekStartDateInstant = ZonedDateTime.of(
//        weekStartDateZonedDateTime.year,
//        weekStartDateZonedDateTime.monthValue,
//        weekStartDateZonedDateTime.dayOfMonth,
//        0, 0, 0, 0,
//        ZoneOffset.UTC,
//    ).toInstant()
//
//    val weekEndDateZonedDateTime = weekEndDate.atZone(ZoneOffset.UTC)
//    val normalizedWeekEndDateInstant = ZonedDateTime.of(
//        weekEndDateZonedDateTime.year,
//        weekEndDateZonedDateTime.monthValue,
//        weekEndDateZonedDateTime.dayOfMonth,
//        23, 59, 59, 999_999_999,
//        ZoneOffset.UTC,
//    ).toInstant()
//
//    Log.d("Hello", "getCurrentWeekPeriodSpentValue: $dayOfWeek")
//
//
//    return PeriodSpentValue(
//        startDate = normalizedWeekStartDateInstant,
//        endDate = normalizedWeekEndDateInstant,
//        amount = currentWeekSpentAmount,
//    )
//}
