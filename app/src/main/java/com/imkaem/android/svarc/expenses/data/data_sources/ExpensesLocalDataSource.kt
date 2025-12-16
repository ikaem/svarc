package com.imkaem.android.svarc.expenses.data.data_sources

import android.util.Log
import com.imkaem.android.svarc.expenses.data.database.CategoriesDao
import com.imkaem.android.svarc.expenses.data.database.DailyBudgetsDao
import com.imkaem.android.svarc.expenses.data.database.ExpensesDao
import com.imkaem.android.svarc.expenses.data.entities.local.CategoryLocalEntity
import com.imkaem.android.svarc.expenses.data.entities.local.DailyBudgetEntity
import com.imkaem.android.svarc.expenses.data.entities.local.ExpenseLocalEntity
import com.imkaem.android.svarc.expenses.data.entities.local.ExpenseWithCategoryPojo
import com.imkaem.android.svarc.expenses.utils.values.CreateDailyBudgetValue
import com.imkaem.android.svarc.expenses.utils.values.CreateExpenseValue
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/* TODO should make interfaces for this */
/* TODO not suee if we should have this level - the data source before the actual lib - but lets try it */
@Singleton
class ExpensesLocalDataSource @Inject constructor(
    private val expensesDao: ExpensesDao,
    private val categoriesDao: CategoriesDao,
    private val dailyBudgetsDao: DailyBudgetsDao,
) {

    /* expenses */
    suspend fun addExpense(expenseValue: CreateExpenseValue): Long {
        val expenseLocalEntity = ExpenseLocalEntity(
            amount = expenseValue.amount,
            currency = expenseValue.currency,
            dateTimeMillis = expenseValue.dateTimeMillis,
            description = expenseValue.description,
            categoryId = expenseValue.categoryId,
        )

        val id = expensesDao.add(expenseLocalEntity)

        Log.d("ExpensesLocalDataSource", "Added expense with id: $id")

        return id
    }

    suspend fun getExpensesWithCategories(): List<ExpenseWithCategoryPojo> {
        val expensesWithCategories = expensesDao.getAllWithCategories()
        return expensesWithCategories
    }

    fun getExpensesFlow(
        fromMillisInclusive: Long,
        toMillisExclusive: Long,
    ): Flow<List<ExpenseLocalEntity>> {
        return expensesDao.getAllFromInclusiveToExclusiveFlow(
            fromMillisInclusive = fromMillisInclusive,
            toMillisExclusive = toMillisExclusive,
        )
    }

    suspend fun addAllExpenses(expenseValues: List<CreateExpenseValue>): List<Long> {
        val expenseEntities = expenseValues.map { expenseValue ->
            ExpenseLocalEntity(
                amount = expenseValue.amount,
                currency = expenseValue.currency,
                dateTimeMillis = expenseValue.dateTimeMillis,
                description = expenseValue.description,
                categoryId = expenseValue.categoryId,
            )
        }

        val ids = expensesDao.addAll(expenseEntities)
        return ids
    }

    suspend fun deleteAllExpenses() {
        expensesDao.deleteAll()
    }

    /* categories */
    suspend fun getCategories(): List<CategoryLocalEntity> {
        val categories = categoriesDao.getAll()
        return categories
    }

    /* daily budgets */
    suspend fun addDailyBudget(dailyBudgetValue: CreateDailyBudgetValue): Long {
        val dailyBudgetEntity = com.imkaem.android.svarc.expenses.data.entities.local.DailyBudgetEntity(
            amount = dailyBudgetValue.amount,
            year = dailyBudgetValue.year,
            month = dailyBudgetValue.month,
        )

        val id = dailyBudgetsDao.add(dailyBudgetEntity)

        Log.d("ExpensesLocalDataSource", "Added daily budget with id: $id")

        return id
    }

    fun getDailyBudgetById(id: Long): Flow<DailyBudgetEntity?> {
        return dailyBudgetsDao.getOneByIdFlow(id)
    }

    fun getDailyBudgetByYearMonth(year: Int, month: Int): Flow<DailyBudgetEntity?> {
        return dailyBudgetsDao.getOneByYearMonthFlow(year, month)
    }

    suspend fun deleteAllDailyBudgets() {
        dailyBudgetsDao.deleteAll()
    }


}

