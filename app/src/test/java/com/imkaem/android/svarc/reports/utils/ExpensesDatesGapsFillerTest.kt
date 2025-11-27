package com.imkaem.android.svarc.reports.utils

import com.imkaem.android.svarc.expenses.domain.models.ExpenseModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

//import junit.framework.TestCase.assertEquals
//import org.junit.Test


class ExpensesDatesGapsFillerTest {

    @Test
    fun testGapsNoExist_noAction() {
        /* given */
        val completeMap = mutableMapOf<Int, List<ExpenseModel>>()
        for (day in 1..31) {
            completeMap[day] = listOf(
                ExpenseModel(
                    id = day.toLong(),
                    amount = 100,
                    currency = "USD",
                    dateTime = java.time.Instant.now(),
                    description = "Test expense",
                    categoryId = 1,
                )
            )
        }

        /* when */
        val filledMap = ExpensesDatesGapsFiller.fillMonthExpensesMapGaps(
            expenses = completeMap,
            month = 1,
            year = 2024,
        )

        /* then */
        assertEquals(completeMap, filledMap)
    }

    @Test
    fun testGapsExist_gapsFilled() {
        /* given */
        val mapWithGaps = mapOf<Int, List<ExpenseModel>>(
            1 to emptyList(),
            3 to listOf(
                ExpenseModel(
                    id = 1,
                    amount = 100,
                    currency = "USD",
                    dateTime = java.time.Instant.now(),
                    description = "Test expense",
                    categoryId = 1,
                )
            ),
            4 to emptyList(),
            7 to emptyList(),
        )

        /* when */
        val filledMap = ExpensesDatesGapsFiller.fillMonthExpensesMapGaps(
            expenses = mapWithGaps,
            month = 1,
            year = 2024,
        )

        /* then */
        val expectedMap = mutableMapOf<Int, List<ExpenseModel>>()
        for (day in 1..31) {
            val existing = mapWithGaps[day]
            if (existing == null) {
                expectedMap[day] = emptyList()
            } else {
                expectedMap[day] = existing
            }
        }

        assertEquals(expectedMap, filledMap)
    }

}