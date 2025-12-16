package com.imkaem.android.svarc.dev.presentation.view_models.dev_screen_view_model

data class DevScreenState(
    val expensesState: DevScreenExpensesState,
    val dailyBudgetsState: DevScreenDailyBudgetsState,
)

data class DevScreenExpensesState(
    val isLoadingAddDummyExpenses: Boolean,
    val isLoadingDeleteAllExpenses: Boolean,
    val errorAddDummyExpenses: String?,
    val errorDeleteAllExpenses: String?,
)

data class DevScreenDailyBudgetsState(
    val isLoadingAddDummyDailyBudgets: Boolean,
    val isLoadingDeleteAllDailyBudgets: Boolean,
    val errorAddDummyDailyBudgets: String?,
    val errorDeleteAllDailyBudgets: String?,
)