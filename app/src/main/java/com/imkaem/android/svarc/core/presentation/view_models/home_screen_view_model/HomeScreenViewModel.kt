package com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imkaem.android.svarc.costs.domain.models.CategoryModel
import com.imkaem.android.svarc.costs.domain.models.ExpenseModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

/* TODO maybe good to separate setting state for different parts of state
* https://trello.com/c/pnDpu2A4
*
* */
/* TODO in trello, there is a card with ai suggestions. check it, apply events for user when they interact with view model */
class HomeScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow<HomeScreenState>(
        generateInitialState()
    )
    val state: StateFlow<HomeScreenState>
        get() = _state

    init {
        loadAndPopulateState()
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenAddExpenseEvent -> handleAddExpenseEvent(event)
            is HomeScreenChangeTabEvent -> handleChangeTabEvent(event)
            is HomeScreenAddCategoryEvent -> handleAddCategoryEvent(event)
        }
    }


    private fun handleAddCategoryEvent(event: HomeScreenAddCategoryEvent) {
        when (event) {
            is HomeScreenAddCategoryEvent.UpdateName -> onAddCategoryChangeName(event.name)
            /* this is actual submission */
            is HomeScreenAddCategoryEvent.SubmitCategory -> onSubmitCategory()
        }
    }


    private fun handleAddExpenseEvent(event: HomeScreenAddExpenseEvent) {
        when (event) {
            is HomeScreenAddExpenseEvent.UpdateAmount -> onAddExpenseChangeAmount(event.amount)
            is HomeScreenAddExpenseEvent.UpdateCategory -> onAddExpenseChangeCategory(
                event.categoryId,
            )

            is HomeScreenAddExpenseEvent.UpdateDate -> TODO()
            is HomeScreenAddExpenseEvent.UpdateDescription -> TODO()
            is HomeScreenAddExpenseEvent.UpdateTime -> TODO()
            /* TODO this is actually submit new event */
            HomeScreenAddExpenseEvent.SubmitExpense -> TODO()
        }
    }


    private fun handleChangeTabEvent(event: HomeScreenChangeTabEvent) {
        when (event) {
            is HomeScreenChangeTabEvent.ChangeTab -> {
                val index = HomeScreenTab.fromIndex(event.index)
                val newState = _state.value.copy(
                    selectedTab = index
                )
                _state.update {
                    newState
                }
            }
        }
    }


    private fun onSubmitCategory() {
        /* TODO this needs to add error handling and pass specific dispatcher */
        val name = _state.value.addCategoryState.data.name

        val trimmed = name.trim()
        if (trimmed.isEmpty()) {
            return
        }

        viewModelScope.launch {
            /* TODO ad this point i guess update to loading
            * this means that initial state should be non-loading?
            * */

            try {
                val newCategory = dummyAddCategoryUseCase(trimmed)

                /* TODO this will be removed once real use case is used
                *   because we will have a flow retrieving categories */

                val currentCategories = _state.value.categoriesState.categories
                val newCategories = currentCategories + newCategory

                val newCategoryState = HomeScreenCategoriesState(
                    categories = newCategories,
                    isLoading = false,
                    error = null,
                )

                val newState = _state.value.copy(
                    categoriesState = newCategoryState
                )

                _state.update {
                    newState
                }
            } catch (e: Exception) {
                /* TODO this needs to be improved, maybe some specific exception types */
                val newCategoryState = HomeScreenCategoriesState(
                    categories = _state.value.categoriesState.categories,
                    isLoading = false,
                    error = e.message,
                )

                val newState = _state.value.copy(
                    categoriesState = newCategoryState
                )

                _state.update {
                    newState
                }
            }
        }
    }

    private fun onAddCategoryChangeName(name: String) {
        val newState = _state.value.copy(
            addCategoryState = _state.value.addCategoryState.copy(
                data = _state.value.addCategoryState.data.copy(
                    name = name,
                )
            )
        )

        _state.update {
            newState
        }
    }

    private fun onAddExpenseChangeAmount(amount: String) {
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

    private fun onAddExpenseChangeCategory(categoryId: Int) {
        val newState = _state.value.copy(
            addExpenseState = _state.value.addExpenseState.copy(
                data = _state.value.addExpenseState.data.copy(
                    categoryId = categoryId,
                )
            )
        )

        _state.update {
            newState
        }
    }


    private fun generateInitialState(): HomeScreenState {
        return HomeScreenState(
            expensesState = HomeScreenExpensesState(
                expenses = emptyList(),
                isLoading = true,
                error = null,
            ),
            categoriesState = HomeScreenCategoriesState(
                categories = emptyList(),
                isLoading = true,
                error = null
            ),
            selectedTab = HomeScreenTab.CURRENT,
            addCategoryState = HomeScreenAddCategoryState(
                data = HomeScreenAddCategoryStateData(
                    name = ""
                ),
                isLoading = true,
                error = null,
            ),
            addExpenseState = HomeScreenAddExpenseState(
                data = HomeScreenAddExpenseStateData(
                    amount = "",
                    /* TODO this should be some default */
                    /* TODO this should be retrieved i guess? */
                    /* TODO i am not sure if this is good to be null initially */
                    categoryId = null,
//                    categoryName = "General",
                    description = "",
                    date = "",
                    time = "",
                ),

                isLoading = true,
                error = null
            )
        )
    }

    /* populated state generation */
    private fun loadAndPopulateState() {
        /* TODO not sure if everything should be loaded initially - for now lets load all right now */
        viewModelScope.launch {

            /* if any data should be loaded into db first */
            loadData()

            /* populate state */
            generatePopulatedState()
        }
    }

    /* TODO move these below */

    private suspend fun loadData() {
        /* this will eventually load data from remote into db, if needed */
    }

    private suspend fun generatePopulatedState() {
        /* TODO maybe should be passing io dispatcher and adding explict error handler here */
        /* TODO this should be separated i guess, as per comment and link at the top of this file */
        val categories = dummyGetCategoriesUseCase()
        val expenses = dummyGetExpensesUseCase()

        val categoriesState = HomeScreenCategoriesState(
            categories = categories,
            isLoading = false,
            error = null,
        )

        val expensesState = HomeScreenExpensesState(
            expenses = expenses,
            isLoading = false,
            error = null,
        )

        val addCategoryState = HomeScreenAddCategoryState(
            data = HomeScreenAddCategoryStateData(
                name = ""
            ),
            isLoading = false,
            error = null,
        )

        val addExpensesState = HomeScreenAddExpenseState(
            data = HomeScreenAddExpenseStateData(
                amount = "",
                /* TODO lets set first as default */
                categoryId = categories.firstOrNull()?.id,
                description = "",
                /* TODO this needs to be adjusted so it initially shows now date and time
                * will be handling this a bit later
                * */
                date = "",
                time = "",
            ),
            isLoading = false,
            error = null,
        )

        val selectedTab = _state.value.selectedTab

        val newState = _state.value.copy(
            expensesState = expensesState,
            categoriesState = categoriesState,
            addExpenseState = addExpensesState,
            addCategoryState = addCategoryState,
            selectedTab = selectedTab,
        )

        _state.update {
            newState
        }
    }

    /* TODO dummy use cases - will be delegated to real stuff later */
    private suspend fun dummyGetCategoriesUseCase(): List<CategoryModel> {
        /* TODO this will be a flow when real implementation arrives */
        val categories = listOf<CategoryModel>(
            CategoryModel(1, "Health"),
            CategoryModel(2, "Home"),
            CategoryModel(3, "Food"),
            CategoryModel(4, "Social"),
            CategoryModel(5, "Sport"),
            CategoryModel(id = 6, "Some longer category name"),
            CategoryModel(7, "Other"),
        )
        delay(1000)
        return categories
    }

    private suspend fun dummyGetExpensesUseCase(): List<ExpenseModel> {
        /* TODO this will be a flow when real implementation arrives */
        val expenses = listOf<ExpenseModel>(
            ExpenseModel(
                1,
                700,
                "EUR",
                Instant.now(),
                "Some description",
                CategoryModel(1, "Health")
            ),
            ExpenseModel(
                2,
                1500,
                "EUR",
                Instant.now(),
                "Some description",
                CategoryModel(2, "Home")
            ),
            ExpenseModel(
                3,
                500,
                "EUR",
                Instant.now(),
                "Some description",
                CategoryModel(3, "Food")
            ),
            ExpenseModel(
                4,
                2000,
                "EUR",
                Instant.now(),
                "Some description",
                CategoryModel(4, "Social")
            ),
            ExpenseModel(
                5,
                1200,
                "EUR",
                Instant.now(),
                "Some description",
                CategoryModel(5, "Sport")
            ),
            ExpenseModel(
                6,
                300,
                "EUR",
                Instant.now(),
                "Some description",
                CategoryModel(7, "Other")
            ),
        )
        delay(1000)
        return expenses
    }

    private suspend fun dummyAddCategoryUseCase(name: String): CategoryModel {

        /* TODO when have real use case, this check will be done on database level i guess? */
        val categories = _state.value.categoriesState.categories

        val existing = categories.find { c ->
            c.name === name
        }
        if (existing != null) {
            throw IllegalStateException("Category with name $name already exists")
        }

        /* TODO this will be a flow when real implementation arrives */
        delay(1000)
        val newId = categories.size + 1

        val newCategory = CategoryModel(
            id = newId,
            name = name,
        )

        return newCategory
    }
}

