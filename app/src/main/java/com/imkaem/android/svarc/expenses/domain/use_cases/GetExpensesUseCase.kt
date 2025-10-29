package com.imkaem.android.svarc.expenses.domain.use_cases

import com.imkaem.android.svarc.expenses.data.repositories.ExpensesRepository
import com.imkaem.android.svarc.expenses.domain.models.ExpenseModel

class GetExpensesUseCase(
    private val expensesRepository: ExpensesRepository
) {

    suspend operator fun invoke(): List<ExpenseModel> {

        val expenses = expensesRepository.getExpenses()

        return expenses
    }


}