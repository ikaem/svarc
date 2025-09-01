package com.imkaem.android.svarc.core.presentation.widgets

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    value: String,
    label: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    onValueChange: ((String) -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    TextField(
        value = value,
        onValueChange = onValueChange ?: {},
        label = label,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        readOnly = readOnly,
        modifier = modifier
            /* TODO border is not even needed */
//            .border(BorderStroke(0.dp, Color.Transparent))
            .clip(RoundedCornerShape(0.dp)),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
        )
    )
}