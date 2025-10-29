package com.imkaem.android.svarc.expenses.domain.models

import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

/* this should represent eventual entity in database - there should be a table with periods (month), and an amount for each month */
data class PeriodMonthModel(
    val id: Int,
    val month: Int,
    val year: Int,
    val amount: Int,
) {

    /* TODO not sure if i should be doing this */
    val name: String
        get() {
            val name =
                Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
            return "$name, $year"
        }
}
