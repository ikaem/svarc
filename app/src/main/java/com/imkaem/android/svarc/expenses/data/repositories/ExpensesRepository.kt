package com.imkaem.android.svarc.expenses.data.repositories

import com.imkaem.android.svarc.expenses.data.data_sources.ExpensesLocalDataSource
import com.imkaem.android.svarc.expenses.utils.values.CreateExpenseValue

/* TODO we need interface in domain layer for this */
class ExpensesRepository(
    private val expensesLocalDataSource: ExpensesLocalDataSource,
) {

    suspend fun addExpense(
        expenseValue: CreateExpenseValue
    ): Long {
        val id = expensesLocalDataSource.addOne(expenseValue)

        return id
    }
}