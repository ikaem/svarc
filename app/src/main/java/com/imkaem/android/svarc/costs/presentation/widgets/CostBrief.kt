package com.imkaem.android.svarc.costs.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imkaem.android.svarc.ui.theme.ColorGreyLight
import com.imkaem.android.svarc.ui.theme.ColorGreyLighter

@Composable
fun CostBrief(
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .background(ColorGreyLighter)
            .fillMaxWidth()
    ) {
        CostBriefSegment(
            icon = Icons.Filled.CreditCard,
            iconDescription = "Cost icon",
            text = "30 EUR",
            textWeight = FontWeight.Bold,
            modifier = Modifier
                .background(ColorGreyLight)
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .fillMaxWidth()
        )
        Column(
            modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            CostBriefSegment(
                icon = Icons.Filled.CalendarMonth,
                iconDescription = "Date icon",
                text = "25 October 2025, 14:30",
            )
            CostBriefSegment(
                icon = Icons.Filled.Category,
                iconDescription = "Category icon",
                text = "Utilities",
            )
        }
    }
}

@Composable
private fun CostBriefSegment(
    icon: ImageVector,
    iconDescription: String,
    text: String,
    modifier: Modifier = Modifier,
    textWeight: FontWeight = FontWeight.Normal,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(
            icon,
            contentDescription = iconDescription,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text,
            fontSize = 14.sp,
            fontWeight = textWeight
        )
    }
}