package com.imkaem.android.svarc.expenses.domain.use_cases

import com.imkaem.android.svarc.core.utils.converters.PeriodAmountValueConverters
import com.imkaem.android.svarc.core.utils.values.CurrentPeriodsAmountValues
import com.imkaem.android.svarc.core.utils.values.PeriodAmountValue
import com.imkaem.android.svarc.core.utils.values.ThisMonthPeriodAmountValues
import com.imkaem.android.svarc.core.utils.values.TodayPeriodAmountValues
import com.imkaem.android.svarc.expenses.data.repositories.ExpensesRepository
import com.imkaem.android.svarc.expenses.domain.models.ExpenseModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
//import org.junit.Before
//import org.junit.Test
//import org.junit.Assert.*
import java.time.ZoneOffset
import java.time.ZonedDateTime

class GetPeriodExpensesReportsUseCaseTest {

    val year = 2025
    val month = 11

    val expenses = createDummyExpensesList(
        year = year,
        month = month,
    )

    val date = ZonedDateTime.of(
        year,
        month,
        26,
        12,
        0,
        0,
        0,
        ZoneOffset.UTC,
    ).toInstant()
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
        val useCase = GetPeriodExpensesReportsUseCase(
            mockExpensesRepository
        )
        val flow = useCase(
            700L,
            date,

            )

        /* then */
        /* get expected stuff - these methods are already tested */

        val expectedTodayPeriodAmountSpentValue =
            PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountSpentValue(
                expenses,
                date,
            )
        val expectedTodayPeriodAmountRemainderValue =
            PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountRemainderValue(
                expenses,
                date,
                700L,
            )
        val expectedTodayPeriodAmountAccumulatedRemainderValue =
            PeriodAmountValueConverters.fromMonthExpensesToDatePeriodAmountAccumulatedRemainderValue(
                expenses,
                date,
                700L,
            )
        val expectedThisMonthPeriodAmountSpentValue = PeriodAmountValueConverters.fromMonthExpensesToMonthPeriodAmountSpentValue(
            year,
            month,
            expenses,
        )
        val expectedThisMonthPeriodAmountBudgetValue = PeriodAmountValueConverters.fromDailyBudgetToMonthPeriodAmountBudgetValue(
            year,
            month,
            700L,
        )
        val expectedThisMonthPeriodAmountRemainderValue = PeriodAmountValueConverters.fromMonthExpensesToMonthPeriodAmountRemainderValue(
            year,
            month,
            expenses,
            700L,
        )

        val currentPeriodsAmountValues = CurrentPeriodsAmountValues(
            today = TodayPeriodAmountValues(
                spent = expectedTodayPeriodAmountSpentValue,
                remainder = expectedTodayPeriodAmountRemainderValue,
                accumulatedRemainder = expectedTodayPeriodAmountAccumulatedRemainderValue,
            ),
            thisMonth = ThisMonthPeriodAmountValues(
                spent = expectedThisMonthPeriodAmountSpentValue,
                budget = expectedThisMonthPeriodAmountBudgetValue,
                remainder = expectedThisMonthPeriodAmountRemainderValue,
            )
        )

        val first = flow.first()

        assertEquals(
            currentPeriodsAmountValues,
            first
        )
    }

}

/* idea here is that i get a flow of items like this */

private fun createDummyExpensesList(
    year: Int,
    month: Int,
): List<ExpenseModel> {

    val expense1 = ExpenseModel(
        id = 1L,
        amount = 500L,
        currency = "EUR",
        dateTime = ZonedDateTime.of(
            year,
            month,
            1,
            10,
            0,
            0,
            0,
            ZoneOffset.UTC,
        ).toInstant(),
        description = "Grocery shopping",
        categoryId = 1L,
    )

    val expense2 = ExpenseModel(
        id = 2L,
        amount = 1000L,
        currency = "EUR",
        dateTime = ZonedDateTime.of(
            year,
            month,
            15,
            15,
            30,
            0,
            0,
            ZoneOffset.UTC,
        ).toInstant(),
        description = "Electronics purchase",
        categoryId = 2L,
    )

    val expense3 = ExpenseModel(
        id = 3L,
        amount = 2000L,
        currency = "EUR",
        dateTime = ZonedDateTime.of(
            year,
            month,
            20,
            9,
            0,
            0,
            0,
            ZoneOffset.UTC,
        ).toInstant(),
        description = "Monthly rent",
        categoryId = 3L,
    )

    val expense4 = ExpenseModel(
        id = 4L,
        amount = 750L,
        currency = "EUR",
        dateTime = ZonedDateTime.of(
            year,
            month,
            20,
            18,
            45,
            0,
            0,
            ZoneOffset.UTC,
        ).toInstant(),
        description = "Dining out",
        categoryId = 4L,
    )

    val expense5 = ExpenseModel(
        id = 5L,
        amount = 300L,
        currency = "EUR",
        dateTime = ZonedDateTime.of(
            year,
            month,
            25,
            14,
            15,
            0,
            0,
            ZoneOffset.UTC,
        ).toInstant(),
        description = "Transportation",
        categoryId = 5L,
    )

    val expenses = listOf(
        expense1,
        expense2,
        expense3,
        expense4,
        expense5,
    )

    return expenses
}

//val currentPeriodsAmountValues = CurrentPeriodsAmountValues(
//    today = TodayPeriodAmountValues(
//        spent = PeriodAmountValue(
////                    startDate = Instant.now(),
//            startDate = ZonedDateTime.of(
//                2025,
//                11,
//                26,
//                0, 0, 0, 0,
//                ZoneOffset.UTC,
//            ).toInstant(),
//            endDate = ZonedDateTime.of(
//                2025,
//                11,
//                26,
//                23, 59, 59, 999_999_999,
//                ZoneOffset.UTC,
//            ).toInstant(),
//            amount = 1500L,
//        ),
//        remainder = PeriodAmountValue(
//            startDate = ZonedDateTime.of(
//                2025,
//                11,
//                26,
//                0, 0, 0, 0,
//                ZoneOffset.UTC,
//            ).toInstant(),
//            endDate = ZonedDateTime.of(
//                2025,
//                11,
//                26,
//                23, 59, 59, 999_999_999,
//                ZoneOffset.UTC,
//            ).toInstant(),
//            amount = 1500L,
//        ),
//        accumulatedRemainder = PeriodAmountValue(
//            startDate = ZonedDateTime.of(
//                2025,
//                11,
//                26,
//                0, 0, 0, 0,
//                ZoneOffset.UTC,
//            ).toInstant(),
//            endDate = ZonedDateTime.of(
//                2025,
//                11,
//                26,
//                23, 59, 59, 999_999_999,
//                ZoneOffset.UTC,
//            ).toInstant(),
//            amount = 1500L,
//        ),
//    ),
//    thisMonth = ThisMonthPeriodAmountValues(
//        spent = PeriodAmountValue(
//            startDate = ZonedDateTime.of(
//                2025,
//                11,
//                1,
//                0, 0, 0, 0,
//                ZoneOffset.UTC,
//            ).toInstant(),
//            endDate = ZonedDateTime.of(
//                2025,
//                11,
//                30,
//                23, 59, 59, 999_999_999,
//                ZoneOffset.UTC,
//            ).toInstant(),
//            amount = 60000L,
//        ),
//        budget = PeriodAmountValue(
//            startDate = ZonedDateTime.of(
//                2025,
//                11,
//                1,
//                0, 0, 0, 0,
//                ZoneOffset.UTC,
//            ).toInstant(),
//            endDate = ZonedDateTime.of(
//                2025,
//                11,
//                30,
//                23, 59, 59, 999_999_999,
//                ZoneOffset.UTC,
//            ).toInstant(),
//            amount = 100000L,
//        ),
//        remainder = PeriodAmountValue(
//            startDate = ZonedDateTime.of(
//                2025,
//                11,
//                1,
//                0, 0, 0, 0,
//                ZoneOffset.UTC,
//            ).toInstant(),
//            endDate = ZonedDateTime.of(
//                2025,
//                11,
//                30,
//                23, 59, 59, 999_999_999,
//                ZoneOffset.UTC,
//            ).toInstant(),
//            amount = 40000L,
//        ),
//    )
//)