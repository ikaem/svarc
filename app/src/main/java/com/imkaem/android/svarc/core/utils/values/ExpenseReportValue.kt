package com.imkaem.android.svarc.core.utils.values

import java.time.Instant

data class ExpenseReportValue(
    /* DATE represents the end date of the period? or, the state on the date ... */
    /* TODO not to be confused with DateSpentValue, which holds value for a specific date */
    val date: Instant,
    val amount: Long,
)
