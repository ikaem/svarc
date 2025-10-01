package com.imkaem.android.svarc.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imkaem.android.svarc.expenses.data.database.ExpensesDao
import com.imkaem.android.svarc.expenses.data.local.CategoryLocalEntity
import com.imkaem.android.svarc.expenses.data.local.ExpenseLocalEntity

@Database(
    entities = [
        ExpenseLocalEntity::class,
        CategoryLocalEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class SvarcDatabase : RoomDatabase() {
    abstract val expensesDao: ExpensesDao
}