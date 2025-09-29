package com.imkaem.android.svarc.core.presentation.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.imkaem.android.svarc.ui.theme.ColorWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickTimeDialog(
    onDismissRequest: () -> Unit,
    selectedHour: Int,
    selectedMinute: Int,
    onTimeChange: (hour: Int, minute: Int) -> Unit,
//    timeState: TimePickerState,
) {

    val timeState = rememberTimePickerState(
        initialHour = selectedHour,
        initialMinute = selectedMinute,
        is24Hour = true,
    )

    LaunchedEffect(timeState.hour, timeState.minute) {
        onTimeChange(timeState.hour, timeState.minute)
    }


    Dialog(
        onDismissRequest = onDismissRequest,
//                properties = DialogProperties(usePlatformDefaultWidth = true)
    ) {
        Column(
            modifier = Modifier
                .background(ColorWhite)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                "Select time",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            TimePicker(
                state = timeState,
                layoutType = TimePickerLayoutType.Vertical
            )
        }
    }

}