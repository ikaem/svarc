package com.imkaem.android.svarc.core.presentation.screens

import CustomTabbedView
import HomeScreenCostsActions
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenChangeTabEvent
import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenViewModel
import com.imkaem.android.svarc.core.presentation.widgets.HomeScreenAllExpenses
import com.imkaem.android.svarc.core.presentation.widgets.HomeScreenCurrentCosts

@Composable
fun HomeScreen(
    onNavigateToReports: () -> Unit,
) {
    val viewModel: HomeScreenViewModel = viewModel()
    val state = viewModel.state.collectAsStateWithLifecycle().value

    val currentExpenses = state.currentExpenses

    Scaffold { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {

            HomeScreenCostsActions(
                addExpenseState = state.addExpenseState,
                addCategoryState = state.addCategoryState,
                editDailyBudgetState = state.editDailyBudgetState,
                categoriesState = state.categoriesState,
                monthPeriodsState = state.monthPeriodsState,
                datePickerDialogState = state.datePickerDialogState,
                timePickerDialogState = state.timePickerDialogState,
                categoryPickerDialogState = state.categoryPickerDialogState,
                onNavigateToReports = onNavigateToReports,
                onAddExpenseEvent = viewModel::onEvent,
                onAddCategoryEvent = viewModel::onEvent,
                onEditDailyBudgetEvent = viewModel::onEvent,
                onToggleDialogEvent = viewModel::onEvent,
                modifier = Modifier.padding(horizontal = 10.dp),
                /* TODO temp only */
//                selectedDate = state.selectedDate,
//                selectedHour = state.selectedHour,
//                selectedMinute = state.selectedMinute,
            )

            HorizontalDivider(
                modifier = Modifier.padding(10.dp)
            )

            CustomTabbedView(
                currentTabIndex = state.selectedTab.index,
                onTabSelected = { it ->
                    viewModel.onEvent(HomeScreenChangeTabEvent.ChangeTab(it))
                },
                tabLabels = listOf("Current", "All expenses"),
                tabs = listOf(
                    { HomeScreenCurrentCosts(
                        currentExpenses = state.currentExpenses,
                        /* TODO this is very temp hardcoding - this should come from the view model state */
                        dailyBudget = 1000L
                    ) },
                    { HomeScreenAllExpenses(state.expensesState.expenses) }
                ),
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }


    }

}



