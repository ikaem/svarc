import android.hardware.camera2.params.ColorSpaceTransform
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.imkaem.android.svarc.core.presentation.dialogs.PickDateDialog
import com.imkaem.android.svarc.core.presentation.dialogs.PickTimeDialog
import com.imkaem.android.svarc.core.presentation.widgets.CustomTextField
import com.imkaem.android.svarc.core.utils.helpers.DateHelpers
import com.imkaem.android.svarc.costs.domain.models.CategoryModel
import com.imkaem.android.svarc.costs.presentation.PickCategoryDialog
import com.imkaem.android.svarc.ui.theme.ColorBlue
import com.imkaem.android.svarc.ui.theme.ColorGreyDark
import com.imkaem.android.svarc.ui.theme.ColorGreyLight
import com.imkaem.android.svarc.ui.theme.ColorGreyLighter
import com.imkaem.android.svarc.ui.theme.ColorRed
import com.imkaem.android.svarc.ui.theme.ColorWhite
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import kotlin.time.ExperimentalTime

/* date picker and working with dates
* https://medium.com/@andyphiri92/working-with-date-picker-in-jetpack-compose-3ec6c2f65a5a
*https://medium.com/javarevisited/why-you-shouldnt-use-localdatetime-to-avoid-production-issues-d2833fc7df41 
* */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun HomeScreenCostsActions(
    modifier: Modifier = Modifier
) {

    /* add expense bottom sheet stuff -> move to screen, and view model later */
    val addExpenseBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val currentTime = Calendar.getInstance()
    val nowHour = currentTime.get(Calendar.HOUR_OF_DAY)
    val nowMinute = currentTime.get(Calendar.MINUTE)

    /* DATE PICKER */
    val dateState = rememberDatePickerState()
    val showDatePickerDialog = remember { mutableStateOf(false) }

    /* TIME PICKER */
    val timeState = rememberTimePickerState(
        initialHour = nowHour,
        initialMinute = nowMinute,
        is24Hour = true,
    )
    val showTimePickerDialog = remember {
        mutableStateOf(false)
    }

    /* category picker */
    /* these will be held in view model, and categories stored and taken from database */
    val categoriesState = remember {
        mutableStateOf(
            listOf<CategoryModel>(
                CategoryModel(1, "Health"),
                CategoryModel(2, "Home"),
                CategoryModel(3, "Food"),
                CategoryModel(4, "Social"),
                CategoryModel(5, "Sport"),
                CategoryModel(id = 6, "Some longer category name")
            )
        )
    }
    val newCategoryState = remember {
        mutableStateOf("")
    }

    /* TODO i guess this will be populated, in view model, with sme category models */
    val selectedCategoryState = remember {
        mutableStateOf<CategoryModel?>(null)
    }
    val showCategoryPickerDialog = remember {
        mutableStateOf(false)
    }


    /* description state */
    val descriptionState = remember {
        mutableStateOf("")
    }




    when {
        showTimePickerDialog.value -> {
            PickTimeDialog(
                onDismissRequest = {
                    showTimePickerDialog.value = false
                },
                timeState = timeState,
            )
        }

        showDatePickerDialog.value -> {
            PickDateDialog(
                onDismissRequest = {
                    showDatePickerDialog.value = false
                },
                dateState = dateState,
            )
        }

        showCategoryPickerDialog.value -> {
            PickCategoryDialog(
                onDismissRequest = {
                    showCategoryPickerDialog.value = false
                },
                onSelectCategory = {
                    selectedCategoryState.value = it
                },
                onNewCategoryNameChange = {
                    newCategoryState.value = it
                },
                onNewCategoryAdd = {
                    val trimmed = newCategoryState.value.trim()
                    if (trimmed.isEmpty()) {
                        return@PickCategoryDialog
                    }

                    val existingCategory = categoriesState.value.find { i ->
                        i.name.equals(trimmed, ignoreCase = true)
                    }
                    if (existingCategory != null) {
                        return@PickCategoryDialog
                    }

                    val newId = (categoriesState.value.maxOfOrNull { it.id } ?: 0) + 1
                    val newCategory = CategoryModel(newId, trimmed)

                    val updatedList = categoriesState.value.toMutableList()
                    updatedList.add(newCategory)

                    categoriesState.value = updatedList.toList()
                    newCategoryState.value = ""
                },
                selectedCategory = selectedCategoryState.value,
                categories = categoriesState.value,
                newCategoryState.value,
            )
        }
    }


//    when {
//        addExpenseBottomSheetState.isVisible -> {
    /* TODO move this to bottom sheets */
    ModalBottomSheet(
        shape = RoundedCornerShape(topEnd = 0.dp),
        dragHandle = {},
        onDismissRequest = {
            scope.launch { addExpenseBottomSheetState.hide() }
        },
        sheetState = addExpenseBottomSheetState,
//        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)

    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp),
//            horizontalAlignment = Alignment.S
        ) {
            Text(
                "ADD EXPENSE",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp),
            )


            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {


                TextField(
                    value = "",
                    onValueChange = { /*TODO*/ },
                    label = { Text("Amount") },
                    trailingIcon = {
                        Icon(Icons.Filled.CreditCard, contentDescription = "Amount icon")
                    },
                    /* TODO it is stupid that i have to do this individually for each element*/
                    modifier = Modifier.fillMaxWidth()

                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        TextField(
                            enabled = false,
                            value = dateState.selectedDateMillis?.let { it ->

                                /* TODO ideally, we would store utc datetime to db*/
                                val instant = DateHelpers.millisecondsToInstant(it)
                                val formattedDate =
                                    DateHelpers.instantToLocalDateFormattedString(instant)

                                formattedDate

//                        } ?: "${nowDay-nowMonth-nowYear}",
                            } ?: currentTime.timeInMillis.let { it ->
                                val instant = DateHelpers.millisecondsToInstant(it)
                                val formattedDate =
                                    DateHelpers.instantToLocalDateFormattedString(instant)

                                formattedDate
                            },

                            onValueChange = { /*TODO*/ },
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.CalendarMonth,
                                    contentDescription = "Select date icon",
                                )
                            },
                            label = { Text("Date") },
                            modifier = Modifier.clickable {
                                showDatePickerDialog.value = true
                            }
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f)

                    ) {

                        CustomTextField(
                            readOnly = true,
                            value = timeState.let {
                                val hour = timeState.hour
                                val minute = timeState.minute

                                String.format(
                                    Locale.getDefault(),
                                    "%02d:%02d",
                                    hour,
                                    minute
                                )
                            },
                            label = { Text("Time") },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.AccessTime,
                                    contentDescription = "Select time icon",
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = "Open Pick Category icon",
                                    modifier = Modifier.clickable {
                                        showTimePickerDialog.value = true
                                    }
                                )
                            },
                        )

                        /* -------*/
//                        TextField(
//                            enabled = false,
////                        value = "",
//                            value = timeState.let {
//                                val hour = timeState.hour
//                                val minute = timeState.minute
//
//                                String.format(
//                                    Locale.getDefault(),
//                                    "%02d:%02d",
//                                    hour,
//                                    minute
//                                )
//                            },
//                            onValueChange = { /*TODO*/ },
//                            label = { Text("Time") },
//                            leadingIcon = {
//                                Icon(
//                                    Icons.Filled.AccessTime,
//                                    contentDescription = "Select time icon",
//                                )
//                            },
//                            trailingIcon = {
//                                Icon(
//                                    Icons.AutoMirrored.Filled.OpenInNew,
//                                    contentDescription = "Open time picker dialog icon",
//                                    modifier = Modifier
//                                        .clickable {
//                                            showCategoryPickerDialog.value = true
//                                        }
//                                        .clickable {
//                                            showTimePickerDialog.value = true
//                                        }
//                                )
//                            },
////                            modifier = Modifier.clickable {
////                                showTimePickerDialog.value = true
////                            }
//
//                        )
                    }
                }

                CustomTextField(
                    readOnly = true,
                    value = selectedCategoryState.value?.name ?: "",
                    label = { Text("Category") },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Category,
                            contentDescription = "Category icon",
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = "Open Pick Category icon",
                            modifier = Modifier.clickable {
                                showCategoryPickerDialog.value = true
                            }
                        )
                    },
                )

                CustomTextField(
                    value = descriptionState.value,
                    onValueChange = {
                        descriptionState.value = it
                    },
                    label = { Text("Description") },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Description icon",
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
//                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            addExpenseBottomSheetState.show()
                        }

                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorBlue
                    )
                ) {
                    Text("Add expense")

                }
                Button(
                    onClick = {
                        /* clean all */
                        selectedCategoryState.value = null
                        descriptionState.value = ""
//
//                        dateState./**/


                        /* close the modal */
                        scope.launch {
                            addExpenseBottomSheetState.hide()
                            /* TODO not sure if i should remove the bottom sheet from composition too? */

                        }

                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorGreyLight,
                    )
                ) {
                    Text("Cancel", color = ColorGreyDark)
                }


            }
        }
    }
//        }
//    }

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
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Filled.BarChart,
                contentDescription = "Balance report icon",
                modifier = Modifier.size(30.dp)

            )
            Text(
                "Balance report",
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
//        Text(
//
//            "10 EUR",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold
//        )
//        Text(
//            "Edit daily budget",
//            fontSize = 10.sp,
//            color = ColorGreyDark,
//        )
    }
}