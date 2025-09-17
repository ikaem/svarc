package com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model

import com.imkaem.android.svarc.costs.domain.models.ExpenseModel

data class HomeScreenViewModelState (
    val addExpenseState: AddExpenseState,
    val allExpenses: List<ExpenseModel>,
    val selectedTab: HomeScreenTab,

    /* TODO we will be adding other states, like home screen costs and edit daily budget and so on... */
)


data class AddExpenseState(
    val data: AddExpenseStateData,
    val isLoading: Boolean,
    val error: String? = null
)

data class AddExpenseStateData(
    val amount: String,
    val category: Int,
    val description: String,
    /* TODO maybe this is not string */
    val date: String,
    /* TODO maybe this is not string */
    val time: String,
)

enum class HomeScreenTab(val index: Int) {
    CURRENT(0),
    ALL_EXPENSES(1);

    companion object {
        fun fromIndex(index: Int) = entries.find { it.index == index } ?: CURRENT
    }
}