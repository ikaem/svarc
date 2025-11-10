package com.imkaem.android.svarc.dev.domain.use_cases

import com.imkaem.android.svarc.expenses.data.repositories.ExpensesRepository
import com.imkaem.android.svarc.expenses.utils.values.CreateExpenseValue
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.ZonedDateTime
import javax.inject.Inject

class AddDummyExpensesUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {

    suspend operator fun invoke(): List<Long> {
        val expenseValues = generateExpenses()

        val ids = expensesRepository.addAllExpenses(expenseValues)
        return ids
    }
    private fun generateExpenses(): List<CreateExpenseValue> {
        val expensesToAdd = mutableListOf<CreateExpenseValue>()

        val nowInstant = Instant.now()
        val zonedDateTime = nowInstant.atZone(ZoneOffset.UTC)
        val year = zonedDateTime.year
        val month = zonedDateTime.monthValue

        val daysInMonth = YearMonth.of(year, month).lengthOfMonth()

        for (day in 1..daysInMonth) {
            for (expenseIndex in 1..2) {
                val dateInstant = Instant.from(
                    ZonedDateTime.of(
                        year,
                        month,
                        day,
                        expenseIndex, 0, 0, 0,
                        ZoneOffset.UTC,
                    )
                )

                val randomAmount = (500..5000).random().toLong()
                val createExpenseValue = CreateExpenseValue(
                    amount = randomAmount,
                    currency = "EUR",
                    description = "description: $day - $expenseIndex",
                    categoryId = 1,
                    dateTimeMillis = dateInstant.toEpochMilli(),
                )

                expensesToAdd.add(createExpenseValue)
            }
        }

        return expensesToAdd

    }
}

