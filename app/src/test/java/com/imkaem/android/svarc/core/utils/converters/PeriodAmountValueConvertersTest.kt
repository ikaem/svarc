package com.imkaem.android.svarc.core.utils.converters

import com.imkaem.android.svarc.expenses.domain.models.ExpenseModel
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

class PeriodAmountValueConvertersTest {

    @Nested
    inner class FromMonthExpenseModelsToDatePeriodAmountValue {

        @Test
        fun givenDate_shouldReturnCorrectPeriodStartAndEndDates() {
            /* given */
            val date = ZonedDateTime.of(
                2024,
                3,
                15,
                10, 11, 12, 0,
                ZoneOffset.UTC
            ).toInstant()

            /* when */
            val periodAmountValue =
                PeriodAmountValueConverters.fromMonthExpenseModelsToDatePeriodAmountValue(
                    expenses = emptyList(),
                    date = date,
                )

            /* then */
            val expectedStartDate = ZonedDateTime.of(
                2024,
                3,
                15,
                0, 0, 0, 0,
                ZoneOffset.UTC
            ).toInstant()
            val expectedEndDate = ZonedDateTime.of(
                2024,
                3,
                15,
                23, 59, 59, 999_999_999,
                ZoneOffset.UTC
            ).toInstant()

            val actualStartDate = periodAmountValue.startDate
            val actualEndDate = periodAmountValue.endDate

            assertEquals(
                expectedStartDate,
                actualStartDate,
            )
            assertEquals(
                expectedEndDate,
                actualEndDate,
            )
        }

        @Test
        fun givenEmptyList_shouldReturnZeroAmount() {
            /* given */
            val expenses = emptyList<ExpenseModel>()

            /* when */
            val periodAmountValue =
                PeriodAmountValueConverters.fromMonthExpenseModelsToDatePeriodAmountValue(
                    expenses = expenses,
                    date = Instant.now()
                )

            /* then */
            assertEquals(periodAmountValue.amount, 0L)
        }

        @Test
        fun givenListWithoutProvidedDateExpenses_shouldReturnZeroAmount() {
            /* given */
            val providedDate = ZonedDateTime.of(
                2024,
                3,
                15, // This date is not in the expenses list
                10, 11, 12, 0,
                ZoneOffset.UTC
            ).toInstant()

            val dateOne = ZonedDateTime.of(
                2024,
                3,
                10,
                10, 11, 12, 0,
                ZoneOffset.UTC
            ).toInstant()
            val dateTwo = ZonedDateTime.of(
                2024,
                3,
                20,
                10, 11, 12, 0,
                ZoneOffset.UTC
            ).toInstant()

            val expenses = listOf<ExpenseModel>(
                ExpenseModel(
                    id = 1,
                    amount = 100,
                    currency = "USD",
                    dateTime = dateOne,
                    description = "Expense on date one",
                    categoryId = 1,
                ),
                ExpenseModel(
                    id = 2,
                    amount = 200,
                    currency = "USD",
                    dateTime = dateTwo,
                    description = "Expense on date two",
                    categoryId = 2,
                )
            )

            /* when */
            val value = PeriodAmountValueConverters.fromMonthExpenseModelsToDatePeriodAmountValue(
                expenses = expenses,
                date = providedDate,
            )

            /* then */
            assertEquals(0L, value.amount)
        }

        @Test
        fun givenListWithProvidedDateExpenses_shouldReturnCorrectAmount() {
            /* given */
            val providedDate = ZonedDateTime.of(
                2024,
                3,
                15, // This date is not in the expenses list
                10, 11, 12, 0,
                ZoneOffset.UTC
            ).toInstant()

            val dateOne = ZonedDateTime.of(
                2024,
                3,
                10,
                10, 11, 12, 0,
                ZoneOffset.UTC
            ).toInstant()
            val dateTwo = ZonedDateTime.of(
                2024,
                3,
                20,
                10, 11, 12, 0,
                ZoneOffset.UTC
            ).toInstant()

            val expenses = listOf<ExpenseModel>(
                ExpenseModel(
                    id = 1,
                    amount = 100,
                    currency = "USD",
                    dateTime = dateOne,
                    description = "Expense on date one",
                    categoryId = 1,
                ),
                ExpenseModel(
                    id = 2,
                    amount = 200,
                    currency = "USD",
                    dateTime = dateTwo,
                    description = "Expense on date two",
                    categoryId = 2,
                )
                /* here we have two expenses on the provided date */, ExpenseModel(
                    id = 3,
                    amount = 150,
                    currency = "USD",
                    dateTime = providedDate.plusMillis(100),
                    description = "Expense on provided date - 1",
                    categoryId = 3,
                ),
                ExpenseModel(
                    id = 4,
                    amount = 250,
                    currency = "USD",
                    dateTime = providedDate.plusMillis(200),
                    description = "Expense on provided date - 2",
                    categoryId = 4,
                )
            )

            /* when */
            val value = PeriodAmountValueConverters.fromMonthExpenseModelsToDatePeriodAmountValue(
                expenses = expenses,
                date = providedDate,
            )

            /* then */
            assertEquals(350L, value.amount)
        }
    }

}