package com.imkaem.android.svarc.core.presentation.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    value: String,
//    label: @Composable () -> Unit,
    label: String,
//    leadingIcon: @Composable (() -> Unit),
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    onValueChange: ((String) -> Unit)? = null,
//    trailingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconTap: (() -> Unit)? = null,
) {
    TextField(
        value = value,
        textStyle = TextStyle(
            fontSize = 14.sp,
        ),
        onValueChange = onValueChange ?: {},
//        label = label,
        label = {
            Text(
                text = label,
//                fontSize = 16.sp,
                color = Color.Gray,
            )
        },
        leadingIcon = {
            Icon(
                leadingIcon,
                contentDescription = "$label icon",
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            trailingIcon?.let {
                Icon(
                    it,
                    contentDescription = "$label icon",
                    modifier = Modifier.size(20.dp).clickable {
                        onTrailingIconTap?.invoke()
                    }
                )
            }
        },

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