package com.imkaem.android.svarc.reports.utils.values

import com.imkaem.android.svarc.expenses.utils.values.DateSpentValue
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class GraphRowValueConverters {

    companion object {

        fun accumulatedRemainderGraphRowValuesFromDateSpendValues(
            dateSpents: List<DateSpentValue>,
            dailyBudget: Int,
        ): List<GraphRowValue> {

            /* ok, idea here is to:
            * we add up current day remainder, to previous day remainder - so we get accumutalted reminder for each day
            * */

            val rowValues = mutableListOf<GraphRowValue>()

            /* how do we do this?
            * for first, accumulated remainder is just daily budget - spent amount
            *
            *
            * */

            /* what is max for accumulated remainder?
            * yeah, so it is total budget for the period
            * */

            /* TODO it is important here that we have date spents for all days - so if some are missing, we need to include those as well */
            val maxValue = dailyBudget * dateSpents.size
//            var accumulatedRemainder = maxValue;


            /* lets plan this
            * we have
            * - initially accumulated remainder which is daily budget * number of days
            *   - this can only go down
            * - we also have max value - which is always daily budget * number of days
            *   - this is always constant
            *
            * - now, what is the value? value is current accumulated remainder - this is currently accumulated remainder - (daily budget - spent amount) = (180 - (1)) = 173
            * - now, what is the opposite value? it is the total spent amount up to now. so it is 7. how do we get 7? i guess it is max value - what is the current accumulated remiander - this is called accumulated spent amount
            *
            * */

            for (dateSpent in dateSpents) {

                val previousAccumulatedRemainder = if(rowValues.isEmpty()) {
                    maxValue
                } else {
                    rowValues.last().value // value is accumulated remainder
                }

                val dayCurrentAccumulatedRemainder = previousAccumulatedRemainder - dateSpent.amount // this is value
                val dayCurrentAccumulatedRemainderAbs = abs(dayCurrentAccumulatedRemainder)

                /* now we do calculation */
                val factor = 100.00 / maxValue
                val currentAccumulatedRemainderAmountPercentage = dayCurrentAccumulatedRemainderAbs * factor
                val currentAccumulatedRemainderAmountWeight = min(
                    currentAccumulatedRemainderAmountPercentage / 100.00,
                    1.00
                )

                val currentAccumulatedSpentAmountWeight = max(
                    1.00 - currentAccumulatedRemainderAmountWeight,
                    0.0
                )

                val title = run {
                    /* TODO extract this */
                    val utcLocalDate = dateSpent.date.atZone(ZoneOffset.UTC).toLocalDate()

                    val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.getDefault())
                    formatter.format(utcLocalDate)
                    val formattedDate = formatter.format(utcLocalDate)

                    formattedDate
                }

                val rowValue = GraphRowValue(
                    title = title,
                    value = dayCurrentAccumulatedRemainder,
                    currency = "EUR",
                    valueRowWeight = currentAccumulatedRemainderAmountWeight.toFloat(),
                    remainderRowWeight = currentAccumulatedSpentAmountWeight.toFloat(),
                )

                rowValues.add(rowValue)






//                ------------------------------
//                val previousDateAccumulatedRemainder = rowValues.lastOrNull()?.value ?: 0
//                val currentDateRemainder = maxValue - dateSpent.amount
////                val currentAccumulatedRemainder =
////                    previousDateAccumulatedRemainder + currentDateRemainder
//
//
//                /* ok now at this point we have to do calculkcations */
//                val currentAccumulatedRemainderAbs = abs(accumulatedRemainder)
//
//                val factor = 100.00 / maxValue
//                val currentAccumulatedRemainderAmountPercentage = currentAccumulatedRemainderAbs * factor
//
//                val currentAccumulatedRemainderAmountWeight = min(
//                    currentAccumulatedRemainderAmountPercentage / 100.00,
//                    1.00
//                )
//
//                /* TODO this might */
//                val spentAccumulatedAmountWeight = max(
//                    1.00 - currentAccumulatedRemainderAmountWeight,
//                    0.0
//                )
//
//                val title = run {
//                    /* TODO extract this */
//                    val utcLocalDate = dateSpent.date.atZone(ZoneOffset.UTC).toLocalDate()
//
//                    val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.getDefault())
//                    formatter.format(utcLocalDate)
//                    val formattedDate = formatter.format(utcLocalDate)
//
//                    formattedDate
//                }
//
//                val rowValue = GraphRowValue(
//                    title = title,
//                    value = currentAccumulatedRemainder,
//                    currency = "EUR",
//                    valueRowWeight = currentAccumulatedRemainderAmountWeight.toFloat(),
//                    remainderRowWeight = spentAccumulatedAmountWeight.toFloat(),
//                )
//
////                rowValue
//                rowValues.add(rowValue)
            }


            /* TODO temp only */

            return rowValues

        }

        fun dailyRemainderGraphRowValuesFromDateSpentValues(
            dateSpents: List<DateSpentValue>,
            dailyBudget: Int,
        ): List<GraphRowValue> {
            /* ok, how do we calculate this:
            * we need eacch day remainder value?
            * so we take day dudget, and subtract spent amount from it
            * day budget is row max value
            *
            * */

            val rowValues = dateSpents.map { dateSpent ->

                /* max value is the limit */

                /* actually, remainder is what we want to show? */
                val remainderAmount =
                    dailyBudget - dateSpent.amount // this can be positive, as in: 900 - 200 = 700, or it can be negative, as in 900 - 1200 = -300

                /* we now need to get absolute value of the remainder, because that is what we will be using to */
                val remainderAmountAbs = abs(remainderAmount)

                /* now we calculate stuff*/
                val factor = 100.00 / dailyBudget
                /* TODO this can possibly be extracted */
                val remainderAmountPercentage =
                    remainderAmountAbs * factor // this will be 66% if remainder is 66 and budget is 100

                /* TODO this will need to be clamped to 1 max as well - but lets see */
                val remainderAmountWeight = min(
                    remainderAmountPercentage / 100.00,
                    1.00
                ) // we want to clamp the weight to 1.00 max
                val spentAmountWeight = max(
                    1.00 - remainderAmountWeight,
                    0.0
                ) // we want to clamp to make sure we dont get negative values

                val title = run {
                    val utcLocalDate = dateSpent.date.atZone(ZoneOffset.UTC).toLocalDate()

                    val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.getDefault())
                    formatter.format(utcLocalDate)
                    val formattedDate = formatter.format(utcLocalDate)

                    formattedDate

//            "bla"
                }

                val rowValue = GraphRowValue(
                    title = title,
                    value = remainderAmount,
                    currency = "EUR",
                    valueRowWeight = remainderAmountWeight.toFloat(),
                    remainderRowWeight = spentAmountWeight.toFloat(),
                )

                rowValue
            }

            return rowValues
        }

        fun spentGraphRowValuesFromDateSpentValues(
            dateSpents: List<DateSpentValue>,
            dailyBudget: Int,
        ): List<GraphRowValue> {

            /* TODO this should actually have max value of daily budget */

//            val maxDateSpentValue = dateSpents.maxOfOrNull { it.amount } ?: 0

            val rowValues = dateSpents.map { dateSpent ->
                spentGraphRowValueFromDateSpentValue(
                    dateSpent = dateSpent,
                    dailyBudget = dailyBudget,
                )
            }

            return rowValues
        }


        /* TODO tep */
        private fun spentGraphRowValueFromDateSpentValue(
            dateSpent: DateSpentValue,
            dailyBudget: Int,
        ): GraphRowValue {
            val factor = 100.00 / dailyBudget

            val spentAmountPercentage =
                dateSpent.amount * factor // this will be 66% if amount is 66 and max is 100
            val spentAmountWeight =
                min(spentAmountPercentage / 100.00, 1.00) // we want to clamp the weight to 1.00 max
            val remainderWeight = max(
                1.00 - spentAmountWeight,
                0.0
            ) // we want to clamp to make sure we dont get negative values

            val title = run {
                val utcLocalDate = dateSpent.date.atZone(ZoneOffset.UTC).toLocalDate()

                val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.getDefault())
                formatter.format(utcLocalDate)
                val formattedDate = formatter.format(utcLocalDate)

                formattedDate

//            "bla"
            }

            return GraphRowValue(
                title = title,
                value = dateSpent.amount,
                currency = "EUR",
                valueRowWeight = spentAmountWeight.toFloat(),
                remainderRowWeight = remainderWeight.toFloat(),
            )
        }

    }
}