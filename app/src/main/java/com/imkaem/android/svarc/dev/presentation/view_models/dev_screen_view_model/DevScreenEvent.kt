package com.imkaem.android.svarc.dev.presentation.view_models.dev_screen_view_model

import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenEvent

sealed class DevScreenEvent {}


sealed class DevScreenExpensesEvent: DevScreenEvent() {
    object AddDummyExpenses: DevScreenExpensesEvent()
    object DeleteAllExpenses: DevScreenExpensesEvent()
}

sealed class DevScreenDailyBudgetsEvent: DevScreenEvent() {
    object AddDummyDailyBudgets: DevScreenDailyBudgetsEvent()
    object DeleteAllDailyBudgets: DevScreenDailyBudgetsEvent()
}