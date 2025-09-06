package com.imkaem.android.svarc.core.presentation.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import com.imkaem.android.svarc.core.utils.values.CustomOptionFieldValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOptionsField(
    isOptionsShown: Boolean,
    selectedOption: CustomOptionFieldValue,
    options: List<CustomOptionFieldValue>,
    label: String,
    /* TODO maybe this will be needed */
//    onToggleOptionsShown: (isShown: Boolean) -> Unit,
    onToggleOptionsShown: () -> Unit,
    onSelectOption: (CustomOptionFieldValue) -> Unit,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
) {

    ExposedDropdownMenuBox(
        expanded = isOptionsShown,
//        onExpandedChange = {
//            isPeriodDropdownExpanded.value = false
//        },
        onExpandedChange = {
            /* TODO not really sure what this does */
        },
//        modifier = Modifier.fillMaxWidth()
        modifier = modifier,
    ) {

        CustomTextField(
            value = selectedOption.label,
            readOnly = true,
            label = label,
            leadingIcon = leadingIcon,
            trailingIcon = isOptionsShown.let {
                return@let if (it) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore
            },
//            onTrailingIconTap = {
//                isPeriodDropdownExpanded.value = !isPeriodDropdownExpanded.value
//            },
            onTrailingIconTap = onToggleOptionsShown,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    MenuAnchorType.PrimaryEditable,
                    isOptionsShown,
//                    isPeriodDropdownExpanded.value
                )
                /* TODO i have no idea what this does */
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
//                        isPeriodDropdownExpanded.value = false
                        /* TODO not sure if i should expelictly call it close */
//                        onToggleOptionsShown()
                    }
                }

        )

        ExposedDropdownMenu(
//                        ExposedDropdownMenuBox(
            expanded = isOptionsShown,
            onDismissRequest = onToggleOptionsShown,
//            onDismissRequest = {
//                isPeriodDropdownExpanded.value = false
//            },
//                            onExpandedChange = {}
//                    modifier = Modifier.fillMaxWidth()
        ) {
            for (option in options) {
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        /* TODO i guess dropdown should be closed here */
                        onSelectOption(option)
                        onToggleOptionsShown()
                    }
                )
            }
        }
    }
}