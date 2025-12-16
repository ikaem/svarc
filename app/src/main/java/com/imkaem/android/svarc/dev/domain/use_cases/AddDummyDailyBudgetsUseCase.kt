package com.imkaem.android.svarc.dev.domain.use_cases

import com.imkaem.android.svarc.expenses.data.repositories.ExpensesRepository
import com.imkaem.android.svarc.expenses.utils.values.CreateDailyBudgetValue
import java.time.Instant
import java.time.ZoneOffset
import javax.inject.Inject

class AddDummyDailyBudgetsUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository,
) {

    suspend operator fun invoke(): List<Long> {
        val ids = mutableListOf<Long>()

        val budgetsValues = generateDailyBudgets()
        for (budget in budgetsValues) {
            val id = expensesRepository.addDailyBudget(budget)
            ids.add(id)
        }

        return ids
    }

    private fun generateDailyBudgets(): List<CreateDailyBudgetValue> {
        val budgetsToAdd = mutableListOf<CreateDailyBudgetValue>()

        val nowInstant = Instant.now()
        val zonedDateTime = nowInstant.atZone(ZoneOffset.UTC)
        val year = zonedDateTime.year

        val monthsInYear = 12

        for (month in 1..monthsInYear) {
            val randomAmount = (1000..5000).random().toLong()
            val createDailyBudgetValue = CreateDailyBudgetValue(
                amount = randomAmount,
                year = year,
                month = month,
            )
        }

        return budgetsToAdd
    }
}