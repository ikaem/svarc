package com.imkaem.android.svarc.expenses.domain.use_cases

import com.imkaem.android.svarc.expenses.data.repositories.ExpensesRepository
import com.imkaem.android.svarc.expenses.domain.models.ExpenseWithCategoryModel
import javax.inject.Inject

class GetExpensesWithCategoriesUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {

    suspend operator fun invoke(): List<ExpenseWithCategoryModel> {

        val expenses = expensesRepository.getExpensesWithCategories()

        return expenses
    }


}