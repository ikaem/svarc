package com.imkaem.android.svarc.expenses.data.repositories

import com.imkaem.android.svarc.expenses.data.data_sources.ExpensesLocalDataSource
import com.imkaem.android.svarc.expenses.domain.models.CategoryModel
import com.imkaem.android.svarc.expenses.domain.models.ExpenseModel
import com.imkaem.android.svarc.expenses.utils.values.CreateExpenseValue
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/* TODO we need interface in domain layer for this */

/* TODO maybe we should not have another level of data source
* but, maybe we should, lets see how it goes
* */
@Singleton
class ExpensesRepository @Inject constructor(
    private val expensesLocalDataSource: ExpensesLocalDataSource,
) {

    suspend fun addExpense(
        expenseValue: CreateExpenseValue
    ): Long {
        val id = expensesLocalDataSource.addExpense(expenseValue)
        return id
    }

    suspend fun getExpensesWithCategories(): List<ExpenseModel> {
        val pojos = expensesLocalDataSource.getExpensesWithCategories()

        val models = pojos.map { pojo ->

            val dateTime = Instant.ofEpochMilli(pojo.expense.dateTimeMillis)

            val model = ExpenseModel(
                id = pojo.expense.id,
                amount = pojo.expense.amount,
                currency = pojo.expense.currency,
                dateTime = dateTime,
                description = pojo.expense.description,
                category = CategoryModel(
                    id = pojo.category.id,
                    name = pojo.category.name,
                )
            )
            model
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