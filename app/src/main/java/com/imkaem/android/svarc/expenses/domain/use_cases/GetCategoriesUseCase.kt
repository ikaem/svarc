package com.imkaem.android.svarc.expenses.domain.use_cases

import com.imkaem.android.svarc.expenses.data.repositories.ExpensesRepository
import com.imkaem.android.svarc.expenses.domain.models.CategoryModel
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {
    suspend operator fun invoke(): List<CategoryModel> {

        val categories = expensesRepository.getCategories()

        return categories

    }
}