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
    inner class FromMonthExpensesToDatePeriodAmountRemainderValue {
        /* date should return correct period start and end dates */
        @Test
        fun givenDate_shouldReturnCorrectPeriodStartAndEndDates() {
            /* given */
            val date = ZonedDateTime.of(
                2024,
                5,
                20,
                14, 30, 0, 0,
                ZoneOffset.UTC
            ).toInstant()

            /* when */
            val remainderValue =
                PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountRemainderValue(
                    expenses = emptyList(),
                    date = date,
                    dailyBudget = 500L,
                )

            /* then */
            val expectedStartDate = ZonedDateTime.of(
                2024,
                5,
                20,
                0, 0, 0, 0,
                ZoneOffset.UTC
            ).toInstant()
            val expectedEndDate = ZonedDateTime.of(
                2024,
                5,
                20,
                23, 59, 59, 999_999_999,
                ZoneOffset.UTC
            ).toInstant()

            assertEquals(expectedStartDate, remainderValue.startDate)
            assertEquals(expectedEndDate, remainderValue.endDate)
        }

        /* empty list should return full budget */
        @Test
        fun givenEmptyList_shouldReturnFullBudgetAmount() {
            /* given */
            val expenses = emptyList<ExpenseModel>()

            /* when */
            val periodAmountValue =
                PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountRemainderValue(
                    expenses = expenses,
                    date = Instant.now(),
                    dailyBudget = 500L,
                )

            /* then */
            assertEquals(500L, periodAmountValue.amount)
        }

        /* list without date should return WHAT?! probably full budget */
        @Test
        fun givenListWithoutProvidedDateExpenses_shouldReturnFullBudgetAmount() {
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
            val value =
                PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountRemainderValue(
                    expenses = expenses,
                    date = providedDate,
                    dailyBudget = 500L,
                )

            /* then */
            assertEquals(500L, value.amount)


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
                ),
                /* here we have two expenses on the provided date */
                ExpenseModel(
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
            val value =
                PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountRemainderValue(
                    expenses = expenses,
                    date = providedDate,
                    dailyBudget = 600L,
                )

            /* then */
            assertEquals(200, value.amount)
        }

    }

    @Nested
    inner class FromMonthExpensesToDatePeriodAmountSpentValue {

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
                PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountSpentValue(
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
                PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountSpentValue(
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
            val value = PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountSpentValue(
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
                ),
                /* here we have two expenses on the provided date */
                ExpenseModel(
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
            val value =
                PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountSpentValue(
                    expenses = expenses,
                    date = providedDate,
                )

            /* then */
            assertEquals(400, value.amount)
        }
    }

}