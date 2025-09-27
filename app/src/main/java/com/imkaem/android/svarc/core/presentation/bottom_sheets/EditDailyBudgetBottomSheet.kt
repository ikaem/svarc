package com.imkaem.android.svarc.core.presentation.bottom_sheets

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imkaem.android.svarc.core.presentation.widgets.CustomOptionsField
import com.imkaem.android.svarc.core.presentation.widgets.CustomTextField
import com.imkaem.android.svarc.core.utils.values.CustomOptionFieldValue
import com.imkaem.android.svarc.costs.domain.models.PeriodMonthModel
import com.imkaem.android.svarc.ui.theme.ColorBlue
import com.imkaem.android.svarc.ui.theme.ColorGreyDark
import com.imkaem.android.svarc.ui.theme.ColorGreyLight
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDailyBudgetBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    monthPeriods: List<PeriodMonthModel>,
    selectedMonthPeriod: PeriodMonthModel?,
    onChangeSelectedMonthPeriod: (PeriodMonthModel) -> Unit,
    selectedMonthPeriodDailyBudgetValue: String?,
    onChangeSelectedMonthPeriodDailyBudgetValue: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {

    /* TODO not sure still if the state should be here */
    val isPeriodDropdownExpanded = remember {
        mutableStateOf(false)
    }


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
                "EDIT DAILY BUDGET",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {


                CustomOptionsField(
                    isOptionsShown = isPeriodDropdownExpanded.value,
                    selectedOption = CustomOptionFieldValue(
                        key = selectedMonthPeriod?.id,
                        label = selectedMonthPeriod?.name,
                    ),
                    options = monthPeriods.map {
                        CustomOptionFieldValue(
                            key = it.id,
                            label = it.name,
                        )
                    },
                    label = "Select month",
                    onToggleOptionsShown = {
                        isPeriodDropdownExpanded.value = !isPeriodDropdownExpanded.value
                    },
                    leadingIcon = Icons.Filled.CalendarMonth,
                    onSelectOption = { option ->
                        val period = monthPeriods.firstOrNull { it.id == option.key }
                        if (period == null) {
                            /* TODO not sure if this is ok to return from */
                            return@CustomOptionsField
                        }

                        onChangeSelectedMonthPeriod(period)
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomTextField(
                        value = selectedMonthPeriodDailyBudgetValue
                            ?: selectedMonthPeriod?.amount.let { cents ->

                                if(cents == null) {
                                    return@let ""
                                }

                                val euros = cents / 100.00
                                val formatted = String.format(Locale.getDefault(), "%.2f", euros)
                                formatted
                            },
                        onValueChange = onChangeSelectedMonthPeriodDailyBudgetValue,
                        label = "Amount",
                        leadingIcon = Icons.Filled.Money,
                        modifier = Modifier.weight(1f),
                    )

                    CustomTextField(
                        value = "EUR",
                        onValueChange = {},
                        label = "Amount",
                        leadingIcon = Icons.Filled.Money,
                        modifier = Modifier.weight(1f),
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
//                verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onSave,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorBlue
                        )
                    ) {
                        Text("Submit")

                    }
                    Button(
                        onClick = onCancel,
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
}