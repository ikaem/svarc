package com.imkaem.android.svarc.expenses.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.imkaem.android.svarc.expenses.data.entities.local.DailyBudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyBudgetsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(dailyBudget: DailyBudgetEntity): Long

    @Query("SELECT * FROM daily_budgets WHERE id = :id")
    fun getOneByIdFlow(id: Long): Flow<DailyBudgetEntity?>

    @Query("SELECT * FROM daily_budgets WHERE year = :year AND month = :month")
    fun getOneByYearMonthFlow(year: Int, month: Int): Flow<DailyBudgetEntity?>

    @Query("DELETE FROM daily_budgets")
    suspend fun deleteAll()
}