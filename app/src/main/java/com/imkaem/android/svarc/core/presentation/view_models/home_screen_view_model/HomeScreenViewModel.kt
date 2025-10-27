package com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imkaem.android.svarc.core.utils.temp.TempDI
import com.imkaem.android.svarc.expenses.domain.models.CategoryModel
import com.imkaem.android.svarc.expenses.domain.models.ExpenseModel
import com.imkaem.android.svarc.expenses.domain.models.PeriodMonthModel
import com.imkaem.android.svarc.expenses.utils.values.CreateExpenseValue
import com.imkaem.android.svarc.expenses.utils.values.DateSpentValue
import com.imkaem.android.svarc.reports.utils.temp.TempDateSpentsGenerator
import com.imkaem.android.svarc.reports.utils.values.GraphRowValueConverters
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal
import java.util.Calendar






/* TODO maybe good to separate setting state for different parts of state
* https://trello.com/c/pnDpu2A4
*
* */
/* TODO in trello, there is a card with ai suggestions. check it, apply events for user when they interact with view model */
class HomeScreenViewModel : ViewModel() {

    /* TODO this will be injected via hilt later */
    val createExpenseUseCase = TempDI.createExpenseUseCase
    val getCategoriesUseCase = TempDI.getCategoriesUseCase

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
            is HomeScreenToggleDialogEvent -> handleToggleDialogEvent(event)
            is HomeScreenEditDailyBudgetEvent -> handleEditDailyBudgetEvent(event)
        }
    }

    private fun handleEditDailyBudgetEvent(event: HomeScreenEditDailyBudgetEvent) {
        when (event) {
            is HomeScreenEditDailyBudgetEvent.SelectMonthPeriod -> onEditDailyBudgetSelectMonthPeriod(
                event.periodId
            )

            is HomeScreenEditDailyBudgetEvent.UpdateBudgetAmount -> onEditDailyBudgetChangeAmount(
                event.amount
            )

            is HomeScreenEditDailyBudgetEvent.SubmitBudget -> onEditDailyBudgetSubmit()
        }
    }

    private fun handleToggleDialogEvent(event: HomeScreenToggleDialogEvent) {
        when (event) {
            is HomeScreenToggleDialogEvent.ToggleDatePickerDialog -> onToggleDatePickerDialog()
            is HomeScreenToggleDialogEvent.ToggleTimePickerDialog -> onToggleTimePickerDialog()
            is HomeScreenToggleDialogEvent.ToggleCategoryPickerDialog -> onToggleCategoryPickerDialog()
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

            is HomeScreenAddExpenseEvent.UpdateDescription -> onAddExpenseChangeDescription(event.description)
            is HomeScreenAddExpenseEvent.UpdateDate -> onAddExpenseChangeDate(event.date)
            is HomeScreenAddExpenseEvent.UpdateTime -> onAddExpenseChangeTime(
                event.hours,
                event.minutes,
            )
            /* TODO this is actually submit new event */
            HomeScreenAddExpenseEvent.SubmitExpense -> onAddExpenseSubmit()
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

    private fun onToggleDatePickerDialog() {
        val newState = _state.value.copy(
            datePickerDialogState = _state.value.datePickerDialogState.copy(
                isShown = !_state.value.datePickerDialogState.isShown
            )
        )
        _state.update {
            newState
        }
    }

    private fun onToggleTimePickerDialog() {
        val newState = _state.value.copy(
            timePickerDialogState = _state.value.timePickerDialogState.copy(
                isShown = !_state.value.timePickerDialogState.isShown
            )
        )
        _state.update {
            newState
        }
    }

    private fun onToggleCategoryPickerDialog() {
        val newState = _state.value.copy(
            categoryPickerDialogState = _state.value.categoryPickerDialogState.copy(
                isShown = !_state.value.categoryPickerDialogState.isShown
            )
        )
        _state.update {
            newState
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

                /* TODO this is wrong state - we should be adjust add category state instead */
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

    private fun onAddExpenseChangeAmount(value: String) {
        val amount = value.toLongOrNull()
        if (amount == null) return

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
        /*TODO maybe category should be full category, so we dont have to search for it in the actual composable? like with month periods?  */
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

    private fun onAddExpenseChangeDescription(description: String) {
        val newState = _state.value.copy(
            addExpenseState = _state.value.addExpenseState.copy(
                data = _state.value.addExpenseState.data.copy(
                    description = description,
                )
            )
        )

        _state.update {
            newState
        }
    }

    private fun onAddExpenseChangeDate(date: Long) {
        val newState = _state.value.copy(
            addExpenseState = _state.value.addExpenseState.copy(
                data = _state.value.addExpenseState.data.copy(
                    date = date,
                )
            )
        )

        _state.update {
            newState
        }
    }

    private fun onAddExpenseChangeTime(hour: Int, minute: Int) {
        val newState = _state.value.copy(
            addExpenseState = _state.value.addExpenseState.copy(
                data = _state.value.addExpenseState.data.copy(
                    hour = hour,
                    minute = minute,
                )
            )
        )

        _state.update {
            newState
        }
    }

    private fun onAddExpenseSubmit() {

        /* TODO also, dont do it if loading... */

        val addExpenseStateData = _state.value.addExpenseState.data
        /* TODO some validation should be done where? not sure maybe in use case */
        val isValid = validateAddExpenseState(addExpenseStateData)
        if (!isValid) {
            return
        }

        /* TODO we might be unnecessarily accessing state twice - it might change in meantime? */

        /* TODO need to pass dispatcher and */
        viewModelScope.launch {
            try {


                /* TODO just attemp for now */
                createExpenseUseCase(
                    amount = addExpenseStateData.amount!!,
                    categoryId = addExpenseStateData.categoryId!!,
                    description = addExpenseStateData.description,
                    date = addExpenseStateData.date,
                    hour = addExpenseStateData.hour,
                    minute = addExpenseStateData.minute,
                )

                /* TODO values should probably be sanitized or something? we will see where that happens */
                val newExpenses = dummyAddExpenseUseCase(
                    amount = addExpenseStateData.amount!!,
                    categoryId = addExpenseStateData.categoryId!!,
                    description = addExpenseStateData.description,
                    date = addExpenseStateData.date,
                    hour = addExpenseStateData.hour,
                    minute = addExpenseStateData.minute,
                )

                val newExpensesState = HomeScreenExpensesState(
                    expenses = newExpenses,
                    isLoading = false,
                    error = null,
                )

                val newState = _state.value.copy(
                    expensesState = newExpensesState,
                    /* TODO we should probably reset the AddExpenseState*/
                )

                _state.update {
                    newState
                }
            } catch (e: Exception) {
                /* TODO this needs to be improved - maybe some specific exception type */
                val addExpenseState = _state.value.addExpenseState.copy(
                    isLoading = false,
                    error = e.message,
                )

                val newState = _state.value.copy(
                    addExpenseState = addExpenseState
                )

                _state.update {
                    newState
                }
            }
        }
    }

    private fun validateAddExpenseState(addExpenseStateData: HomeScreenAddExpenseStateData): Boolean {

        /* TODO should add validation stuff here - to validate state */
        /* TODO should we also emit some kind of state to indicate which fields are invalid */

        val amount = addExpenseStateData.amount
        /* TODO we can allow negative values, because some expenses can be refunds or something ? */
        if (amount == null) {
            return false
        }

        val categoryId = addExpenseStateData.categoryId
        if (categoryId == null) {
            return false
        }

        val description = addExpenseStateData.description
        /* NOTE we can actually allow creation of expenses in past, so we are good */
        val date = addExpenseStateData.date
        val hour = addExpenseStateData.hour
        val minute = addExpenseStateData.minute

        return true

    }

    private fun onEditDailyBudgetChangeAmount(amount: String) {
        val newState = _state.value.copy(
            editDailyBudgetState = _state.value.editDailyBudgetState.copy(
                data = _state.value.editDailyBudgetState.data.copy(
                    selectedMonthPeriodDailyBudgetValue = amount,
                )
            )
        )

        _state.update {
            newState
        }
    }

    private fun onEditDailyBudgetSelectMonthPeriod(periodId: Int) {
        /* TODO maybe we could have another state, or field in this state, that says ... i dont know, something to keep full selectedMonthPeriod maybe? */


        val monthPeriods = _state.value.monthPeriodsState.periods
        val period = monthPeriods.firstOrNull { it.id == periodId }

        val newState = _state.value.copy(
            editDailyBudgetState = _state.value.editDailyBudgetState.copy(
                data = _state.value.editDailyBudgetState.data.copy(
                    selectedMonthPeriod = period,
                    selectedMonthPeriodDailyBudgetValue = period?.amount.toString(),
                )
            )
        )

        _state.update {
            newState
        }
    }

    private fun onEditDailyBudgetSubmit() {
        /* TODO this needs to add error handling and pass specific dispatcher */

        val periodId = _state.value.editDailyBudgetState.data.selectedMonthPeriod?.id
            ?: return

        val amount = _state.value.editDailyBudgetState.data.selectedMonthPeriodDailyBudgetValue
        val trimmed = amount?.trim()
        if (trimmed.isNullOrEmpty()) {
            return
        }

        viewModelScope.launch {
            /* TODO at this point si guess update to loading
            *   this means that initial state should be non-loading */

            try {

                val updatedMonthPeriods = dummyEditDailyBudgetUseCase(
                    periodId = periodId,
                    amount = trimmed,
                )

                /* TODFO this will be removed once real use case is used, because we will have a flow retreiving month periods */

                val updatedMonthPeriodsState = _state.value.monthPeriodsState.copy(
                    periods = updatedMonthPeriods,
                    isLoading = false,
                    error = null,
                )

                val newState = _state.value.copy(
                    monthPeriodsState = updatedMonthPeriodsState,
                )

                _state.update {
                    newState
                }

            } catch (e: Exception) {
                val editDailyBudgetState = HomeScreenEditDailyBudgetState(
                    data = _state.value.editDailyBudgetState.data,
                    isLoading = false,
                    error = e.message,
                )

                val newState = _state.value.copy(
                    editDailyBudgetState = editDailyBudgetState
                )

                _state.update {
                    newState
                }
            }
        }
    }


    private fun generateInitialState(): HomeScreenState {
        /* TODO i guess this could be extracted to something like getNowDateTimeValues
        *  , and then reuse it on toggle thing to show to reset all
        * using this -> https://slack-chats.kotlinlang.org/t/22780603/i-m-using-timepickerstate-and-datepickerstate-for-timepicker
        * */

        /* TODO not sure if this should be declared on the view model level? but if do so, then now date and time will be incorrect after using the same view model (screen) for some time */
        val currentTime = Calendar.getInstance()
        val nowHours = currentTime.get(Calendar.HOUR_OF_DAY)
        val nowMinutes = currentTime.get(Calendar.MINUTE)
//        val nowDay = currentTime.get(Calendar.DAY_OF_MONTH)
//        val nowMonth = currentTime.get(Calendar.MONTH) + 1
//        val nowYear = currentTime.get(Calendar.YEAR)
//        val nowMilliseconds = Instant.now().toEpochMilli()
        val nowMilliseconds = currentTime.timeInMillis



        return HomeScreenState(
            currentExpenses = emptyList(),
            /* TODO just separator to remind that this above needs to be a real state */
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
            addCategoryState = HomeScreenAddCategoryState(
                data = HomeScreenAddCategoryStateData(
                    name = ""
                ),
                isLoading = true,
                error = null,
            ),
            addExpenseState = HomeScreenAddExpenseState(
                data = HomeScreenAddExpenseStateData(
                    amount = null,
                    /* TODO this should be some default */
                    /* TODO this should be retrieved i guess? */
                    /* TODO i am not sure if this is good to be null initially */
                    categoryId = null,
//                    categoryName = "General",
                    description = "",
//                    date = "",
//                    time = "",
                    date = nowMilliseconds,
                    hour = nowHours,
                    minute = nowMinutes,
                ),

                isLoading = true,
                error = null
            ),
            selectedTab = HomeScreenTab.CURRENT,
            timePickerDialogState = HomeScreenTimePickerDialogState(
                isShown = false,
            ),
            datePickerDialogState = HomeScreenDatePickerDialogState(
                isShown = false,
            ),
            categoryPickerDialogState = HomeScreenCategoryPickerDialogState(
                isShown = false,
            ),
            editDailyBudgetState = HomeScreenEditDailyBudgetState(
                data = HomeScreenEditDailyBudgetStateData(
                    selectedMonthPeriod = null,
                    selectedMonthPeriodDailyBudgetValue = null,
                ),
                isLoading = false,
                error = null,
            ),
            monthPeriodsState = HomeScreenMonthPeriodsState(
                periods = emptyList(),
                isLoading = false,
                error = null,
            ),
//            selectedDate = nowMilliseconds,
//            selectedHour = nowHours,
//            selectedMinute = nowMinutes,

            /* TODO so here we have to calculate now actually*/
            /* TODO but now should only be calculated when we open the modal */
            /* TODO change this to calculate these numbers only when we open the add expense modal - we can do it in onHandleToggleDate and TimePicker Dialogs ...*/
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
//        val categories = dummyGetCategoriesUseCase()
        /* TODO temp only tis */
        val categories = getCategoriesUseCase()
        val expenses = dummyGetExpensesUseCase()
        val monthPeriods = dummyGetMonthPeriodsUseCase()

        /* TODO temp */
        val currentExpenses = dummyGetCurrentExpensesUseCase()

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

        val monthPeriodsState = HomeScreenMonthPeriodsState(
            periods = monthPeriods,
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

        val addExpenseState = _state.value.addExpenseState.copy(
            data = _state.value.addExpenseState.data.copy(
                categoryId = categories.firstOrNull()?.id,
            ),
            isLoading = false,
            error = null,
        )

//        val addExpensesState = HomeScreenAddExpenseState(
//            /* TODO this should be populated only on open of dialog - lets take care of it later */
//            data = _state.value.addExpenseState.data.copy(
////                addExpenseState = _state.value.addExpenseState.copy(
////                    data = _state.value.addExpenseState.data.copy(
////                        categoryId = categories.firstOrNull()?.id,
////                    )
////                )
//            ),
//            isLoading = _state.value.addExpenseState.
////            data = HomeScreenAddExpenseStateData(
////                amount = null,
////                /* TODO lets set first as default */
////                categoryId = categories.firstOrNull()?.id,
////                description = "",
////                /* TODO this needs to be adjusted so it initially shows now date and time
////                * will be handling this a bit later
////                * */
//////                date = "",
//////                time = "",
////            ),
////            isLoading = false,
////            error = null,
//        )

        val editDailyBudgetState = HomeScreenEditDailyBudgetState(
            data = HomeScreenEditDailyBudgetStateData(
                selectedMonthPeriod = monthPeriods.lastOrNull(),
                selectedMonthPeriodDailyBudgetValue = monthPeriods.lastOrNull()?.amount.toString(),
            ),
            isLoading = false,
            error = null,
        )

        val selectedTab = _state.value.selectedTab
        val timePickerDialogState = _state.value.timePickerDialogState
        val datePickerDialogState = _state.value.datePickerDialogState
        val categoryPickerDialogState = _state.value.categoryPickerDialogState


        val newState = _state.value.copy(
            /* TODO temp */
            currentExpenses = currentExpenses,
            expensesState = expensesState,
            categoriesState = categoriesState,
            monthPeriodsState = monthPeriodsState,
            addExpenseState = addExpenseState,
            addCategoryState = addCategoryState,
            editDailyBudgetState = editDailyBudgetState,
            selectedTab = selectedTab,
            timePickerDialogState = timePickerDialogState,
            datePickerDialogState = datePickerDialogState,
            categoryPickerDialogState = categoryPickerDialogState,
        )

        _state.update {
            newState
        }
    }

    /* TODO dummy use cases - will be delegated to real stuff later */

    /* TODO for now we return list of this month's expenses */
    private suspend fun dummyGetCurrentExpensesUseCase(): List<ExpenseModel> {

        /* so here we get this month expenses in total */
        val now = Instant.now().atZone(ZoneOffset.UTC)

        val year = now.year
        val month = now.monthValue
        val day = 1


        val startOfMonth = ZonedDateTime.of(
            year,
            month,
            day,
            0, 0, 0, 0,
            ZoneOffset.UTC
        ).toInstant()

        /* TODO i am not sure of this works */
        val startoOfNextMonth = startOfMonth.plus(
            Duration.ofDays(YearMonth.of(year, month).lengthOfMonth().toLong())
        )

        val currentMonthExpenses = dummyExpenses.filter { expense ->
            if(expense.dateTime < startOfMonth) return@filter false
            if(expense.dateTime >= startoOfNextMonth) return@filter false

            return@filter true
        }


        /* TODO this should return some kind of object that contains reports for current period  */

        /* so we need to
        * reduce expenses into day expenses
        * populate non-existing days with 0 values
        * calculate this months reports - we already do this in reports screen
        *
        * */

        val expensesByDay = currentMonthExpenses.groupBy { expense ->

            /* this creates zoned date time */
            val zonedDateTime = expense.dateTime.atZone(ZoneOffset.UTC)

            /* now we get just day */
            val day = zonedDateTime.dayOfMonth

            /* and we want to group by day */

            day
        }

        /* now we want to reduce each day expenses to 1 */

        val dayExpenses = expensesByDay.map { entry ->

            /* ok, now we get day */
            val day = entry.key

            val expensesForDay = entry.value
            val totalForDay = expensesForDay.sumOf { it.amount }
            val date = expensesForDay.first().dateTime

            val dateYear = date.atZone(ZoneOffset.UTC).year
            val dateMonth = date.atZone(ZoneOffset.UTC).monthValue
            val dateDay = date.atZone(ZoneOffset.UTC).dayOfMonth

            val normalizedDate = ZonedDateTime.of(
                dateYear,
                dateMonth,
                dateDay,
                0,0,0,0,
                ZoneOffset.UTC
            ).toInstant()

            val dateSpentValue = DateSpentValue(
                amount = totalForDay,
                date = normalizedDate,
            )


            return@map dateSpentValue
        }

        /* TODO this logic also exists in ReportScreenGraphs.kt*/


        val completeMonthDateSpents = TempDateSpentsGenerator.fillMonthDateSpentsGaps(
            year = year,
            month = month,
            dateSpents = dayExpenses,
        )

        /* TODO OK, now we need to calculate spents */

        val budget = 600L
        val spentGraphRowValues = GraphRowValueConverters.spentGraphRowValuesFromDateSpentValues(
            completeMonthDateSpents,
            budget
        )

        val dailyRemainderGraphRowValues = GraphRowValueConverters.dailyRemainderGraphRowValuesFromDateSpentValues(
            completeMonthDateSpents,
            budget
        )

        val accumulatedRemainderGraphRowValues = GraphRowValueConverters.accumulatedRemainderGraphRowValuesFromDateSpendValues(
            completeMonthDateSpents,
            budget
        )


        val nowAgain = Instant.now().atZone(ZoneOffset.UTC)
        val nowYear = nowAgain.year
        val nowMonth = nowAgain.monthValue
        val nowDay = nowAgain.dayOfMonth

        val normalizedInstant = ZonedDateTime.of(
            nowYear,
            nowMonth,
            nowDay,
            0,0,0,0,
            ZoneOffset.UTC
        ).toInstant()
        /* ok, now we need to find today, this week, and this month reports */
        val todayReportSpentGraphRowValue = spentGraphRowValues.first { value ->

            /* get normalized date again for the current value  */

            val valueDate = value.date.atZone(ZoneOffset.UTC)
            val valueYear = valueDate.year
            val valueMonth = valueDate.monthValue
            val valueDay = valueDate.dayOfMonth

            val normalizedValueDate = ZonedDateTime.of(
                valueYear,
                valueMonth,
                valueDay,
                0,0,0,0,
                ZoneOffset.UTC
            ).toInstant()

            return@first normalizedValueDate == normalizedInstant
        }

        val thisMonthReportSpentGraphRowValue = spentGraphRowValues.last()

        /* TODO and now for the week */

        /* lets find all sundays */
        val allSundaysInMonthSpentGraphRowValues = spentGraphRowValues.filter { it ->

            /* ok, lets get day for each row value */
            val valueDate = it.date
            val utcLocalDate = valueDate.atZone(ZoneOffset.UTC)

            /* using this
            * https://stackoverflow.com/a/54439448/9661910
            * */
            val formatter = DateTimeFormatter.ofPattern("EEEE")


            val dayOfWeekString = utcLocalDate.format(formatter)


            println("dayOfWeekString: $dayOfWeekString")


            if (dayOfWeekString == "Sunday") {
                return@filter true
            }

            false


        }

        /* now we have all sunday
        * lets now loop through them, and find the one that is closest to current day
        *
        * we can get rid of all previous sundays, because they are not relevant
        * then we we just need to find the first one
        * */

//        val allNowOrFutureSundays = allSundaysInMonthSpentGraphRowValues.filter { it ->
//
//            val valueDate = it.date
//
//            if(valueDate.isBefore(normalizedInstant)) {
//                false
//            } else {
//                true
//            }
//        }
//
//        /* TODO this is not true */
//
//        /* here actually we have to accumulate all of spendings up to that date, but only for the current week - so we have to make a sublist from previous monday to upcoming sunday, and calculate all spending */
//        val thisWeekReportSpentGraphRowValue = allNowOrFutureSundays.first()




        /* TODO ok, now we only need last value of the month reports, current day, */

        /* TODO we can also calculate the week report by:
        * split month into weeks
        * find last day of each week or last day of the month
        * so we can find every suday in the month - thats last day of the week
        * get current day
        * find which last day of each week is the closest to current day
        * thats our week report
        * */

        /* lets find all sundays in that month repor */


        return currentMonthExpenses

        /* how to get week? when does week start? lets say week starts monday? and if it is partially in previous or next month, we just in ignore it? month has a precedence?
        * maybe we dont even need week for now?
        * */

        /* so this returns expenses for:
        * today
        * this week
        * this month
        *
        * TODO maybe we can reuse already existing logic on reports - that
        * */

        /* TODO so we can calculate this month
        *   - and then just return values */
    }

    private suspend fun dummyGetMonthPeriodsUseCase(): List<PeriodMonthModel> {
        val monthPeriods = listOf(
            PeriodMonthModel(1, 11, 2023, 323),
            PeriodMonthModel(2, 12, 2023, 115),
            PeriodMonthModel(3, 1, 2024, 345),
            PeriodMonthModel(4, 2, 2024, 459),
            PeriodMonthModel(5, 3, 2024, 711),
        )

        delay(1000)
        return monthPeriods
    }

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

    private suspend fun dummyAddExpenseUseCase(
        amount: Long,
        categoryId: Int,
        description: String,
        date: Long,
        hour: Int,
        minute: Int,
    ): List<ExpenseModel> {
        /* TODO this will be a flow when real implementation arrives */

        /* TODO this is just proforma */
        /* TODO will need some converter for this */
        val hourInMillis = hour * 60 * 60 * 1000  // hour * 60 minutes * 60 seconds * 1000 millis
        val minuteInMillis = minute * 60 * 1000 // minute * 60 seconds * 1000 millis

        val dateTimeMillis = date + hourInMillis + minuteInMillis

        val createExpenseValue = CreateExpenseValue(
            amount = amount,
            currency = "EUR",
            description = description,
            categoryId = categoryId,
            dateTimeMillis = dateTimeMillis,
        )

        val id = _state.value.expensesState.expenses.size + 1

        val newExpense = ExpenseModel(
            id = id,
            amount = createExpenseValue.amount,
            currency = createExpenseValue.currency,
            dateTime = Instant.ofEpochMilli(createExpenseValue.dateTimeMillis),
            description = createExpenseValue.description,
            category = _state.value.categoriesState.categories.first { it.id == categoryId },
        )

        val newExpenses = _state.value.expensesState.expenses + newExpense

        return newExpenses
    }

    private suspend fun dummyGetExpensesUseCase(): List<ExpenseModel> {
        /* TODO this will be a flow when real implementation arrives */

        delay(1000)
        val expenses = dummyExpenses
        return expenses
    }

    private suspend fun dummyAddCategoryUseCase(name: String): CategoryModel {

        /* TODO when have real use case, this check will be done on database level i guess? */
        val categories = _state.value.categoriesState.categories

        val existing = categories.find { c ->
            c.name.equals(name, ignoreCase = true)
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

        /* TODO this should probably return all categories, so that view model does not need to do it - but it is dummy stuff anyway...*/
        return newCategory
    }

    /* TODO temp */
    private suspend fun dummyEditDailyBudgetUseCase(
        periodId: Int,
        amount: String,
    ): List<PeriodMonthModel> {

        val periods = _state.value.monthPeriodsState.periods

        val period = periods.firstOrNull { it.id == periodId } ?: return periods

        delay(1000)

        /* TODO not we have to replace existing period - this will all be done by database later */
        val updatedPeriods = periods.map { it ->
            if (it.id != period.id) {
                return@map it
            }

            val updated = it.copy(
                amount = amount.toInt()
            )

            updated
        }

        return updatedPeriods
    }
}

/* TODO temp only */
private val dummyExpenses = listOf<ExpenseModel>(
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
        Instant.now().plusMillis(1 * 24 * 60 * 60 * 1000),
        "Some description",
        CategoryModel(3, "Food")
    ),
    ExpenseModel(
        4,
        2000,
        "EUR",
        Instant.now().plusMillis(1 * 24 * 60 * 60 * 1000),
        "Some description",
        CategoryModel(4, "Social")
    ),
    ExpenseModel(
        5,
        1200,
        "EUR",
        Instant.now().plusMillis(2 * 24 * 60 * 60 * 1000),
        "Some description",
        CategoryModel(5, "Sport")
    ),
    ExpenseModel(
        6,
        300,
        "EUR",
        Instant.now().plusMillis(2 * 24 * 60 * 60 * 1000),
        "Some description",
        CategoryModel(7, "Other")
    ),
    ExpenseModel(
        7,
        900,
        "EUR",
        Instant.now().plusMillis(3 * 24 * 60 * 60 * 1000),
        "Some description",
        CategoryModel(6, "Some longer category name")
    ),
    ExpenseModel(
        8,
        400,
        "EUR",
        Instant.now().plusMillis(3 * 24 * 60 * 60 * 1000),
        "Some description",
        CategoryModel(1, "Health")
    ),
)