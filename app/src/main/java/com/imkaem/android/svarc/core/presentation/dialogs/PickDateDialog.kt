package com.imkaem.android.svarc.core.presentation.dialogs

import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickDateDialog(
    onDismissRequest: () -> Unit,
//    dateState: DatePickerState,
    selectedDate: Long,
    onDateChange: (Long) -> Unit,
) {

    /* TODO lets create date state here instead */
    val dateState = rememberDatePickerState(
        /* TODO ok, it seems like we can specify here initial selected date */
        initialSelectedDateMillis = selectedDate,
    )

    /* TODO i am not entirely sure how this works, and what are caveats of using it */
    LaunchedEffect(dateState.selectedDateMillis) {
        Log.d("PickDateDialog", "selected date: ${dateState.selectedDateMillis}")
        dateState.selectedDateMillis?.let {
            onDateChange(it)
        }
    }


    DatePickerDialog(
//        onDismissRequest = {
//            showDatePickerDialog.value = false
//        },
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        dismissButton = {},
        shape = RoundedCornerShape(0.dp)
    ) {
        DatePicker(
//            state = dateState,
            state = dateState,
            showModeToggle = true
        )
    }
}