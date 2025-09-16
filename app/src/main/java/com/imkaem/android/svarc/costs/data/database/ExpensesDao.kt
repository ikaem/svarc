package com.imkaem.android.svarc.costs.data.database

import androidx.room.Dao
import androidx.room.Query
import com.imkaem.android.svarc.costs.data.local.ExpenseLocalEntity


@Dao
interface ExpensesDao {
    @Query("SELECT * FROM EXPENSES ORDER BY date_time_millis ASC")
    suspend fun getAll(): List<ExpenseLocalEntity>
}