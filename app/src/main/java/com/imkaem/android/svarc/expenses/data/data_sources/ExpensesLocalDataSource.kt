package com.imkaem.android.svarc.expenses.data.data_sources

import android.util.Log
import com.imkaem.android.svarc.expenses.data.database.CategoriesDao
import com.imkaem.android.svarc.expenses.data.database.ExpensesDao
import com.imkaem.android.svarc.expenses.data.entities.local.CategoryLocalEntity
import com.imkaem.android.svarc.expenses.data.entities.local.ExpenseLocalEntity
import com.imkaem.android.svarc.expenses.data.entities.local.ExpenseWithCategoryPojo
import com.imkaem.android.svarc.expenses.utils.values.CreateExpenseValue
import javax.inject.Inject
import javax.inject.Singleton

/* TODO should make interfaces for this */
/* TODO not suee if we should have this level - the data source before the actual lib - but lets try it */
@Singleton
class ExpensesLocalDataSource @Inject constructor(
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

