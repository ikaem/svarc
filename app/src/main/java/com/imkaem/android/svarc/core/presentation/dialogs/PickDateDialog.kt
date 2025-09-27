package com.imkaem.android.svarc.core.presentation.dialogs

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickDateDialog(
    onDismissRequest: () -> Unit,
    dateState: DatePickerState,
) {


    /* TODO lets create date state here instead */

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