package com.imkaem.android.svarc.expenses.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.imkaem.android.svarc.expenses.data.entities.local.ExpenseLocalEntity


@Dao
interface ExpensesDao {
    @Query("SELECT * FROM EXPENSES ORDER BY date_time_millis ASC")
    suspend fun getAll(): List<ExpenseLocalEntity>

    @Insert()
    suspend fun add(expense: ExpenseLocalEntity): Long
}