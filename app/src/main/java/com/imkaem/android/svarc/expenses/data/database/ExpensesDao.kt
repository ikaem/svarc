package com.imkaem.android.svarc.expenses.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.imkaem.android.svarc.expenses.data.entities.local.ExpenseLocalEntity
import com.imkaem.android.svarc.expenses.data.entities.local.ExpenseWithCategoryPojo
import kotlinx.coroutines.flow.Flow


@Dao
interface ExpensesDao {
    @Query("SELECT * FROM EXPENSES ORDER BY date_time_millis ASC")
    suspend fun getAll(): List<ExpenseLocalEntity>

    /* TODO this needs to be converted to flow */
    @Transaction
    @Query("SELECT * FROM expenses ORDER BY date_time_millis ASC")
    suspend fun getAllWithCategories(): List<ExpenseWithCategoryPojo>

    @Query("SELECT * FROM expenses WHERE date_time_millis >= :fromMillisInclusive AND date_time_millis < :toMillisExclusive ORDER BY date_time_millis ASC")
    fun getAllFromInclusiveToExclusiveFlow(
        fromMillisInclusive: Long,
        toMillisExclusive: Long,
    ): Flow<List<ExpenseLocalEntity>>


    @Insert()
    suspend fun add(expense: ExpenseLocalEntity): Long

    /* TODO I am not really sure what is this supposed to return? */
    @Insert()
    suspend fun addAll(expenses: List<ExpenseLocalEntity>): List<Long>

    @Query("DELETE FROM expenses")
    suspend fun deleteAll()


}