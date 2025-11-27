package com.imkaem.android.svarc.expenses.domain.use_cases

import com.imkaem.android.svarc.core.utils.values.CurrentPeriodsAmountValues
import com.imkaem.android.svarc.core.utils.values.PeriodAmountValue
import com.imkaem.android.svarc.core.utils.values.ThisMonthPeriodAmountValues
import com.imkaem.android.svarc.core.utils.values.TodayPeriodAmountValues
import com.imkaem.android.svarc.expenses.data.repositories.ExpensesRepository
import com.imkaem.android.svarc.expenses.domain.models.ExpenseModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verifyAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
//import org.junit.Before
//import org.junit.Test
//import org.junit.Assert.*
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class GetCurrentExpensesReportsUseCaseTest {

    val expenses = createDummyExpensesList()
    val dummyExpensesFlow = flowOf(expenses)

    private lateinit var mockExpensesRepository: ExpensesRepository


    @BeforeEach
    fun setUp() {
        mockExpensesRepository = mockk()
    }

    /* TODO we dont need to use dispatcher and scope it seems - we can only use Run test */
    @Test
    fun givenRegularCall_shouldReturnExpectedFlowOfValues(): Unit = runTest {




        val fromMillisInclusiveSlot = slot<Long>()
        val toMillisExclusiveSlot = slot<Long>()
        every {
            mockExpensesRepository.getExpensesFlow(
                capture(fromMillisInclusiveSlot),
                capture(toMillisExclusiveSlot),
            )
        }.returns(dummyExpensesFlow)

        /* given */

        /* when */
        val useCase = GetCurrentExpensesReportsUseCase(
            mockExpensesRepository
        )
        val flow = useCase()

        /* then */
        val first = flow.first()

        assertEquals(
            currentPeriodsAmountValues,
            first
        )
    }

}

/* idea here is that i get a flow of items like this */

private fun createDummyExpensesList(): List<ExpenseModel> {

    val expenses = mutableListOf<ExpenseModel>()

    for (day in 1..30) {
        if (day % 2 != 0) {
            continue
        }

//        val numberOfExpenses = (1..5).random()
        for (expenseIndex in 1..2) {
            val id = expenses.size.toLong() + 1
            val amount = (100L..1000L).random()
            val currency = "EUR"
//            val dateTime = Instant.now().atZone(ZoneOffset.UTC).withDayOfMonth(day).toInstant()
            val dateTime = ZonedDateTime.of(
                2025,
                11,
                day,
                expenseIndex,
                0,
                0,
                0,
                ZoneOffset.UTC,
            ).toInstant()
            val description = "Expense $expenseIndex on day $day"
            val categoryId = 1L

            val expense = ExpenseModel(
                id = id,
                amount = amount,
                currency = currency,
                dateTime = dateTime,
                description = description,
                categoryId = categoryId,
            )

            expenses.add(expense)
        }
    }

    return expenses
}

val currentPeriodsAmountValues = CurrentPeriodsAmountValues(
    today = TodayPeriodAmountValues(
        spent = PeriodAmountValue(
//                    startDate = Instant.now(),
            startDate = ZonedDateTime.of(
                2025,
                11,
                26,
                0, 0, 0, 0,
                ZoneOffset.UTC,
            ).toInstant(),
            endDate = ZonedDateTime.of(
                2025,
                11,
                26,
                23, 59, 59, 999_999_999,
                ZoneOffset.UTC,
            ).toInstant(),
            amount = 1500L,
        ),
        remainder = PeriodAmountValue(
            startDate = ZonedDateTime.of(
                2025,
                11,
                26,
                0, 0, 0, 0,
                ZoneOffset.UTC,
            ).toInstant(),
            endDate = ZonedDateTime.of(
                2025,
                11,
                26,
                23, 59, 59, 999_999_999,
                ZoneOffset.UTC,
            ).toInstant(),
            amount = 1500L,
        ),
        accumulatedRemainder = PeriodAmountValue(
            startDate = ZonedDateTime.of(
                2025,
                11,
                26,
                0, 0, 0, 0,
                ZoneOffset.UTC,
            ).toInstant(),
            endDate = ZonedDateTime.of(
                2025,
                11,
                26,
                23, 59, 59, 999_999_999,
                ZoneOffset.UTC,
            ).toInstant(),
            amount = 1500L,
        ),
    ),
    thisMonth = ThisMonthPeriodAmountValues(
        spent = PeriodAmountValue(
            startDate = ZonedDateTime.of(
                2025,
                11,
                1,
                0, 0, 0, 0,
                ZoneOffset.UTC,
            ).toInstant(),
            endDate = ZonedDateTime.of(
                2025,
                11,
                30,
                23, 59, 59, 999_999_999,
                ZoneOffset.UTC,
            ).toInstant(),
            amount = 60000L,
        ),
        budget = PeriodAmountValue(
            startDate = ZonedDateTime.of(
                2025,
                11,
                1,
                0, 0, 0, 0,
                ZoneOffset.UTC,
            ).toInstant(),
            endDate = ZonedDateTime.of(
                2025,
                11,
                30,
                23, 59, 59, 999_999_999,
                ZoneOffset.UTC,
            ).toInstant(),
            amount = 100000L,
        ),
        remainder = PeriodAmountValue(
            startDate = ZonedDateTime.of(
                2025,
                11,
                1,
                0, 0, 0, 0,
                ZoneOffset.UTC,
            ).toInstant(),
            endDate = ZonedDateTime.of(
                2025,
                11,
                30,
                23, 59, 59, 999_999_999,
                ZoneOffset.UTC,
            ).toInstant(),
            amount = 40000L,
        ),
    )
)