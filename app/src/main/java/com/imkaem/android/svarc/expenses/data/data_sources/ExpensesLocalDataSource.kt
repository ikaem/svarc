package com.imkaem.android.svarc.expenses.data.data_sources

import com.imkaem.android.svarc.expenses.data.database.ExpensesDao
import com.imkaem.android.svarc.expenses.data.entities.local.ExpenseLocalEntity
import com.imkaem.android.svarc.expenses.utils.values.CreateExpenseValue

/* TODO should make interfaces for this */
class ExpensesLocalDataSource(
    private val expensesDao: ExpensesDao,
) {

    suspend fun addOne(expenseValue: CreateExpenseValue): Long {
        val expenseLocalEntity = ExpenseLocalEntity(
            amount = expenseValue.amount,
            currency = expenseValue.currency,
            dateTimeMillis = expenseValue.dateTimeMillis,
            description = expenseValue.description,
            categoryId = expenseValue.categoryId,
        )

        val id = expensesDao.add(expenseLocalEntity)

        return id

    }
}