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
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenToggleDialogEvent
import com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model.HomeScreenTimePickerDialogState
import com.imkaem.android.svarc.core.utils.helpers.DateHelpers
import com.imkaem.android.svarc.costs.domain.models.PeriodMonthModel
import com.imkaem.android.svarc.costs.presentation.PickCategoryDialog
import com.imkaem.android.svarc.ui.theme.ColorGreyDark
import com.imkaem.android.svarc.ui.theme.ColorGreyLighter
import com.imkaem.android.svarc.ui.theme.ColorWhite
import kotlinx.coroutines.launch
import java.util.Calendar
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
    categoriesState: HomeScreenCategoriesState,
    datePickerDialogState: HomeScreenDatePickerDialogState,
    timePickerDialogState: HomeScreenTimePickerDialogState,
    categoryPickerDialogState: HomeScreenCategoryPickerDialogState,
    onNavigateToReports: () -> Unit,
    onAddExpenseEvent: (HomeScreenAddExpenseEvent) -> Unit,
    onAddCategoryEvent: (HomeScreenAddCategoryEvent) -> Unit,
    onToggleDialogEvent: (HomeScreenToggleDialogEvent) -> Unit,
    /* TODO i guess it would be better to use that events type on callbacks, because it would be less arguments passed here */
//    onChangeAddExpenseAmount: (amount: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    /* add expense state and stuff ----------*/
    /* add expense bottom sheet stuff -> move to screen, and view model later */
    val addExpenseBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val currentTime = Calendar.getInstance()
    val nowHour = currentTime.get(Calendar.HOUR_OF_DAY)
    val nowMinute = currentTime.get(Calendar.MINUTE)

    /* amount state */
//    val amountState = remember {
//        mutableStateOf("0.00")
//    }

    /* DATE PICKER */
    /* TODO we will move logic for date and time to viewModel later */
    val dateState = rememberDatePickerState()
//    val showDatePickerDialog = remember { mutableStateOf(false) }

    /* TIME PICKER */
    val timeState = rememberTimePickerState(
        initialHour = nowHour,
        initialMinute = nowMinute,
        is24Hour = true,
    )
//    val showTimePickerDialog = remember {
//        mutableStateOf(false)
//    }

    /* category picker */
    /* these will be held in view model, and categories stored and taken from database */
//    val oldCategoriesState = remember {
//        mutableStateOf(
//            listOf<CategoryModel>(
//                CategoryModel(1, "Health"),
//                CategoryModel(2, "Home"),
//                CategoryModel(3, "Food"),
//                CategoryModel(4, "Social"),
//                CategoryModel(5, "Sport"),
//                CategoryModel(id = 6, "Some longer category name"),
//                CategoryModel(7, "Other"),
//            )
//        )
//    }
//    val newCategoryState = remember {
//        mutableStateOf("")
//    }

    /* TODO i guess this will be populated, in view model, with sme category models */
//    val selectedCategoryState = remember {
//        mutableStateOf<CategoryModel?>(null)
//    }
//    val showCategoryPickerDialog = remember {
//        mutableStateOf(false)
//    }


    /* description state */
//    val descriptionState = remember {
//        mutableStateOf("")
//    }

    /* edit daily budget state */
    val editDailyBudgetBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    /* TODO this is temp here */
    val monthPeriods = listOf(
        PeriodMonthModel(1, 11, 2023, 323),
        PeriodMonthModel(2, 12, 2023, 115),
        PeriodMonthModel(3, 1, 2024, 345),
        PeriodMonthModel(4, 2, 2024, 459),
        PeriodMonthModel(5, 3, 2024, 711),
    )

    val selectedMonthPeriod = remember {
        mutableStateOf(monthPeriods.last())
    }

    val selectedMonthPeriodDailyBudget = remember {
        mutableStateOf<String?>(null)
    }


    /* TODO this should be extracted somehow, so it does not pollute this */
    when {

//        showTimePickerDialog.value -> {
        timePickerDialogState.isShown -> {
            PickTimeDialog(
                onDismissRequest = {
//                    showTimePickerDialog.value = false
                    onToggleDialogEvent(HomeScreenToggleDialogEvent.ToggleTimePickerDialog)
                },
                timeState = timeState,
            )
        }

//        showDatePickerDialog.value -> {
        datePickerDialogState.isShown -> {
            PickDateDialog(
                onDismissRequest = {
//                    showDatePickerDialog.value = false
                    onToggleDialogEvent(HomeScreenToggleDialogEvent.ToggleDatePickerDialog)
                },
                dateState = dateState,
            )
        }

//        showCategoryPickerDialog.value -> {
        categoryPickerDialogState.isShown -> {
            PickCategoryDialog(
                onDismissRequest = {
//                    showCategoryPickerDialog.value = false
                    onToggleDialogEvent(HomeScreenToggleDialogEvent.ToggleCategoryPickerDialog)
                },
                onSelectCategory = { it ->
//                    selectedCategoryState.value = it
                    onAddExpenseEvent(
                        HomeScreenAddExpenseEvent.UpdateCategory(it.id),
                    )
                },
                onNewCategoryNameChange = {
//                    newCategoryState.value = it
                    onAddCategoryEvent(
                        HomeScreenAddCategoryEvent.UpdateName(it)
                    )
                },
                onNewCategoryAdd = {
                    onAddCategoryEvent(
                        HomeScreenAddCategoryEvent.SubmitCategory
                    )

//                    val trimmed = newCategoryState.value.trim()
//                    if (trimmed.isEmpty()) {
//                        return@PickCategoryDialog
//                    }
//
//                    val existingCategory = oldCategoriesState.value.find { i ->
//                        i.name.equals(trimmed, ignoreCase = true)
//                    }
//                    if (existingCategory != null) {
//                        return@PickCategoryDialog
//                    }
//
//                    val newId = (oldCategoriesState.value.maxOfOrNull { it.id } ?: 0) + 1
//                    val newCategory = CategoryModel(newId, trimmed)
//
//                    val updatedList = oldCategoriesState.value.toMutableList()
//                    updatedList.add(newCategory)
//
//                    oldCategoriesState.value = updatedList.toList()
//                    newCategoryState.value = ""
                },
//                selectedCategory = selectedCategoryState.value,
                selectedCategory = run {
                    val selectedId = addExpenseState.data.categoryId
                    val selectedCategory =
                        categoriesState.categories.firstOrNull { it.id == selectedId }
                    selectedCategory

                },
                categories = categoriesState.categories,
                newCategoryName = addCategoryState.data.name,
//                newCategoryName = categoriesState.
//                categories = oldCategoriesState.value,
//                newCategoryState.value,
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
                onSave = {},
                onCancel = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                monthPeriods = monthPeriods,
                selectedMonthPeriod = selectedMonthPeriod.value,
                onChangeSelectedMonthPeriod = {
                    selectedMonthPeriod.value = it
                    /* TODO we want to make sure original period amount is used */
                    selectedMonthPeriodDailyBudget.value = null
                },
                selectedMonthPeriodDailyBudgetValue = selectedMonthPeriodDailyBudget.value,
                onChangeSelectedMonthPeriodDailyBudgetValue = {
                    selectedMonthPeriodDailyBudget.value = it
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
//                amountValue = amountState.value,
                amountValue = addExpenseState.data.amount,
                onAmountChange = {
//                    amountState.value = it
                    onAddExpenseEvent(HomeScreenAddExpenseEvent.UpdateAmount(it))
                },
                dateValue = dateState.selectedDateMillis?.let {
                    val instant = DateHelpers.millisecondsToInstant((it))
                    val formattedDate = DateHelpers.instantToLocalDateFormattedString(instant)

                    formattedDate
                } ?: currentTime.timeInMillis.let {
                    val instant = DateHelpers.millisecondsToInstant((it))
                    val formattedDate = DateHelpers.instantToLocalDateFormattedString(instant)

                    formattedDate

                },
                onOpenDatePicker = {
//                    showDatePickerDialog.value = true
                    onToggleDialogEvent(HomeScreenToggleDialogEvent.ToggleDatePickerDialog)
                },
                timeValue = timeState.let {
                    val hour = timeState.hour
                    val minute = timeState.minute

                    String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        hour,
                        minute
                    )
                },
                onOpenTimePicker = {
//                    showTimePickerDialog.value = true
                    onToggleDialogEvent(HomeScreenToggleDialogEvent.ToggleTimePickerDialog)
                },
//                categoryValue = selectedCategoryState.value?.name
//                    ?: oldCategoriesState.value.last().name,
                categoryValue = addExpenseState.data.categoryId?.let { categoryId ->
                    val categoryName =
                        categoriesState.categories.firstOrNull { category -> category.id == categoryId }
                    categoryName?.name
                } ?: "Unknown",
                onOpenCategoryPicker = {
//                    showCategoryPickerDialog.value = true
                    onToggleDialogEvent(HomeScreenToggleDialogEvent.ToggleCategoryPickerDialog)
                },

//                descriptionValue = descriptionState.value,
                descriptionValue = addExpenseState.data.description,
                onDescriptionChange = {
//                    descriptionState.value = it
                    onAddExpenseEvent(HomeScreenAddExpenseEvent.UpdateDescription(it))
                },
                onCancel = {
                    /* TODO add the expense */
                    /* clean all */
                    onAddExpenseEvent
//                    selectedCategoryState.value = null
//                    descriptionState.value = ""
//                    amountState.value = "0.00"
//                    dateState = null
                },
                onSave = {

                    /* TODO not really sure what to do here yet */
                    /* clean all */
//                    selectedCategoryState.value = null
//                    descriptionState.value = ""
//                    amountState.value = "0.00"
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