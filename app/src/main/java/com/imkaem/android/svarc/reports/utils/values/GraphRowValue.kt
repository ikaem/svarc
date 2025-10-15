package com.imkaem.android.svarc.reports.utils.values

import java.time.Instant
import java.util.Locale

/* TODO maybe this should be a model, not sure */
data class GraphRowValue(
    val date: Instant,
    val title: String,
    /* TODO keep in mind this is in cents */
    val value: Long,
    val currency: String,
    val valueRowWeight: Float,
    val remainderRowWeight: Float,
) {

    /* TODO not sure i should be doing this here */
    val label: String
        get() {
//            val formattedValue = String.format(Locale.getDefault(),"%.2f", value)

            val valueInUnits = value / 100.00

            val formattedValue = String.Companion.format(Locale.getDefault(),"%.2f", valueInUnits)

            return "$formattedValue $currency"

//           return "$value $currency"
        }
}