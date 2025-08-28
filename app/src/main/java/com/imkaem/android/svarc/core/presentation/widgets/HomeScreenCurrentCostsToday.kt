package com.imkaem.android.svarc.core.presentation.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imkaem.android.svarc.ui.theme.ColorGreen
import com.imkaem.android.svarc.ui.theme.ColorRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCurrentCostsToday(
    modifier: Modifier = Modifier
) {


    Column(
        modifier = modifier
    ) {
        Text(
            "TODAY",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TooltipedMetricSubtitle(
                subtitle = "Accumulated remainder",
                tooltipContent = "Amount of money left from previous days, minus what you have spent today."
            )
            Spacer(Modifier.height(5.dp))
            Text(
                "4 EUR",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = ColorGreen,
            )
        }
        Spacer(Modifier.height(10.dp))

        Row {
            Column {
//                Text("Spent")
                TooltipedMetricSubtitle(
                    subtitle = "Spent",
                    tooltipContent = "Total amount of money spent today.",
                    fontSize = 12.sp,
                )
                Text(
                    "12 EUR",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.width(50.dp))
            Column {
//                Text("Remainder")
                TooltipedMetricSubtitle(
                    subtitle = "Remainder",
                    tooltipContent = "Amount of money left from your daily budget after today's spending.",
                    fontSize = 12.sp,
                )
                Text(
                    "-2 EUR",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorRed,
                    )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TooltipedMetricSubtitle(
    subtitle: String,
    tooltipContent: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            subtitle,
            fontSize = fontSize,
        )
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = {
                PlainTooltip {
                    Text(tooltipContent)
                }

            },
            state = rememberTooltipState()
        ) {
            Icon(
                Icons.Filled.Info,
                contentDescription = "Accumulated remainder info",
                modifier = Modifier
                    .width(18.dp)
                    .padding(start = 5.dp)
            )
        }
    }
}