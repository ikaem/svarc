package com.imkaem.android.svarc.expenses.data.repositories

import com.imkaem.android.svarc.expenses.data.data_sources.ExpensesLocalDataSource
import com.imkaem.android.svarc.expenses.domain.models.CategoryModel
import com.imkaem.android.svarc.expenses.domain.models.DailyBudgetModel
import com.imkaem.android.svarc.expenses.domain.models.ExpenseModel
import com.imkaem.android.svarc.expenses.domain.models.ExpenseWithCategoryModel
import com.imkaem.android.svarc.expenses.utils.values.CreateDailyBudgetValue
import com.imkaem.android.svarc.expenses.utils.values.CreateExpenseValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/* TODO we need interface in domain layer for this */

/* TODO maybe we should not have another level of data source
* but, maybe we should, lets see how it goes
* */
@Singleton
class ExpensesRepository @Inject constructor(
    private val expensesLocalDataSource: ExpensesLocalDataSource,
) {

    suspend fun addExpense(
        expenseValue: CreateExpenseValue
    ): Long {
        val id = expensesLocalDataSource.addExpense(expenseValue)
        return id
    }

    suspend fun getExpensesWithCategories(): List<ExpenseWithCategoryModel> {
        val pojos = expensesLocalDataSource.getExpensesWithCategories()

        val models = pojos.map { pojo ->
            val dateTime = Instant.ofEpochMilli(pojo.expense.dateTimeMillis)

            val model = ExpenseWithCategoryModel(
                id = pojo.expense.id,
                amount = pojo.expense.amount,
                currency = pojo.expense.currency,
                dateTime = dateTime,
                description = pojo.expense.description,
                category = CategoryModel(
                    id = pojo.category.id,
                    name = pojo.category.name,
                )
            )
            model
        }

        return models
    }

    fun getExpensesFlow(
        fromMillisInclusive: Long,
        toMillisExclusive: Long,
    ): Flow<List<ExpenseModel>> {
        val entitiesFlow = expensesLocalDataSource.getExpensesFlow(
            fromMillisInclusive = fromMillisInclusive,
            toMillisExclusive = toMillisExclusive,
        )

        val modelsFlow = entitiesFlow.map { entities ->
            val models = entities.map { entity ->

                val dateTime = Instant.ofEpochMilli(entity.dateTimeMillis)

                val model = ExpenseModel(
                    id = entity.id,
                    amount = entity.amount,
                    currency = entity.currency,
                    dateTime = dateTime,
                    description = entity.description,
                    categoryId = entity.id,
                )

                model
            }

            models
        }

        return modelsFlow;
    }

    suspend fun addAllExpenses(expenses: List<CreateExpenseValue>): List<Long> {
        val ids = expensesLocalDataSource.addAllExpenses(expenses)
        return ids
    }

    suspend fun deleteAllExpenses() {
        expensesLocalDataSource.deleteAllExpenses()
    }

    suspend fun getCategories(): List<CategoryModel> {
        val entities = expensesLocalDataSource.getCategories()

        val models = entities.map { entity ->

            CategoryModel(
                id = entity.id,
                name = entity.name,
            )
        }

        return models
    }

    /* daily budgets */
    suspend fun addDailyBudget(dailyBudgetValue: CreateDailyBudgetValue): Long {
        val id = expensesLocalDataSource.addDailyBudget(
            dailyBudgetValue = dailyBudgetValue
        )

        return id
    }

    fun getDailyBudgetByIdFlow(id: Long): Flow<DailyBudgetModel?> {
        /* TODO create some converter */
        val entityFlow = expensesLocalDataSource.getDailyBudgetById(id)

        val modelFlow = entityFlow.map { entity ->
            entity?.let {
                DailyBudgetModel(
                    id = it.id,
                    amount = it.amount,
                    year = it.year,
                    month = it.month,
                )
            }
        }

        return modelFlow
    }

    fun getDailyBudgetByYearMonthFlow(year: Int, month: Int): Flow<DailyBudgetModel?> {
        /* TODO create some converter */
        val entityFlow = expensesLocalDataSource.getDailyBudgetByYearMonth(year, month)

        val modelFlow = entityFlow.map { entity ->
            entity?.let {
                DailyBudgetModel(
                    id = it.id,
                    amount = it.amount,
                    year = it.year,
                    month = it.month,
                )
            }
        }

        return modelFlow
    }

    suspend fun deleteAllDailyBudgets() {
        expensesLocalDataSource.deleteAllDailyBudgets()
    }
}