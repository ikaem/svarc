package com.imkaem.android.svarc.reports.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imkaem.android.svarc.reports.utils.values.GraphRowValue
import com.imkaem.android.svarc.ui.theme.ColorBlue
import com.imkaem.android.svarc.ui.theme.ColorGrey
import com.imkaem.android.svarc.ui.theme.ColorGreyLighterEr
import com.imkaem.android.svarc.ui.theme.ColorRed
import com.imkaem.android.svarc.ui.theme.ColorWhite

@Composable
fun GraphRows(
    rows: List<GraphRowValue>,
    /* TODO this is what we allow */
    maxAmount: Int,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
//            .background(ColorGreyLighterEr)
            .padding(
                top = 20.dp,
                start = 10.dp,
                end = 10.dp,
                bottom = 40.dp
            )
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        for (rowValue in rows) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(20.dp),
                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    rowValue.title,
                    fontSize = 14.sp,
                    color = ColorGrey,
                    modifier = Modifier.weight(0.3f)
                )
                Box(
                    Modifier
                        //                        .fillMaxWidth()
                        .weight(0.7f)
                        .height(50.dp)
                        .background(Color.Red),
                    contentAlignment = Alignment.CenterStart

                ) {
                    Row(
                    ) {
                        Box(
                            modifier = Modifier
//                                .weight(rowValue.valueRowWeight)
                                .let {
                                    if (rowValue.valueRowWeight > 0) {
                                        it.weight(rowValue.valueRowWeight)
                                    } else {
                                        it
                                    }
                                }
                                .fillMaxHeight()
                                .let {
                                    if (rowValue.value < 0) {
                                        it.background(ColorRed)
                                    } else if (rowValue.value > maxAmount) {
                                        it.background(ColorRed)
                                    } else {
                                        it.background(ColorBlue)
                                    }
                                }
//                                .background(ColorBlue)
                        )
                        Box(
                            modifier = Modifier
                                .let {
                                    if (rowValue.remainderRowWeight > 0) {
                                        it.weight(rowValue.remainderRowWeight)
                                    } else {
                                        it
                                    }
                                }
//                                .weight(remainderAmountWeight.toFloat())
                                .fillMaxHeight()
                                .background(ColorGrey)
                        )
                    }
                    Text(
//                        formattedAmount,
                        rowValue.label,
                        color = ColorWhite,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
            }
        }
    }

}