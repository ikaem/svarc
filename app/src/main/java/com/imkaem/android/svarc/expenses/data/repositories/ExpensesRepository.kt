package com.imkaem.android.svarc.expenses.data.repositories

import com.imkaem.android.svarc.expenses.data.data_sources.ExpensesLocalDataSource
import com.imkaem.android.svarc.expenses.domain.models.CategoryModel
import com.imkaem.android.svarc.expenses.domain.models.ExpenseModel
import com.imkaem.android.svarc.expenses.utils.values.CreateExpenseValue
import java.time.Instant

/* TODO we need interface in domain layer for this */
class ExpensesRepository(
    private val expensesLocalDataSource: ExpensesLocalDataSource,
) {

    suspend fun addExpense(
        expenseValue: CreateExpenseValue
    ): Long {
        val id = expensesLocalDataSource.addExpense(expenseValue)
        return id
    }

    suspend fun getExpenses(): List<ExpenseModel> {
        val entities = expensesLocalDataSource.getExpenses()
        val models = entities.map { entity ->
            val instant = Instant.ofEpochMilli(entity.dateTimeMillis)


            ExpenseModel(
                id = entity.id,
                amount = entity.amount,
                currency = entity.currency,
                description = entity.description,
                dateTime = instant,
                category = CategoryModel(
                    id = 12,
                    name = "entity.category.name",
                )

            )


        }

        return models
    }

    suspend fun getCategories(): List<CategoryModel> {
        val entities = expensesLocalDataSource.getCategories()

        val models = entities.map { entity ->

            CategoryModel(
                id = entity.id,
                name = entity.name,
            )
        }

        return models
    }
}