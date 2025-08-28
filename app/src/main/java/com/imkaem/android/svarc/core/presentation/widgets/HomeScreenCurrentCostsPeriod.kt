package com.imkaem.android.svarc.core.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imkaem.android.svarc.ui.theme.ColorGreyDark
import com.imkaem.android.svarc.ui.theme.ColorGreyLighter

@Composable
fun HomeScreenCurrentCostsPeriod(
    periodLabel: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ColorGreyLighter,
) {
    Column(
        modifier = modifier.background(backgroundColor).padding(10.dp)
    ) {
        Text(
            periodLabel,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = ColorGreyDark
        )
        Spacer(Modifier.height(5.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            PeriodMetricBlock(
                modifier = Modifier.weight(1f),
                label = "Accumulated remainder",
                value = "-11 EUR",
            )
            PeriodMetricBlock(
                label = "Spent",
                value = "20 EUR",
                valueSize = 14.sp
            )
            PeriodMetricBlock(
                label = "Remainder",
                value = "30 EUR",
                valueSize = 14.sp
            )
        }
    }
}

@Composable
private fun PeriodMetricBlock(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueSize: TextUnit = 20.sp,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            label,
            fontSize = 12.sp,
        )
        Text(
            value,
            fontSize = valueSize,
            fontWeight = FontWeight.Bold,
        )
    }
}