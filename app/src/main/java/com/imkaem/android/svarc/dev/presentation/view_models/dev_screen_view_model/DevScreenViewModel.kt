package com.imkaem.android.svarc.dev.presentation.view_models.dev_screen_view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imkaem.android.svarc.dev.domain.use_cases.AddDummyDailyBudgetsUseCase
import com.imkaem.android.svarc.dev.domain.use_cases.AddDummyExpensesUseCase
import com.imkaem.android.svarc.dev.domain.use_cases.DeleteAllDailyBudgetsUseCase
import com.imkaem.android.svarc.dev.domain.use_cases.DeleteAllExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "DevScreenViewModel"

@HiltViewModel
class DevScreenViewModel @Inject constructor(
    private val addDummyExpensesUseCase: AddDummyExpensesUseCase,
    private val deleteAllExpensesUseCase: DeleteAllExpensesUseCase,
    private val addDummyDailyBudgetsUseCase: AddDummyDailyBudgetsUseCase,
    private val deleteAllDailyBudgetsUseCase: DeleteAllDailyBudgetsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<DevScreenState>(
        DevScreenState(
            expensesState = DevScreenExpensesState(
                isLoadingAddDummyExpenses = false,
                isLoadingDeleteAllExpenses = false,
                errorAddDummyExpenses = null,
                errorDeleteAllExpenses = null,
            ),
            dailyBudgetsState = DevScreenDailyBudgetsState(
                isLoadingAddDummyDailyBudgets = false,
                isLoadingDeleteAllDailyBudgets = false,
                errorAddDummyDailyBudgets = null,
                errorDeleteAllDailyBudgets = null,
            )
        )
    )

    val state: StateFlow<DevScreenState>
        get() = _state


    fun onEvent(event: DevScreenEvent) {
        when (event) {
            is DevScreenExpensesEvent -> handleExpensesEvent(event)
            is DevScreenDailyBudgetsEvent -> handleDailyBudgetsEvent(event)
        }
    }

    private fun handleExpensesEvent(event: DevScreenExpensesEvent) {
        when (event) {
            is DevScreenExpensesEvent.AddDummyExpenses -> onAddDummyExpenses()
            is DevScreenExpensesEvent.DeleteAllExpenses -> onDeleteAllExpenses()
        }
    }

    private fun handleDailyBudgetsEvent(event: DevScreenDailyBudgetsEvent) {
        when (event) {
            is DevScreenDailyBudgetsEvent.AddDummyDailyBudgets -> onAddDummyDailyBudgets()
            is DevScreenDailyBudgetsEvent.DeleteAllDailyBudgets -> onDeleteAllDailyBudgets()
        }
    }


    private fun onAddDummyExpenses() {
        /* TODO will need to pass dispatcher and erorr handler */
        /* TODO even though I prefer handling errors here */
        viewModelScope.launch {

            _state.update {
                it.copy(
                    expensesState = it.expensesState.copy(
                        isLoadingAddDummyExpenses = true,
                        errorAddDummyExpenses = null,
                    )
                )
            }

            try {
                addDummyExpensesUseCase()
                _state.update {
                    it.copy(
                        expensesState = it.expensesState.copy(
                            isLoadingAddDummyExpenses = false,
                            errorAddDummyExpenses = null,
                        )
                    )
                }
            } catch (e: Exception) {
                Log.d(TAG, "onAddDummyExpenses: there was an error adding dummy expenses", e)
                _state.update {
                    it.copy(
                        expensesState = it.expensesState.copy(
                            isLoadingAddDummyExpenses = false,
                            errorAddDummyExpenses = "There was an error adding dummy expenses",
                        )
                    )
                }
            }
        }
    }

    private fun onDeleteAllExpenses() {
        viewModelScope.launch {

            _state.update {
                it.copy(
                    expensesState = it.expensesState.copy(
                        isLoadingDeleteAllExpenses = true,
                        errorDeleteAllExpenses = null
                    )
                )
            }

            try {
                deleteAllExpensesUseCase()

                _state.update {
                    it.copy(
                        expensesState = it.expensesState.copy(
                            isLoadingDeleteAllExpenses = false,
                            errorDeleteAllExpenses = null
                        )
                    )
                }

            } catch (e: Exception) {

                _state.update {
                    it.copy(
                        expensesState = it.expensesState.copy(
                            isLoadingDeleteAllExpenses = true,
                            errorDeleteAllExpenses = "There was an error deleting all expenses"
                        )
                    )
                }
            }
        }
    }

    private fun onAddDummyDailyBudgets() {
        /* TODO will need to pass dispatcher and error handler
        * even though, I prefer handling errors here
        * */

        viewModelScope.launch {
            _state.update {
                it.copy(
                    dailyBudgetsState = it.dailyBudgetsState.copy(
                        isLoadingAddDummyDailyBudgets = true,
                        errorAddDummyDailyBudgets = null,
                    )
                )
            }

            try {
                addDummyDailyBudgetsUseCase()
                _state.update {
                    it.copy(
                        dailyBudgetsState = it.dailyBudgetsState.copy(
                            isLoadingAddDummyDailyBudgets = false,
                            errorAddDummyDailyBudgets = null,
                        )
                    )
                }
            } catch (e: Exception) {
                Log.d(
                    TAG,
                    "onAddDummyDailyBudgets: there was an error adding dummy daily budgets",
                    e
                )
                _state.update {
                    it.copy(
                        dailyBudgetsState = it.dailyBudgetsState.copy(
                            isLoadingAddDummyDailyBudgets = false,
                            errorAddDummyDailyBudgets = "There was an error adding dummy daily budgets",
                        )
                    )
                }
            }
        }
    }

    private fun onDeleteAllDailyBudgets() {
        /* TODO do error handling and dispatcher */
        viewModelScope.launch {

            _state.update {
                it.copy(
                    dailyBudgetsState = it.dailyBudgetsState.copy(
                        isLoadingDeleteAllDailyBudgets = true,
                        errorDeleteAllDailyBudgets = null,
                    )
                )
            }

            try {
                deleteAllDailyBudgetsUseCase()
                _state.update {
                    it.copy(
                        dailyBudgetsState = it.dailyBudgetsState.copy(
                            isLoadingDeleteAllDailyBudgets = false,
                            errorDeleteAllDailyBudgets = null,
                        )
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        dailyBudgetsState = it.dailyBudgetsState.copy(
                            isLoadingDeleteAllDailyBudgets = false,
                            errorDeleteAllDailyBudgets = "There was an error deleting all daily budgets",
                        )
                    )
                }
            }
        }
    }
}