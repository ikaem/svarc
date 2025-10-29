package com.imkaem.android.svarc.expenses.data.data_sources

import android.util.Log
import com.imkaem.android.svarc.expenses.data.database.CategoriesDao
import com.imkaem.android.svarc.expenses.data.database.ExpensesDao
import com.imkaem.android.svarc.expenses.data.entities.local.CategoryLocalEntity
import com.imkaem.android.svarc.expenses.data.entities.local.ExpenseLocalEntity
import com.imkaem.android.svarc.expenses.data.entities.local.ExpenseWithCategoryPojo
import com.imkaem.android.svarc.expenses.utils.values.CreateExpenseValue

/* TODO should make interfaces for this */
class ExpensesLocalDataSource(
    private val expensesDao: ExpensesDao,
    private val categoriesDao: CategoriesDao,
) {

    /* expenses */
    suspend fun addExpense(expenseValue: CreateExpenseValue): Long {
        val expenseLocalEntity = ExpenseLocalEntity(
            amount = expenseValue.amount,
            currency = expenseValue.currency,
            dateTimeMillis = expenseValue.dateTimeMillis,
            description = expenseValue.description,
            categoryId = expenseValue.categoryId,
        )

        val id = expensesDao.add(expenseLocalEntity)

        Log.d("ExpensesLocalDataSource", "Added expense with id: $id")

        return id
    }

    suspend fun getExpensesWithCategories(): List<ExpenseWithCategoryPojo> {
        val expensesWithCategories = expensesDao.getAllWithCategories()
        return expensesWithCategories
    }

    /* categories */
    suspend fun getCategories(): List<CategoryLocalEntity> {
        val categories = categoriesDao.getAll()
        return categories
    }
}

