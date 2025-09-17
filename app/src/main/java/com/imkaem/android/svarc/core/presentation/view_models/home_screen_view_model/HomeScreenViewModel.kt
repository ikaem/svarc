package com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/* TODO in trello, there is a card with ai suggestions. check it, apply events for user when they interact with view model */
class HomeScreenViewModel : ViewModel() {


    private val _state = MutableStateFlow<HomeScreenViewModelState>(
        generateInitialState()
    )
    val state: StateFlow<HomeScreenViewModelState>
        get() = _state


    fun onChangeSelectedTabIndex(index: Int) {
        val newState = _state.value.copy(
            selectedTab = HomeScreenTab.fromIndex(index),
        )

        _state.update {
            newState
        }
    }

    fun onChangeAddExpenseAmount(amount: String) {
        val newState = _state.value.copy(
            addExpenseState = _state.value.addExpenseState.copy(
                data = _state.value.addExpenseState.data.copy(
                    amount = amount,
                )
            )
        )

        _state.update {
            newState
        }
    }


    private fun generateInitialState(): HomeScreenViewModelState {
        return HomeScreenViewModelState(
            allExpenses = emptyList(),
            selectedTab = HomeScreenTab.CURRENT,
            addExpenseState = AddExpenseState(
                data = AddExpenseStateData(
                    amount = "",
                    /* TODO this should be some default */
                    category = 1,
                    description = "",
                    date = "",
                    time = "",
                ),
                isLoading = false,
                error = null
            )
        )
    }
}