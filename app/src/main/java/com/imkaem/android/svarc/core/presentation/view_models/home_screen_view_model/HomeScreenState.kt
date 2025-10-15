package com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model

import com.imkaem.android.svarc.expenses.domain.models.CategoryModel
import com.imkaem.android.svarc.expenses.domain.models.ExpenseModel
import com.imkaem.android.svarc.expenses.domain.models.PeriodMonthModel

data class HomeScreenState(
    /* for main screen current tab */
    val currentExpenses: List<ExpenseModel>,
    val addExpenseState: HomeScreenAddExpenseState,
    val expensesState: HomeScreenExpensesState,
    val addCategoryState: HomeScreenAddCategoryState,
    val categoriesState: HomeScreenCategoriesState,
    val editDailyBudgetState: HomeScreenEditDailyBudgetState,
    val monthPeriodsState: HomeScreenMonthPeriodsState,
    val selectedTab: HomeScreenTab,
    val timePickerDialogState: HomeScreenTimePickerDialogState,
    val datePickerDialogState: HomeScreenDatePickerDialogState,
    val categoryPickerDialogState: HomeScreenCategoryPickerDialogState,

    /* TODO this is temp only, we will be handing this later */
//    val selectedDate: Long,
//    val selectedHour: Int,
//    val selectedMinute: Int,

    /* TODO we will be adding other states, like home screen costs and edit daily budget and so on... */
)

data class HomeScreenMonthPeriodsState(
    val periods: List<PeriodMonthModel>,
    val isLoading: Boolean,
    val error: String? = null,
)

data class HomeScreenEditDailyBudgetState(
    val data: HomeScreenEditDailyBudgetStateData,
    val isLoading: Boolean,
    val error: String? = null
)

data class HomeScreenEditDailyBudgetStateData(
    val selectedMonthPeriod: PeriodMonthModel?,
    val selectedMonthPeriodDailyBudgetValue: String?
)


data class HomeScreenAddExpenseState(
    val data: HomeScreenAddExpenseStateData,
    val isLoading: Boolean,
    val error: String? = null
)


data class HomeScreenExpensesState(
    val expenses: List<ExpenseModel>,
    /* TODO will need pagination info */
    val isLoading: Boolean,
    val error: String? = null,
)

data class HomeScreenAddCategoryState(
    val data: HomeScreenAddCategoryStateData,
    val isLoading: Boolean,
    val error: String? = null
)

data class HomeScreenCategoriesState(
    val categories: List<CategoryModel>,
    val isLoading: Boolean,
    val error: String? = null
)

data class HomeScreenAddExpenseStateData(
//    val amount: String,
    val amount: Long?,
    /* TODO this could possible be some value class? */
    val categoryId: Int?,
//    val categoryName: String,
    val description: String,
    /* TODO maybe this is not string */
//    /* TODO maybe this is not string */
//    val date: String,
//    val time: String,
    val date: Long,
    val hour: Int,
    val minute: Int,
)

data class HomeScreenAddCategoryStateData(
    val name: String
)

data class HomeScreenTimePickerDialogState(
    val isShown: Boolean,
)

data class HomeScreenDatePickerDialogState(
    val isShown: Boolean,
)

data class HomeScreenCategoryPickerDialogState(
    val isShown: Boolean,
)

enum class HomeScreenTab(val index: Int) {
    CURRENT(0),
    ALL_EXPENSES(1);

    companion object {
        fun fromIndex(index: Int) = entries.find { it.index == index } ?: CURRENT
    }
}