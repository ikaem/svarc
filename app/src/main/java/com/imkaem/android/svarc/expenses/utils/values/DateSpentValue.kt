package com.imkaem.android.svarc.expenses.utils.values

import java.time.Instant

/* TODO not sure if this should be a model? */
data class DateSpentValue(
    val amount: Long,
    /* TODO i am not sure it Instant is better, */
    val date: Instant,
    /* TODO or actual individual integers are better */
//    val day: Int,
//    val month: Int,
//    val year: Int,
)