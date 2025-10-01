package com.imkaem.android.svarc.core.utils.temp

import com.imkaem.android.svarc.core.data.database.SvarcDatabaseInstance
import com.imkaem.android.svarc.expenses.data.database.ExpensesDao


val EXPENSES_DAO: ExpensesDao = SvarcDatabaseInstance.expensesDao()



object TempDI {
}