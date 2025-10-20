package com.imkaem.android.svarc.core.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imkaem.android.svarc.ui.theme.ColorGreyDark
import com.imkaem.android.svarc.ui.theme.ColorGreyLighter
import java.util.Locale

/* TODO this could potentially be renamded to Month - because we no longer hold week */
@Composable
fun HomeScreenCurrentCostsPeriod(
    periodLabel: String,
    periodSpentValue: PeriodSpentValue?,
    periodRemainderAmountReportValue: PeriodSpentValue?,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ColorGreyLighter,
) {
    Column(
        modifier = modifier
            .background(backgroundColor)
            .padding(10.dp)
    ) {
        Text(
            periodLabel,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = ColorGreyDark
        )
        Spacer(Modifier.height(5.dp))

        Row(
//            horizontalArrangement = Arrangement.spacedBy(20.dp),
//            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
//            PeriodMetricBlock(
//                modifier = Modifier.weight(1f),
//                label = "Accumulated remainder",
//                value = "-11 EUR",
//            )
            PeriodMetricBlock(
                label = "Spent",
                //                value = "20 EUR",
                value = periodSpentValue.let { it ->

                    /* TODO this should be extracted, as it will be reused in other period UIs*/
                    /* maybe even as some kind of extension */

                    val majorDenominator = 100.00
                    val amount = it?.amount ?: 0

                    val major = amount / majorDenominator
                    val output = String.format(Locale.getDefault(), "%.2f EUR", major)

                    output
                },
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                PeriodMetricBlock(
                    label = "Month budget",
                    value = periodRemainderAmountReportValue.let { it ->

                        /* TODO this should be extracted, as it will be reused in other period UIs*/
                        /* maybe even as some kind of extension */

                        val majorDenominator = 100.00
                        val amount = it?.amount ?: 0

                        val major = amount / majorDenominator
                        val output = String.format(Locale.getDefault(), "%.2f EUR", major)

                        output
                    },
                    valueSize = 14.sp
                )
                PeriodMetricBlock(
                    label = "Remainder",
                    value = periodRemainderAmountReportValue.let { it ->

                        /* TODO this should be extracted, as it will be reused in other period UIs*/
                        /* maybe even as some kind of extension */

                        val majorDenominator = 100.00
                        val amount = it?.amount ?: 0

                        val major = amount / majorDenominator
                        val output = String.format(Locale.getDefault(), "%.2f EUR", major)

                        output
                    },
                    valueSize = 14.sp
                )
            }

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