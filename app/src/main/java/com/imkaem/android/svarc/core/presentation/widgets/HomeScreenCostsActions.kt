import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imkaem.android.svarc.core.presentation.bottom_sheets.EditDailyBudgetBottomSheet
import com.imkaem.android.svarc.core.presentation.dialogs.PickDateDialog
import com.imkaem.android.svarc.core.presentation.dialogs.PickTimeDialog
import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenAddCategoryEvent
import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenAddCategoryState
import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenAddExpenseEvent
import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenAddExpenseState
import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenCategoriesState
import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenCategoryPickerDialogState
import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenDatePickerDialogState
import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenEditDailyBudgetEvent
import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenEditDailyBudgetState
import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenMonthPeriodsState
import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenToggleDialogEvent
import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenTimePickerDialogState
import com.imkaem.android.svarc.core.utils.helpers.DateHelpers
import com.imkaem.android.svarc.expenses.presentation.PickCategoryDialog
import com.imkaem.android.svarc.ui.theme.ColorGreyDark
import com.imkaem.android.svarc.ui.theme.ColorGreyLighter
import com.imkaem.android.svarc.ui.theme.ColorWhite
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.time.ExperimentalTime

/* date picker and working with dates
* https://medium.com/@andyphiri92/working-with-date-picker-in-jetpack-compose-3ec6c2f65a5a
*https://medium.com/javarevisited/why-you-shouldnt-use-localdatetime-to-avoid-production-issues-d2833fc7df41 
* */


/* TODO this needs splitting into more atomic widgets */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun HomeScreenCostsActions(
    addExpenseState: HomeScreenAddExpenseState,
    addCategoryState: HomeScreenAddCategoryState,
    editDailyBudgetState: HomeScreenEditDailyBudgetState,
    categoriesState: HomeScreenCategoriesState,
    monthPeriodsState: HomeScreenMonthPeriodsState,
    datePickerDialogState: HomeScreenDatePickerDialogState,
    timePickerDialogState: HomeScreenTimePickerDialogState,
    categoryPickerDialogState: HomeScreenCategoryPickerDialogState,
    onNavigateToReports: () -> Unit,
    onAddExpenseEvent: (HomeScreenAddExpenseEvent) -> Unit,
    onAddCategoryEvent: (HomeScreenAddCategoryEvent) -> Unit,
    onEditDailyBudgetEvent: (HomeScreenEditDailyBudgetEvent) -> Unit,
    onToggleDialogEvent: (HomeScreenToggleDialogEvent) -> Unit,
    /* TODO i guess it would be better to use that events type on callbacks, because it would be less arguments passed here */
//    onChangeAddExpenseAmount: (amount: String) -> Unit,
    modifier: Modifier = Modifier,


    /* TODO only testing for now */
//    selectedDate: Long,
//    selectedHour: Int,
//    selectedMinute: Int,
) {
    val scope = rememberCoroutineScope()

    /* add expense state and stuff ----------*/
    /* add expense bottom sheet stuff -> move to screen, and view model later */
    val addExpenseBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

//    val currentTime = Calendar.getInstance()
//    val nowHour = currentTime.get(Calendar.HOUR_OF_DAY)
//    val nowMinute = currentTime.get(Calendar.MINUTE)

    /* amount state */
//    val amountState = remember {
//        mutableStateOf("0.00")
//    }

    /* DATE PICKER */
    /* TODO we will move logic for date and time to viewModel later */
    /* TODO i think these picker states should be moved to our composable closest to the picker - the one that is actually recomposed, so that these states can be reinstantiated? */
//    val dateState = rememberDatePickerState(
//        /* TODO ok, it seems like we can specify here initial selected date */
//        initialSelectedDateMillis = selectedDate,
//    )


    /* TIME PICKER */
//    val timeState = rememberTimePickerState(
//        initialHour = selectedHour,
//        initialMinute = selectedMinute,
//        is24Hour = true,
//    )

    /* edit daily budget state */
    /* TODO not sure where this should live - maybe in view model?
    * and then we can have state for it as normal, like other dialogs
    * and we would also call .show() and .hide() on sheet state  from the view model
    * */
    val editDailyBudgetBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )


    /* TODO this should be extracted somehow, so it does not pollute this */
    when {

        timePickerDialogState.isShown -> {
            PickTimeDialog(
                onDismissRequest = {
                    onToggleDialogEvent(HomeScreenToggleDialogEvent.ToggleTimePickerDialog)
                },
                selectedHour = addExpenseState.data.hour,
                selectedMinute = addExpenseState.data.minute,
                onTimeChange = { hour, minute ->
                    onAddExpenseEvent(
                        HomeScreenAddExpenseEvent.UpdateTime(
                            hour, minute
                        )
                    )
                }
//                timeState = timeState,
            )
        }


        datePickerDialogState.isShown -> {
            PickDateDialog(
                onDismissRequest = {
                    onToggleDialogEvent(HomeScreenToggleDialogEvent.ToggleDatePickerDialog)
                },
//                dateState = dateState,
                selectedDate = addExpenseState.data.date,
                onDateChange = {
                    onAddExpenseEvent(
                        HomeScreenAddExpenseEvent.UpdateDate(it)
                    )
                }
            )

        }

        categoryPickerDialogState.isShown -> {
            PickCategoryDialog(
                onDismissRequest = {
                    onToggleDialogEvent(HomeScreenToggleDialogEvent.ToggleCategoryPickerDialog)
                },
                onSelectCategory = { it ->
                    onAddExpenseEvent(
                        HomeScreenAddExpenseEvent.UpdateCategory(it.id),
                    )
                },
                onNewCategoryNameChange = {
                    onAddCategoryEvent(
                        HomeScreenAddCategoryEvent.UpdateName(it)
                    )
                },
                onNewCategoryAdd = {
                    onAddCategoryEvent(
                        HomeScreenAddCategoryEvent.SubmitCategory
                    )
                },
                selectedCategory = run {
                    /* TODO: hm, maybe this should also be handled by view model - to keep this inside add expense state */

                    /* TODO but it needs to be reactive? so we need to adjustit every time */
                    val selectedId = addExpenseState.data.categoryId
                    val selectedCategory =
                        categoriesState.categories.firstOrNull { it.id == selectedId }
                    selectedCategory

                },
                categories = categoriesState.categories,
                newCategoryName = addCategoryState.data.name,
            )
        }

        editDailyBudgetBottomSheetState.isVisible -> {
            EditDailyBudgetBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        editDailyBudgetBottomSheetState.hide()
                    }
                },
                sheetState = editDailyBudgetBottomSheetState,
                onSave = {
                    onEditDailyBudgetEvent(
                        HomeScreenEditDailyBudgetEvent.SubmitBudget
                    )
                },
                onCancel = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                monthPeriods = monthPeriodsState.periods,
                selectedMonthPeriod = editDailyBudgetState.data.selectedMonthPeriod,
                onChangeSelectedMonthPeriod = {

                    onEditDailyBudgetEvent(
                        HomeScreenEditDailyBudgetEvent.SelectMonthPeriod(it.id)
                    )
                },

                selectedMonthPeriodDailyBudgetValue = editDailyBudgetState.data.selectedMonthPeriodDailyBudgetValue,
                onChangeSelectedMonthPeriodDailyBudgetValue = {
                    onEditDailyBudgetEvent(
                        HomeScreenEditDailyBudgetEvent.UpdateBudgetAmount(it)
                    )
                }
            )
        }

        addExpenseBottomSheetState.isVisible -> {
            AddExpenseModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        addExpenseBottomSheetState.hide()
                    }
                },
                sheetState = addExpenseBottomSheetState,
                amountValue = run {
                    val amount = addExpenseState.data.amount
                    if (amount == null) return@run ""
                    return@run amount.toString()
                },
                onAmountChange = {
                    onAddExpenseEvent(HomeScreenAddExpenseEvent.UpdateAmount(it))
                },
//                dateValue = dateState.selectedDateMillis?.let {
                dateValue = addExpenseState.data.date.let {
                    val instant = DateHelpers.millisecondsToInstant((it))
                    val formattedDate = DateHelpers.instantToLocalDateFormattedString(instant)

                    formattedDate
//                } ?: currentTime.timeInMillis.let {
                },
//                    ?: selectedDate.let {
//                    val instant = DateHelpers.millisecondsToInstant((it))
//                    val formattedDate = DateHelpers.instantToLocalDateFormattedString(instant)
//
//                    formattedDate
//
//                },
                onOpenDatePicker = {
                    onToggleDialogEvent(HomeScreenToggleDialogEvent.ToggleDatePickerDialog)
                },
//                timeValue = timeState.let {
//                    val hour = timeState.hour
//                    val minute = timeState.minute
//
//                    String.format(
//                        Locale.getDefault(),
//                        "%02d:%02d",
//                        hour,
//                        minute
//                    )
//                },
                timeValue = run {
                    String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        addExpenseState.data.hour,
                        addExpenseState.data.minute,
//                        selectedHour,
//                        selectedMinute
                    )
                },
                onOpenTimePicker = {
                    onToggleDialogEvent(HomeScreenToggleDialogEvent.ToggleTimePickerDialog)
                },
                categoryValue = addExpenseState.data.categoryId?.let { categoryId ->
                    val categoryName =
                        categoriesState.categories.firstOrNull { category -> category.id == categoryId }
                    categoryName?.name
                } ?: "Unknown",
                onOpenCategoryPicker = {
                    onToggleDialogEvent(HomeScreenToggleDialogEvent.ToggleCategoryPickerDialog)
                },
                descriptionValue = addExpenseState.data.description,
                onDescriptionChange = {
                    onAddExpenseEvent(HomeScreenAddExpenseEvent.UpdateDescription(it))
                },
                onCancel = {

//                    onAddExpenseEvent
                },
                onSave = {
                    /* TODO not really sure what to do here yet */

                    onAddExpenseEvent(HomeScreenAddExpenseEvent.SubmitExpense)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),

                )
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        CostsAction(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    scope.launch {
                        editDailyBudgetBottomSheetState.show()
                    }
                }
        ) {
            Text(
                "10 EUR",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Edit daily budget",
                fontSize = 10.sp,
                color = ColorGreyDark,
            )
        }
        CostsAction(
            modifier = Modifier
                .weight(1f)
                .clickable {
//                Log.d("HomeScreen", "Navigate to reports")
                    onNavigateToReports()
                }
        ) {
            Icon(
                Icons.Filled.BarChart,
                contentDescription = "Report icon",
                modifier = Modifier.size(30.dp)

            )
            Text(
                "Reports",
                fontSize = 10.sp,
                color = ColorGreyDark,
            )
        }
        CostsAction(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    scope.launch {
                        addExpenseBottomSheetState.show()
                    }
                },
            color = ColorGreyDark,
        ) {
            Icon(
                Icons.Filled.AddCircleOutline,
                contentDescription = "Add expense icon",
                modifier = Modifier.size(30.dp),
                tint = ColorWhite,
            )
            Text(
                "Add expense",
                fontSize = 10.sp,
                color = ColorWhite,
            )
        }
    }
}

@Composable
private fun CostsAction(
    modifier: Modifier = Modifier,
    color: Color = ColorGreyLighter,
    content: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .background(color)
            .padding(5.dp)
            .fillMaxHeight()
    ) {

        content()
    }
}