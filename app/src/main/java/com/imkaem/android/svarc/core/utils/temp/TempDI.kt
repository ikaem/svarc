package com.imkaem.android.svarc.core.utils.temp

import com.imkaem.android.svarc.core.data.database.SvarcDatabaseInstance
import com.imkaem.android.svarc.expenses.data.data_sources.ExpensesLocalDataSource
import com.imkaem.android.svarc.expenses.data.database.ExpensesDao
import com.imkaem.android.svarc.expenses.data.repositories.ExpensesRepository
import com.imkaem.android.svarc.expenses.domain.use_cases.CreateExpenseUseCase


val EXPENSES_DAO: ExpensesDao = SvarcDatabaseInstance.expensesDao()


val EXPENSES_LOCAL_DATA_SOURCE by lazy {
    ExpensesLocalDataSource(
        expensesDao = EXPENSES_DAO
    )
}

val EXPENSES_REPOSITORY by lazy {
    ExpensesRepository(
        expensesLocalDataSource = EXPENSES_LOCAL_DATA_SOURCE
    )
}



object TempDI {

    val createExpenseUseCase = CreateExpenseUseCase(
        expensesRepository = EXPENSES_REPOSITORY
    )
}