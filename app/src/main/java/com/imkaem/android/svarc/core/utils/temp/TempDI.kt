package com.imkaem.android.svarc.core.utils.temp

import com.imkaem.android.svarc.core.data.database.SvarcDatabaseInstance
import com.imkaem.android.svarc.expenses.data.data_sources.ExpensesLocalDataSource
import com.imkaem.android.svarc.expenses.data.database.ExpensesDao
import com.imkaem.android.svarc.expenses.data.repositories.ExpensesRepository
import com.imkaem.android.svarc.expenses.domain.use_cases.CreateExpenseUseCase
import com.imkaem.android.svarc.expenses.domain.use_cases.GetCategoriesUseCase
import com.imkaem.android.svarc.expenses.domain.use_cases.GetExpensesUseCase

private val DATABASE_INSTANCE = SvarcDatabaseInstance
private val EXPENSES_DAO = DATABASE_INSTANCE.expensesDao()
private val CATEGORIES_DAO = DATABASE_INSTANCE.categoriesDao()


private val EXPENSES_LOCAL_DATA_SOURCE by lazy {
    ExpensesLocalDataSource(
        expensesDao = EXPENSES_DAO,
        categoriesDao = CATEGORIES_DAO
    )
}

private val EXPENSES_REPOSITORY by lazy {
    ExpensesRepository(
        expensesLocalDataSource = EXPENSES_LOCAL_DATA_SOURCE
    )
}


object TempDI {

    val createExpenseUseCase = CreateExpenseUseCase(
        expensesRepository = EXPENSES_REPOSITORY
    )

    val getCategoriesUseCase = GetCategoriesUseCase(
        expensesRepository = EXPENSES_REPOSITORY
    )

    val getExpensesUseCase = GetExpensesUseCase(
        expensesRepository = EXPENSES_REPOSITORY
    )
}