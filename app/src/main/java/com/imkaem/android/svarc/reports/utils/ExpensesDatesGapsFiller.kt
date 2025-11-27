package com.imkaem.android.svarc.reports.utils

import com.imkaem.android.svarc.expenses.domain.models.ExpenseModel
import java.time.YearMonth
import java.time.ZoneOffset


/* TODO maybe we dont need this actually */
class ExpensesDatesGapsFiller {

    companion object {
        fun fillMonthExpensesMapGaps(
            expenses: Map<Int, List<ExpenseModel>>,
            year: Int,
            month: Int,
        ): Map<Int, List<ExpenseModel>> {
            /* first lets define all days in the month */

            /* then we will create another map with all dates
            * if provided map has current iteration day, we add all expenses models to the list
            * if not, we add an empty list
            * */

            val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
            val filledMap = mutableMapOf<Int, List<ExpenseModel>>()

            for (day in 1..daysInMonth) {
                val existing = expenses[day]

                if (existing == null) {
                    filledMap[day] = emptyList()
                } else {
                    filledMap[day] = existing
                }
            }


            return filledMap

        }


        /* this only makes sense to do for expenses that are part of a month */
//        fun fillMonthExpenseModelsGaps(
//            expenses: List<ExpenseModel>,
//            month: Int,
//            year: Int,
//        ) {
//
//
//            val map = expenses.groupBy { it ->
//                val zonedDateTime = it.dateTime.atZone(ZoneOffset.UTC)
//                val day = zonedDateTime.dayOfMonth
//
//                day
//            }
//
//            val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
//            /* now we create a map that loops through each day of the month, and adds it under list, or  */
////            val fullMonthExpenses = (1..daysInMonth).map { it ->
////                val existing = map[it]
////                if(e)
////
////
////            }
//
//            val fullMonthExpenses = mutableListOf<ExpenseModel>()
//
//            /* we should actually loop throug all days, and if we find day in the map, we should add all to prepared list. if not, we should add just one of zero */
//            for(day in 1..daysInMonth) {
//
//                /* TODO we cannot do this because we dont have id for models */
//                val existing = map[day]
//                if(existing != null) {
//                    fullMonthExpenses.addAll(existing)
//                    continue
//                }
//
//                /* at this point, we have nothing. so lets maybe construct it? */
//            }
//
//        }
    }
}