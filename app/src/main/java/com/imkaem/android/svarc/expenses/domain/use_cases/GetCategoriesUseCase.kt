package com.imkaem.android.svarc.expenses.domain.use_cases

import com.imkaem.android.svarc.expenses.data.repositories.ExpensesRepository
import com.imkaem.android.svarc.expenses.domain.models.CategoryModel

class GetCategoriesUseCase(
    private val expensesRepository: ExpensesRepository
) {
    suspend operator fun invoke(): List<CategoryModel> {

        val categories = expensesRepository.getCategories()

        return categories

    }
}