package com.imkaem.android.svarc.core.utils.converters

import com.imkaem.android.svarc.core.utils.values.PeriodAmountValue
import com.imkaem.android.svarc.expenses.domain.models.ExpenseModel
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.Month
import java.time.ZoneOffset
import java.time.ZonedDateTime

class PeriodAmountValueConvertersTest {


    @Nested
    inner class FromMonthExpensesToDatePeriodAmountAccumulatedRemainderValue {

        @Test
        fun givenDate_shouldReturnCorrectPeriodStateAndEndDates() {

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
                PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountAccumulatedRemainderValue(
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

        @Test
        fun givenEmptyList_shouldReturnCorrectCorrectValue() {

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
                PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountAccumulatedRemainderValue(
                    expenses = emptyList(),
                    date = date,
                    dailyBudget = 500L,
                )

            /* then */
            val expectedValue = 500L * 20 // 10000

            assertEquals(expectedValue, remainderValue.amount)
        }

        @Test
        fun givenExpensesListWithMissingProvidedDate_shouldReturnCorrectValue() {

            /* given */
            val expenses = listOf<ExpenseModel>(
                ExpenseModel(
                    id = 1,
                    amount = 100,
                    currency = "USD",
                    dateTime = ZonedDateTime.of(
                        2024,
                        5,
                        10,
                        10, 0, 0, 0,
                        ZoneOffset.UTC
                    ).toInstant(),
                    description = "Expense on May 10",
                    categoryId = 1,
                ),
                ExpenseModel(
                    id = 2,
                    amount = 200,
                    currency = "USD",
                    dateTime = ZonedDateTime.of(
                        2024,
                        5,
                        15,
                        10, 0, 0, 0,
                        ZoneOffset.UTC
                    ).toInstant(),
                    description = "Expense on May 15",
                    categoryId = 2,
                )
            )

            val date = ZonedDateTime.of(
                2024,
                5,
                23,
                14, 30, 0, 0,
                ZoneOffset.UTC
            ).toInstant()


            /* when */
            val remainderValue =
                PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountAccumulatedRemainderValue(
                    expenses = expenses,
                    date = date,
                    dailyBudget = 500L,
                )


            /* then */
            val expectedValue = (500L * 23) - (100 + 200) // 11500

            assertEquals(expectedValue, remainderValue.amount)
        }

        /* test dates outside of the date month range */

        /* test when dates have multiple expenses */

        /* test spending over budget */
    }

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

    @Nested
    inner class FromMonthExpensesToMonthPeriodAmountSpentValue {

        /* should return correct start and end date */
        @Test
        fun givenMonthAndYear_shouldReturnCorrectPeriodStartAndAndDates() {
            /* given */
            val year = 2024
            val month = 5


            /* when */
            val value = PeriodAmountValueConverters.fromMonthExpensesToMonthPeriodAmountSpentValue(
                expenses = emptyList(),
                year = year,
                month = month,
            )

            /* then */
            val expectedStartDate = ZonedDateTime.of(
                year,
                month,
                1,
                0, 0, 0, 0,
                ZoneOffset.UTC
            ).toInstant()
            val expectedEndDate = ZonedDateTime.of(
                year,
                month,
                31,
                23, 59, 59, 999_999_999,
                ZoneOffset.UTC
            ).toInstant()

            val actualStartDate = value.startDate
            val actualEndDate = value.endDate

            assertEquals(
                expectedStartDate,
                actualStartDate
            )
            assertEquals(
                expectedEndDate,
                actualEndDate
            )
        }

        /* empty list should return 0*/
        @Test
        fun givenEmptyList_shouldReturnZeroSpentValue() {
            /* given */
            val expenses = emptyList<ExpenseModel>()

            /* when */
            val value = PeriodAmountValueConverters.fromMonthExpensesToMonthPeriodAmountSpentValue(
                expenses = expenses,
                year = 2024,
                month = 5,
            )

            /* then */
            assertEquals(0L, value.amount)
        }


        /* list with overflowing expenses should return correct spent value */
        @Test
        fun givenOverflowingExpensesList_shouldReturnCorrectSpentValue() {

            /* given */
            val beforeMonthExpense = ExpenseModel(
                id = 1,
                amount = 1000,
                currency = "USD",
                dateTime = ZonedDateTime.of(
                    2024,
                    4,
                    30,
                    10, 0, 0, 0,
                    ZoneOffset.UTC
                ).toInstant(),
                description = "Expense before month",
                categoryId = 1,
            )
            val afterMonthExpense = ExpenseModel(
                id = 2,
                amount = 2000,
                currency = "USD",
                dateTime = ZonedDateTime.of(
                    2024,
                    6,
                    1,
                    10, 0, 0, 0,
                    ZoneOffset.UTC
                ).toInstant(),
                description = "Expense after month",
                categoryId = 2,
            )

            val expenses = listOf(
                beforeMonthExpense,
                afterMonthExpense
            )

            /* when */
            val value = PeriodAmountValueConverters.fromMonthExpensesToMonthPeriodAmountSpentValue(
                expenses = expenses,
                year = 2024,
                month = 5,
            )

            /* then */
            assertEquals(0L, value.amount)
        }

        /* list with expenses should return correct value */
        @Test
        fun givenExpensesList_shouldReturnCorrectSpentValue() {
            /* given */
            val expenseOne = ExpenseModel(
                id = 1,
                amount = 1500,
                currency = "USD",
                dateTime = ZonedDateTime.of(
                    2024,
                    5,
                    10,
                    10, 0, 0, 0,
                    ZoneOffset.UTC
                ).toInstant(),
                description = "Expense one",
                categoryId = 1,
            )

            val expenseTwo = ExpenseModel(
                id = 2,
                amount = 2500,
                currency = "USD",
                dateTime = ZonedDateTime.of(
                    2024,
                    5,
                    20,
                    10, 0, 0, 0,
                    ZoneOffset.UTC
                ).toInstant(),
                description = "Expense two",
                categoryId = 2,
            )

            val expenseThree = ExpenseModel(
                id = 3,
                amount = 3000,
                currency = "USD",
                dateTime = ZonedDateTime.of(
                    2024,
                    5,
                    25,
                    10, 0, 0, 0,
                    ZoneOffset.UTC
                ).toInstant(),
                description = "Expense three",
                categoryId = 3,
            )

            /* when */
            val value = PeriodAmountValueConverters.fromMonthExpensesToMonthPeriodAmountSpentValue(
                expenses = listOf(
                    expenseOne,
                    expenseTwo,
                    expenseThree,
                ),
                year = 2024,
                month = 5,
            )

            /* then */
            val expectedAmount = 1500 + 2500 + 3000 // 7000L
            assertEquals(expectedAmount.toLong(), value.amount)
        }
    }

    @Nested
    inner class FromDailyBudgetToMonthPeriodAmountBudgetValue {

        @Test
        fun givenDailyBudget_shouldReturnExpectedResult() {
            /* given */
            val dailyBudget = 500L

            /* when */
            val value = PeriodAmountValueConverters.fromDailyBudgetToMonthPeriodAmountBudgetValue(
                dailyBudget = dailyBudget,
                year = 2024,
                month = 5, // May has 31 days
            )

            /* then */
            val startDate = ZonedDateTime.of(
                2024,
                5,
                1,
                0, 0, 0, 0,
                ZoneOffset.UTC
            ).toInstant()
            val endDate = ZonedDateTime.of(
                2024,
                5,
                31,
                23, 59, 59, 999_999_999,
                ZoneOffset.UTC
            ).toInstant()
            val expectedAmount = dailyBudget * 31 // 15500L

            val expectedPeriodAmountValue = PeriodAmountValue(
                startDate = startDate,
                endDate = endDate,
                amount = expectedAmount,
            )

            assertEquals(expectedPeriodAmountValue, value)
        }
    }

    @Nested
    inner class FromMonthExpensesToMonthPeriodAmountRemainderValue {
        /* return correct dates */
        @Test
        fun givenMonthAndYear_shouldReturnCorrectPeriodStartAndEndDates() {
            /* given */
            val year = 2024
            val month = 5

            /* when */
            val value =
                PeriodAmountValueConverters.fromMonthExpensesToMonthPeriodAmountRemainderValue(
                    expenses = emptyList(),
                    dailyBudget = 500L,
                    year = year,
                    month = month,
                )

            /* then */
            val expectedStartDate = ZonedDateTime.of(
                year,
                month,
                1,
                0, 0, 0, 0,
                ZoneOffset.UTC
            ).toInstant()
            val expectedEndDate = ZonedDateTime.of(
                year,
                month,
                31,
                23, 59, 59, 999_999_999,
                ZoneOffset.UTC
            ).toInstant()

            val actualStartDate = value.startDate
            val actualEndDate = value.endDate

            assertEquals(
                expectedStartDate,
                actualStartDate
            )
            assertEquals(
                expectedEndDate,
                actualEndDate
            )
        }

        /* if empty list, return expected */
        @Test
        fun givenEmptyExpensesList_shouldReturnExpectedRemainderValue() {
            /* given */
            val expenses = emptyList<ExpenseModel>()

            /* when */
            val value =
                PeriodAmountValueConverters.fromMonthExpensesToMonthPeriodAmountRemainderValue(
                    expenses = expenses,
                    dailyBudget = 500L,
                    year = 2024,
                    month = 5,
                )

            /* then */
            val expectedAmount = 500L * 31 // 15500L
            assertEquals(expectedAmount, value.amount)
        }

        /* if overflowing expenses, return expected */
        @Test
        fun givenOverflowingExpensesList_shouldReturnExpectedRemainderValue() {
            /* given */
            val beforeMonthExpense = ExpenseModel(
                id = 1,
                amount = 1000,
                currency = "USD",
                dateTime = ZonedDateTime.of(
                    2024,
                    4,
                    30,
                    10, 0, 0, 0,
                    ZoneOffset.UTC
                ).toInstant(),
                description = "Expense before month",
                categoryId = 1,
            )
            val afterMonthExpense = ExpenseModel(
                id = 2,
                amount = 2000,
                currency = "USD",
                dateTime = ZonedDateTime.of(
                    2024,
                    6,
                    1,
                    10, 0, 0, 0,
                    ZoneOffset.UTC
                ).toInstant(),
                description = "Expense after month",
                categoryId = 2,
            )

            val expenses = listOf(
                beforeMonthExpense,
                afterMonthExpense
            )

            /* when */
            val value =
                PeriodAmountValueConverters.fromMonthExpensesToMonthPeriodAmountRemainderValue(
                    expenses = expenses,
                    dailyBudget = 500L,
                    year = 2024,
                    month = 5,
                )

            /* then */
            val expectedAmount = 500L * 31 // 15500L
            assertEquals(expectedAmount, value.amount)
        }

        /* if expenses, return expected */
        @Test
        fun givenExpensesList_shouldReturnExpectedRemainderValue() {
            /* given */
            val expenseOne = ExpenseModel(
                id = 1,
                amount = 1500,
                currency = "USD",
                dateTime = ZonedDateTime.of(
                    2024,
                    5,
                    10,
                    10, 0, 0, 0,
                    ZoneOffset.UTC
                ).toInstant(),
                description = "Expense one",
                categoryId = 1,
            )

            val expenseTwo = ExpenseModel(
                id = 2,
                amount = 2500,
                currency = "USD",
                dateTime = ZonedDateTime.of(
                    2024,
                    5,
                    20,
                    10, 0, 0, 0,
                    ZoneOffset.UTC
                ).toInstant(),
                description = "Expense two",
                categoryId = 2,
            )

            val expenseThree = ExpenseModel(
                id = 3,
                amount = 3000,
                currency = "USD",
                dateTime = ZonedDateTime.of(
                    2024,
                    5,
                    25,
                    10, 0, 0, 0,
                    ZoneOffset.UTC
                ).toInstant(),
                description = "Expense three",
                categoryId = 3,
            )

            val expenses = listOf(
                expenseOne,
                expenseTwo,
                expenseThree,
            )

            /* when */
            val value =
                PeriodAmountValueConverters.fromMonthExpensesToMonthPeriodAmountRemainderValue(
                    expenses = expenses,
                    dailyBudget = 500L,
                    year = 2024,
                    month = 5,
                )

            /* then */
            val expectedAmount =
                (500L * 31) - (1500 + 2500 + 3000) // 15500 - 7000 = 8500L
            assertEquals(expectedAmount, value.amount)
        }
    }
}

