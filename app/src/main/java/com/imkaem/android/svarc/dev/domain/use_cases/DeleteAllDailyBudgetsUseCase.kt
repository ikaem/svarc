package com.imkaem.android.svarc.dev.domain.use_cases

import com.imkaem.android.svarc.expenses.data.repositories.ExpensesRepository
import javax.inject.Inject

class DeleteAllDailyBudgetsUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {
    suspend operator fun invoke() {
        expensesRepository.deleteAllDailyBudgets()
    }
}