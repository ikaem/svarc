package com.imkaem.android.svarc.costs.presentation.widgets

import CustomTabbedView
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imkaem.android.svarc.core.presentation.widgets.CustomOptionsField
import com.imkaem.android.svarc.core.utils.values.CustomOptionFieldValue
import com.imkaem.android.svarc.costs.utils.values.DateSpentValue
import com.imkaem.android.svarc.ui.theme.ColorBlue
import com.imkaem.android.svarc.ui.theme.ColorGrey
import com.imkaem.android.svarc.ui.theme.ColorGreyDark
import com.imkaem.android.svarc.ui.theme.ColorGreyLighter
import com.imkaem.android.svarc.ui.theme.ColorGreyLighterEr
import com.imkaem.android.svarc.ui.theme.ColorWhite
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

@Composable
fun ReportsScreenGraphs() {
    /* TODO maybe this should be moved inside the composable that renders CustomZTabbefView for graph types */
    val graphTypesSelectedIndex = remember {
        mutableIntStateOf(0)
    }

    Column(
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        /* period selector - month in this case */
        /* TODO extract to a composable */
        GraphsMonthSelector()
        CustomTabbedView(
            currentTabIndex = graphTypesSelectedIndex.intValue,
            onTabSelected = { index -> graphTypesSelectedIndex.intValue = index },
            tabLabels = listOf("SPENT", "DAILY REMAINDER", "ACCUMULATED REMAINDER"),
            tabLabelTextSize = 12.sp,
            tabs = listOf(
                /* TODO i guess we could integrate padding inside the CustomTabbedView, so we dont have to do it individually */
                { GraphsSpentTabContent() },
                { Text("Daily Remainder") },
                { Text("Accumulated Remainder") },
            ),
            isScrollable = true,
            activeContainerColor =
                ColorGrey,
            containerColor = ColorGreyLighterEr,
//            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}

@Composable
private fun GraphsSpentTabContent(
//    dateCosts: List<DateSpentValue>,
    modifier: Modifier = Modifier
) {

    /* TODO this should be done by a model */
    val dateSpents = List(31) { index ->

        val now = Instant.now().atZone(ZoneId.systemDefault())
        val year = now.year
        val month = now.monthValue
        val day = index + 1

        val zonedDateTime: ZonedDateTime = ZonedDateTime.of(
            year,
            month,
            day,
            ZoneId.systemDefault()
        )

        val dateInstant = zonedDateTime.toInstant()

        DateSpentValue(
            date = dateInstant,
            amount = (index + 1) * 2
        )
    }

    /* now we want to convert dateSpents to some value that is used to render data */

    val maxDateSpent = dateSpents.maxOfOrNull { it.amount } ?: 0
    val rowTotalWeightToMaxDateSpentRatio = 100.00 / maxDateSpent // for 60 it will be 1.66
    val maxRowWeight = maxDateSpent * rowTotalWeightToMaxDateSpentRatio // for 60 it will be 100.00


    val graphRowValues = dateSpents.map { it ->

        val spentAmountPercentage = it.amount * rowTotalWeightToMaxDateSpentRatio

        val spentAmountWeight = min(spentAmountPercentage / 100.00, 1.00)
        val remainderAmountWeight = max(maxRowWeight - spentAmountPercentage, 0.00) / 100.00


        val formattedAmount = String.format(Locale.getDefault(), "%.2f", it.amount)

        val label = run {
            /* TODO instant we can use formatter as well */
            val zonedDateTime = it.date.atZone(ZoneId.systemDefault())
            val formatter = DateTimeFormatter
//            val day = zonedDateTime.dayOfMonth
//            val month = zonedDateTime.monthValue
//            val year = zonedDateTime.year
//            "$day.$month.$year"
        }


    }




    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(ColorGreyLighterEr)
            .padding(
                top = 20.dp,
                start = 10.dp,
                end = 10.dp,
                bottom = 40.dp
            )
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        for (i in 1..31) {

            /* TODO i guess tbis logic could be extracted */
            /* TODO all of this logic should be done in a view model or use case */
            val amount = i.toDouble() * 2
            val maxAmount = 60.00 // -> becasue this is the daily budget
            val formattedAmount = String.format(Locale.getDefault(), "%.2f", amount)

            val ratio = 100.00 / maxAmount // we will get 1.66 for 60
            val maxAmountWidth = maxAmount * ratio // for 60 it will be 100
            val maxAmountWeight = maxAmountWidth / 100.00 // this will be always 1

            val spendAmountWidth = amount * ratio
            val spentAmountWeight = min(spendAmountWidth / 100.00, 1.00)

            val remainderAmountWidth = maxAmountWidth - spendAmountWidth
            val remainderAmountWeight = max(maxAmountWeight - spentAmountWeight, 0.00)

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(20.dp),
                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    "$i Oct, 2025",
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
                                .weight(spentAmountWeight.toFloat())
                                .fillMaxHeight()
                                .background(ColorBlue)
                        )
                        Box(
                            modifier = Modifier
                                .let {
                                    if (remainderAmountWeight > 0) {
                                        it.weight(remainderAmountWeight.toFloat())
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
                        formattedAmount,
                        color = ColorWhite,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
            }
        }
    }
}

/* TODO still not sure if this should stay here */
@Composable
private fun GraphsMonthSelector() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            /* TODO thi should be come kind of constants because it is reused */
            .padding(
                vertical = 20.dp,
            )
            .height(66.dp)
            .fillMaxWidth()
    ) {
        Column(
            Modifier
                .background(ColorGreyLighter)
                .padding(5.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Daily budget",
                fontSize = 12.sp,
                color = ColorGreyDark,
            )
            Text(
                "60 EUR",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            Modifier
//                    .background(ColorGreyLight)
                .weight(1f)
                .padding(5.dp)
        ) {
//                Text("Month")
            CustomOptionsField(
                isOptionsShown = false,
                selectedOption = CustomOptionFieldValue(key = 1, label = "November, 2025"),
                options = listOf(),
                onToggleOptionsShown = {},
                onSelectOption = {},
                leadingIcon = Icons.Filled.CalendarMonth,
                label = "Select month",
            )
        }
    }
}