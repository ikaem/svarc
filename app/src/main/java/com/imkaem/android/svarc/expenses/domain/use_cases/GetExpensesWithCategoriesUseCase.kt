package com.imkaem.android.svarc.expenses.domain.use_cases

import com.imkaem.android.svarc.expenses.data.repositories.ExpensesRepository
import com.imkaem.android.svarc.expenses.domain.models.ExpenseModel

class GetExpensesWithCategoriesUseCase(
    private val expensesRepository: ExpensesRepository
) {

    suspend operator fun invoke(): List<ExpenseModel> {

        val expenses = expensesRepository.getExpensesWithCategories()

        return expenses
    }


}