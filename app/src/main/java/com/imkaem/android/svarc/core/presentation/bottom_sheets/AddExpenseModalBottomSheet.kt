import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imkaem.android.svarc.core.presentation.widgets.CustomTextField
import com.imkaem.android.svarc.ui.theme.ColorBlue
import com.imkaem.android.svarc.ui.theme.ColorGreyDark
import com.imkaem.android.svarc.ui.theme.ColorGreyLight


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseModalBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    amountValue: String,
    onAmountChange: (String) -> Unit,
    dateValue: String,
    onOpenDatePicker: () -> Unit,
    timeValue: String,
    onOpenTimePicker: () -> Unit,
    categoryValue: String,
    onOpenCategoryPicker: () -> Unit,
    descriptionValue: String,
    onDescriptionChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,


    ) {
    /* TODO we should make some kind of wrapper for bottom sheet - like CustomBottomSheet, that has these options already predefined */
    ModalBottomSheet(
        shape = RoundedCornerShape(topEnd = 0.dp),
        dragHandle = {},
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp),
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
                CustomTextField(
                    value = amountValue,
                    onValueChange = onAmountChange,
                    label = "Amount",
                    leadingIcon = Icons.Filled.CreditCard,
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomTextField(
                        modifier = Modifier.weight(1f),
                        readOnly = true,
                        value = dateValue,
//                        value = dateState.selectedDateMillis?.let {
//                            val instant = DateHelpers.millisecondsToInstant((it))
//                            val formattedDate =
//                                DateHelpers.instantToLocalDateFormattedString(instant)
//
//                            formattedDate
//                        } ?: currentTime.timeInMillis.let {
//                            val instant = DateHelpers.millisecondsToInstant((it))
//                            val formattedDate =
//                                DateHelpers.instantToLocalDateFormattedString(instant)
//
//                            formattedDate
//
//                        },
                        label = "Date",
                        leadingIcon = Icons.Filled.CalendarMonth,
                        trailingIcon = Icons.AutoMirrored.Filled.OpenInNew,
                        onTrailingIconTap = onOpenDatePicker,
//                        onTrailingIconTap = {
//                            showDatePickerDialog.value = true
//                        }
                    )
                    CustomTextField(
                        modifier = Modifier
                            .weight(1f),
                        readOnly = true,
                        value = timeValue,
//                        value = timeState.let {
//                            val hour = timeState.hour
//                            val minute = timeState.minute
//
//                            String.format(
//                                Locale.getDefault(),
//                                "%02d:%02d",
//                                hour,
//                                minute
//                            )
//                        },
//                        label = { Text("Time") },
                        label = "Time",
                        leadingIcon = Icons.Filled.AccessTime,
                        trailingIcon = Icons.AutoMirrored.Filled.OpenInNew,
                        onTrailingIconTap = onOpenTimePicker,
//                        onTrailingIconTap = {
//                            showTimePickerDialog.value = true
//                        }
                    )
                }

                CustomTextField(
                    readOnly = true,
                    value = categoryValue,
//                    value = selectedCategoryState.value?.name ?: categoriesState.value.last().name,
//                    label = { Text("Category") },
                    label = "Category",
//                    leadingIcon = {
//                        Icon(
//                            Icons.Filled.Category,
//                            contentDescription = "Category icon",
//                        )
//                    },
                    leadingIcon = Icons.Filled.Category,
                    modifier = Modifier
                        .fillMaxWidth(),
                    trailingIcon = Icons.AutoMirrored.Filled.OpenInNew,
                    onTrailingIconTap = onOpenCategoryPicker,
//                    onTrailingIconTap = {
//                        showCategoryPickerDialog.value = true
//                    },
//                    trailingIcon = {
//                        Icon(
//                            Icons.AutoMirrored.Filled.OpenInNew,
//                            contentDescription = "Open Pick Category icon",
//                            modifier = Modifier.clickable {
//                                showCategoryPickerDialog.value = true
//                            }
//                        )
//                    },
                )

                CustomTextField(
//                    value = descriptionState.value,
                    value = descriptionValue,
                    onValueChange = onDescriptionChange,
//                    onValueChange = {
//                        descriptionState.value = it
//                    },
                    label = "Description",
                    leadingIcon = Icons.Filled.Edit,
                    modifier = Modifier.fillMaxWidth(),
//                    trailingIcon = Icons.AutoMirrored.Filled.OpenInNew,
//                    onTrailingIconTap = {
//                        showCategoryPickerDialog.value = true
//                    },
                )
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
//                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
//                    onClick = onCancel,
                    onClick = onSave,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorBlue
                    )
                ) {
                    Text("Add expense")

                }
                Button(
                    onClick = onCancel,
//                    onClick = {
//                        /* clean all */
//                        selectedCategoryState.value = null
//                        descriptionState.value = ""
////
////                        dateState./**/
//
//
//                        /* close the modal */
//                        scope.launch {
//                            addExpenseBottomSheetState.hide()
//                            /* TODO not sure if i should remove the bottom sheet from composition too? */
//
//                        }
//
//                    },
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


}